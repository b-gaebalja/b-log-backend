package com.bgaebalja.blogbackend.share.domain;

public class ShareRequestDto {
    private Long sharerId;
    private String url;

    // Getter와 Setter
    public Long getSharerId() {
        return sharerId;
    }

    public void setSharerId(Long sharerId) {
        this.sharerId = sharerId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
