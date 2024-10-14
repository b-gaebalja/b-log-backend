package com.bgaebalja.blogbackend.image.domain;

import com.bgaebalja.blogbackend.audit.BaseGeneralEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.NoArgsConstructor;

import static com.bgaebalja.blogbackend.util.EntityConstant.BOOLEAN_DEFAULT_FALSE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class Image extends BaseGeneralEntity {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TargetType targetType;

    @Column(nullable = false)
    private Long targetId;

    @Column(name = "s3_url", nullable = false, length = 500)
    private String s3Url;

    @Column(nullable = false, columnDefinition = BOOLEAN_DEFAULT_FALSE)
    private boolean representativeYn;

    private Image(TargetType targetType, Long targetId, String s3Url, boolean isRepresentative) {
        this.targetType = targetType;
        this.targetId = targetId;
        this.s3Url = s3Url;
        representativeYn = isRepresentative;
    }

    public static Image of(TargetType targetType, Long targetId, String s3Url, boolean isRepresentative) {
        return new Image(targetType, targetId, s3Url, isRepresentative);
    }

    public String getS3Url() {
        return s3Url;
    }
}
