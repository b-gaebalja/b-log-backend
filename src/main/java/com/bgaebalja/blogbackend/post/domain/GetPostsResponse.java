package com.bgaebalja.blogbackend.post.domain;

import com.bgaebalja.blogbackend.image.domain.Image;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GetPostsResponse {
    private List<GetPostResponse> getPostResponses;
    private PageInformation pageInformation;

    private GetPostsResponse(List<GetPostResponse> getPostResponses, PageInformation pageInformation) {
        this.getPostResponses = getPostResponses;
        this.pageInformation = pageInformation;
    }

    public static GetPostsResponse from(Page<Post> pagedPosts, List<Image> representativeImages) {
        List<Post> posts = pagedPosts.getContent();
        List<GetPostResponse> getProductResponses = new ArrayList<>();
        for (int i = 0; i < posts.size(); i++) {
            getProductResponses.add(GetPostResponse.from(posts.get(i), representativeImages.get(i)));
        }

        return new GetPostsResponse(getProductResponses, PageInformation.from(pagedPosts));
    }

    public GetPostResponse get(int index) {
        return getPostResponses.get(index);
    }
}
