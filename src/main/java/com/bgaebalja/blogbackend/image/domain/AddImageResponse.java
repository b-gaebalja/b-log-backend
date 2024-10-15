package com.bgaebalja.blogbackend.image.domain;

import lombok.Getter;

@Getter
public class AddImageResponse {
    private Long id;
    private String url;

    private AddImageResponse(Long id, String url) {
        this.id = id;
        this.url = url;
    }

    public static AddImageResponse from(Image image) {
        return new AddImageResponse(image.getId(), image.getS3Url());
    }
}
