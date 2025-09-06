package com.localtalk.api.auth.config

import com.localtalk.api.auth.domain.AuthRole
import com.localtalk.api.auth.infrastructure.token.JwtTokenDecoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    val jwtTokenDecoder: JwtTokenDecoder,
    val corsConfigurationSource: CorsConfigurationSource,
) {

    @Bean
    fun jwtDecoder(): JwtDecoder = JwtDecoder { token ->
        jwtTokenDecoder.decodeToJwt(token)
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .cors { it.configurationSource(corsConfigurationSource) }
            .csrf { it.disable() }
            .httpBasic { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/api/v1/social-logins/**").permitAll()
                    .requestMatchers("/api/v1/districts/**").permitAll()
                    .requestMatchers("/api/v1/auth/refresh").permitAll()
                    .requestMatchers("/api/v1/interests/**").permitAll()
                    .requestMatchers("/api/v1/members/nickname/validate").authenticated()
                    .requestMatchers("/api/v1/files").authenticated()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    .requestMatchers("/api/v1/**").hasRole(AuthRole.MEMBER.name)
                    .anyRequest().authenticated()
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { jwt ->
                    jwt.decoder(jwtDecoder())
                }
            }
            .build()
    }
}
