package com.bgaebalja.blogbackend.config

import com.bgaebalja.blogbackend.security.filter.JwtFilter
import com.bgaebalja.blogbackend.security.handler.LoginFailureHandler
import com.bgaebalja.blogbackend.security.handler.LoginSuccessHandler
import com.bgaebalja.blogbackend.util.JwtUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.*
import org.springframework.http.HttpMethod
import org.springframework.http.HttpMethod.*
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy.NEVER
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableMethodSecurity
class SecurityConfig(
    private val jwtUtil: JwtUtil
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.cors{it.configurationSource(corsConfigurationSource())}
        http.sessionManagement { it.sessionCreationPolicy(NEVER) }
        http.csrf {it.disable()}
        http.formLogin{
            it.loginPage("/users/login")
            it.successHandler(LoginSuccessHandler(jwtUtil))
            it.failureHandler(LoginFailureHandler()) }
        http.addFilterBefore(JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOriginPatterns = listOf("*")
        configuration.allowedMethods = listOf(GET.toString(), POST.toString(), PUT.toString(), PATCH.toString(),DELETE.toString(), OPTIONS.toString())
        configuration.allowedHeaders = listOf(AUTHORIZATION, CACHE_CONTROL, CONTENT_TYPE)
        configuration.exposedHeaders = listOf(LOCATION)
        configuration.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

}