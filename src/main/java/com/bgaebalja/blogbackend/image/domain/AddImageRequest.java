package com.bgaebalja.blogbackend.image.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import static com.bgaebalja.blogbackend.util.ApiConstant.ID_EXAMPLE;

@NoArgsConstructor
@Getter
@Setter
public class AddImageRequest {
    private static final String IMAGE_VALUE = "이미지";

    private static final String TARGET_TYPE_VALUE = "대상 타입";
    private static final String TARGET_TYPE_EXAMPLE = "POST";

    private static final String TARGET_ID_VALUE = "대상 ID";

    @Schema(description = IMAGE_VALUE)
    private MultipartFile image;

    @Schema(description = TARGET_TYPE_VALUE, example = TARGET_TYPE_EXAMPLE)
    private String targetType;

    @Schema(description = TARGET_ID_VALUE, example = ID_EXAMPLE)
    private String targetId;
}
