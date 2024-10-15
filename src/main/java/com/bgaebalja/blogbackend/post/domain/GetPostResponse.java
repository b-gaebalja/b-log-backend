package com.bgaebalja.blogbackend.post.domain;

import com.bgaebalja.blogbackend.image.domain.Image;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class GetPostResponse {
    private Long id;
    private String username;
    private String content;
    private String representativeImageUrl;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime modifiedAt;

    public static GetPostResponse from(Post post) {
        return GetPostResponse.builder()
                .username(post.getWriter().getUsername())
                .content(post.getContent().getValue())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .build();
    }

    public static GetPostResponse from(Post post, Image representativeImage) {
        return GetPostResponse.builder()
                .id(post.getId())
                .username(post.getWriter().getUsername())
                .content(post.getContent().getValue())
                .representativeImageUrl(representativeImage.getS3Url())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .build();
    }
}
