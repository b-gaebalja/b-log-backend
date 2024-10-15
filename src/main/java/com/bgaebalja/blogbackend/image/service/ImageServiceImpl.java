package com.bgaebalja.blogbackend.image.service;

import com.bgaebalja.blogbackend.image.domain.AddImageRequest;
import com.bgaebalja.blogbackend.image.domain.Image;
import com.bgaebalja.blogbackend.image.domain.RepresentativeImagesRequest;
import com.bgaebalja.blogbackend.image.domain.TargetType;
import com.bgaebalja.blogbackend.image.exception.S3UploadFailedException;
import com.bgaebalja.blogbackend.image.repository.ImageRepository;
import com.bgaebalja.blogbackend.util.FormatConverter;
import com.bgaebalja.blogbackend.util.FormatValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.bgaebalja.blogbackend.image.exception.ExceptionMessage.S3_UPLOAD_FAILED_EXCEPTION_MESSAGE;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final S3Client s3Client;

    private static final String TLS_HTTP_PROTOCOL = "https://";
    private static final String S3_ADDRESS = "s3.amazonaws.com";
    private static final String CACHE_VALUE = "images";

    private static final String CREATE_IMAGE_KEY = "#addImagesRequest.targetId";
    private static final String CREATE_KEY_CONDITION = "#addImagesRequest != null && #addImagesRequest.targetId != null";

    private static final String GET_IMAGE_KEY = "#targetId";
    private static final String GET_KEY_CONDITION = "#targetId != null";

    private static final String DELETE_IMAGE_KEY = "#id";
    private static final String DELETE_KEY_CONDITION = "#id != null";

    @Value("${cloud.aws.s3.bucket-name}")
    private String s3BucketName;

    @Override
    @Transactional(isolation = READ_COMMITTED, timeout = 10)
    @CachePut(key = CREATE_IMAGE_KEY, condition = CREATE_KEY_CONDITION, value = CACHE_VALUE)
    public Image createImage(AddImageRequest addImageRequest) {
        MultipartFile imageToUpload = addImageRequest.getImage();
        TargetType targetType = FormatConverter.parseToTargetType(addImageRequest.getTargetType());
        Long targetId = FormatConverter.parseToLong(addImageRequest.getTargetId());

        List<Image> existingImages = imageRepository.findByTargetTypeAndTargetIdAndDeleteYnFalseOrderByCreatedAtDesc(targetType, targetId);
        boolean isRepresentativeImageExistent = FormatValidator.hasValue(existingImages);
        Image image = Image.of(
                targetType, targetId, uploadToS3(imageToUpload, targetType, targetId), !isRepresentativeImageExistent
        );
        imageRepository.save(image);

        return image;
    }

    private String uploadToS3(MultipartFile image, TargetType targetType, Long productId) {
        String sanitizedFilename = FormatConverter.sanitizeFileName(image.getOriginalFilename());
        long ms = System.currentTimeMillis();
        String key
                = String.format("%s/%d/%s_%s", targetType.toString().toLowerCase(), productId, ms, sanitizedFilename);

        try {
            File tempFile = convertMultipartFileToFile(image);
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket(s3BucketName)
                    .key(key)
                    .build(), Paths.get(tempFile.getPath()));
            tempFile.delete();
        } catch (IOException ie) {
            throw new S3UploadFailedException(S3_UPLOAD_FAILED_EXCEPTION_MESSAGE, ie);
        }

        return String.format("%s%s.%s/%s", TLS_HTTP_PROTOCOL, s3BucketName, S3_ADDRESS, key);
    }

    private File convertMultipartFileToFile(MultipartFile image) throws IOException {
        File convertedFile
                = new File(String.format("%s/%s", System.getProperty("java.io.tmpdir"), image.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(image.getBytes());
        }
        return convertedFile;
    }

    @Override
    @Transactional(isolation = READ_COMMITTED, readOnly = true, timeout = 10)
    public List<Image> getImages(TargetType targetType, Long targetId) {
        return imageRepository.findByTargetTypeAndTargetIdAndDeleteYnFalseOrderByCreatedAtDesc(targetType, targetId);
    }

    @Override
    @Transactional(isolation = REPEATABLE_READ, readOnly = true, timeout = 20)
    public List<Image> getRepresentativeImages(RepresentativeImagesRequest representativeImagesRequest) {
        List<Image> images = new ArrayList<>();
        TargetType targetType = representativeImagesRequest.getTargetType();

        for (int i = 0; i < representativeImagesRequest.size(); i++) {
            Long targetId = representativeImagesRequest.get(i);
            images.add(
                    imageRepository
                            .findByTargetTypeAndTargetIdAndRepresentativeYnTrueAndDeleteYnFalse(targetType, targetId)
                            .orElse(
                                    imageRepository
                                            .findFirstByTargetTypeAndTargetIdAndDeleteYnFalseOrderByCreatedAt(
                                                    targetType, targetId
                                            )
                                            .orElse(Image.of(targetType, targetId, "https://ddipddipddip.s3.ap-northeast-2.amazonaws.com/product/10/1726398668465_DDIP+(3).png", false))
                            )
            );
        }

        return images;
    }
}
