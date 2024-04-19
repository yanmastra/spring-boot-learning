package com.yanmastra.msSecurityBase.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String ROLE_ = "ROLE_";
    private static final String REALM_ACCESS = "realm_access";

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        List<SimpleGrantedAuthority> roles = extractAuthorities(jwt);
        return new UserPrincipal(jwt, roles);
    }

    private List<SimpleGrantedAuthority> extractAuthorities(Jwt jwt) {
        if(jwt.hasClaim(REALM_ACCESS) && jwt.getClaim(REALM_ACCESS) != null) {
            // Todo: this part only use realm_access, need to load resource_access too as authorities
            Map<String, Object> realmAccess = jwt.getClaim(REALM_ACCESS);
            List<String> keycloakRoles = objectMapper.convertValue(realmAccess.get("roles"), new TypeReference<>() {
            });
            return keycloakRoles.stream().map(role -> new SimpleGrantedAuthority(ROLE_+role)).toList();
        }
        return new ArrayList<>();
    }
}