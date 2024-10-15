package com.bgaebalja.blogbackend.image.domain;

import com.bgaebalja.blogbackend.post.domain.Post;

import java.util.List;
import java.util.stream.Collectors;

import static com.bgaebalja.blogbackend.image.domain.TargetType.POST;

public class RepresentativeImagesRequest {
    private TargetType targetType;
    private List<Long> targetIds;

    private RepresentativeImagesRequest(TargetType targetType, List<Long> targetIds) {
        this.targetType = targetType;
        this.targetIds = targetIds;
    }

    public static RepresentativeImagesRequest from(List<Post> posts) {
        List<Long> targetIds = posts.stream().map(Post::getId).collect(Collectors.toList());

        return new RepresentativeImagesRequest(POST, targetIds);
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public Long get(int index) {
        return targetIds.get(index);
    }

    public int size() {
        return targetIds.size();
    }
}
