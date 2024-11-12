package com.bgaebalja.blogbackend.user.controller

import com.bgaebalja.blogbackend.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

private const val GET_KAKAO_TOKEN = "프론트에서 카카오 토큰을 받음"
private const val GET_KAKAO_TOKEN_DESCRIPTION =
    "프론트에서 받은 카카오 토큰을 통해 카카오 유저 정보를 JSON 형태로 반환한다"
private const val KAKAO_TOKEN = "카카오 회원 인증 토큰"

@Tag(name = "Social Controller", description = "소셜 인증을 위한 API")
@RestController
@RequestMapping("/users")
class SocialController(private val userService: UserService) {

    @Operation(summary = GET_KAKAO_TOKEN, description = GET_KAKAO_TOKEN_DESCRIPTION)
    @GetMapping("/kakao")
    fun getKakaoUser(
        @Parameter(description = KAKAO_TOKEN) accessToken: String): Mono<String> {
     return userService.getKakaoUser(accessToken).map { it.toString() }
    }

}