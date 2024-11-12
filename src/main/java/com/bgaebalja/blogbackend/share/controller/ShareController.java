package com.bgaebalja.blogbackend.share.controller;

import com.bgaebalja.blogbackend.image.service.ImageService;
import com.bgaebalja.blogbackend.share.domain.ShareRequestDto;
import com.bgaebalja.blogbackend.share.domain.Share;
import com.bgaebalja.blogbackend.share.service.ShareService;
import com.bgaebalja.blogbackend.util.FormatConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<Void> share(@PathVariable("id") String postId, @RequestBody ShareRequestDto requestDto) {
        Long postIdLong = FormatConverter.parseToLong(postId);
        Long sharerId = requestDto.getSharerId();
        String url = requestDto.getUrl();

        // shareService에서 필요한 로직을 추가
        shareService.sharePost(postIdLong, sharerId, url); // 서비스 메서드에 sharerId와 url 전달

        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
    }


    //북마크 목록 가져오기
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
