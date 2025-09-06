package com.localtalk.api.auth.config

import com.localtalk.api.auth.domain.AuthMember
import com.localtalk.api.auth.domain.AuthRole
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.stereotype.Component

@Component
class LocalTalkJwtAuthenticationConverter : Converter<Jwt, AbstractAuthenticationToken> {

    private val jwtGrantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter().apply {
        setAuthorityPrefix("ROLE_")
        setAuthoritiesClaimName("role")
    }

    override fun convert(jwt: Jwt): AbstractAuthenticationToken {
        val authorities = jwtGrantedAuthoritiesConverter.convert(jwt) ?: emptyList()

        val id = jwt.getClaim<Number>("id").toLong()
        val roleString = jwt.getClaim<String>("role")
        val role = AuthRole.valueOf(roleString)

        val authMember = AuthMember(id, role)

        return UsernamePasswordAuthenticationToken(authMember, null, authorities)
    }
}
