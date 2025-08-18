package com.localtalk.api.auth.config

import com.localtalk.api.auth.domain.AuthRole
import com.localtalk.api.auth.infrastructure.token.ID_CLAIM
import com.localtalk.api.auth.infrastructure.token.JwtTokenDecoder
import com.localtalk.api.auth.infrastructure.token.ROLE_CLAIM
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
    val jwtTokenDecoder: JwtTokenDecoder,
) {

    @Bean
    fun jwtDecoder(): JwtDecoder = JwtDecoder { token ->
        jwtTokenDecoder.decodeToJwt(token)
    }


    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter = JwtAuthenticationConverter().apply {
        setJwtGrantedAuthoritiesConverter { jwt ->
            val role = jwt.getClaim<String>(ROLE_CLAIM)
            listOf(SimpleGrantedAuthority("ROLE_$role"))
        }
        setPrincipalClaimName(ID_CLAIM)
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .httpBasic { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/api/v1/social-logins/**").permitAll()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    .requestMatchers("/api/v1/**").hasRole(AuthRole.MEMBER.name)
                    .anyRequest().authenticated()
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { jwt ->
                    jwt.decoder(jwtDecoder())
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())
                }
            }
            .build()
    }
}
