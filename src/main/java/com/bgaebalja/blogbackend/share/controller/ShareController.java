package com.bgaebalja.blogbackend.share.controller;

import com.bgaebalja.blogbackend.image.domain.RepresentativeImagesRequest;
import com.bgaebalja.blogbackend.image.service.ImageService;
import com.bgaebalja.blogbackend.post.domain.GetPostsResponse;
import com.bgaebalja.blogbackend.post.domain.Post;
import com.bgaebalja.blogbackend.post.util.PageableGenerator;
import com.bgaebalja.blogbackend.share.domain.Share;
import com.bgaebalja.blogbackend.share.service.ShareService;
import com.bgaebalja.blogbackend.util.FormatConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.bgaebalja.blogbackend.image.domain.TargetType.POST;
import static com.bgaebalja.blogbackend.post.util.PaginationConstant.*;
import static org.springframework.http.HttpStatus.OK;

@Controller
@RequestMapping("/sharePosts")
public class ShareController {

    private final ShareService shareService;
    private final ImageService imageService;

    public ShareController(ShareService shareService, ImageService imageService) {
        this.shareService = shareService;
        this.imageService = imageService;
    }

    //북마크하기
    @PostMapping("/{id}/shares")
    public ResponseEntity<Void> share(@PathVariable("id") String postId) {
        shareService.sharePost(FormatConverter.parseToLong(postId));
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
    }

    //북마크 목록 가져오기
//    @GetMapping("/list/{sharerId}")
//    public ResponseEntity<GetPostsResponse> getSharedPosts(
//            @PathVariable("sharerId") String sharerId,
//            @RequestParam(defaultValue = TRUE)
//            @Schema(description = IS_PAGINATION_USED, example = TRUE) String paged,
//            @RequestParam(defaultValue = ZERO)
//            @Schema(description = CURRENT_PAGE_NUMBER, example = ZERO) String pageNumber,
//            @RequestParam(defaultValue = TWELVE)
//            @Schema(description = NUMBER_OF_ITEMS_PER_PAGE, example = TWELVE) String size,
//            @RequestParam(defaultValue = ORDER_BY_CREATED_AT_DESCENDING)
//            @Schema(description = SORTING_METHOD, example = ORDER_BY_CREATED_AT_DESCENDING) String sort) {
//        System.out.println("목록 가져오기 컨트롤러 진입 성공");
//        Pageable pageable = PageableGenerator.createPageable(paged, pageNumber, size, sort);
//        Page<Post> sharedPosts = shareService.getSharedPosts(pageable, FormatConverter.parseToLong(sharerId));
//        GetPostsResponse getPostsResponse
//                = GetPostsResponse.from(
//                sharedPosts,
//                imageService.getRepresentativeImages(RepresentativeImagesRequest.from(sharedPosts.getContent()))
//        );
//
//        return ResponseEntity.status(OK).body(getPostsResponse); // 200 OK
//    }

    @GetMapping("/list/{sharerId}")
    public ResponseEntity<List<Long>> getSharedPosts(
            @PathVariable("sharerId") String sharerId) {
        List<Share> sharedPosts = shareService.getSharedPosts(FormatConverter.parseToLong(sharerId));

        // 공유된 게시물의 postId 목록을 추출
        List<Long> postIds = sharedPosts.stream()
                .map(Share::getPostId) // Share 객체에서 postId를 추출
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(postIds); // 200 OK와 함께 postId 목록 반환
    }

    //북마크 삭제하기
    @Transactional
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Void> deleteSharedPost(@PathVariable("postId") String postId) {
        shareService.deleteSharedPost(FormatConverter.parseToLong(postId));
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
