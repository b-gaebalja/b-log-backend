package com.bgaebalja.blogbackend.image.controller;

import com.bgaebalja.blogbackend.image.domain.AddImageRequest;
import com.bgaebalja.blogbackend.image.domain.AddImageResponse;
import com.bgaebalja.blogbackend.image.domain.Image;
import com.bgaebalja.blogbackend.image.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.HttpStatus.CREATED;

@Controller
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    private static final String ADD_IMAGE = "이미지 추가";
    private static final String ADD_IMAGE_DESCRIPTION = "이미지와 대상 타입, 대상 ID를 입력해 이미지를 추가할 수 있습니다.";
    private static final String ADD_IMAGE_FORM = "이미지 추가 양식";

    @Operation(summary = ADD_IMAGE, description = ADD_IMAGE_DESCRIPTION)
    @PostMapping()
    public ResponseEntity<AddImageResponse> addImage(
            @ModelAttribute @Parameter(description = ADD_IMAGE_FORM) AddImageRequest addImageRequest
    ) {
        Image image = imageService.createImage(addImageRequest);
        return ResponseEntity.status(CREATED).body(AddImageResponse.from(image));
    }
}
