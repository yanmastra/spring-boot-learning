package com.yanmastra.msSecurityBase.security;

import com.yanmastra.msSecurityBase.Log;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.yanmastra.msSecurityBase.security.SecurityConstant.KEY_COMPANY_ACCESS;
import static com.yanmastra.msSecurityBase.security.SecurityConstant.KEY_HEADER_COMPANY_ID;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String ROLE_ = "ROLE_";
    private static final String REALM_ACCESS = "realm_access";
    private static final String RESOURCE_ACCESS = "resource_access";
    private static final String ROLES = "roles";

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final KeycloakAccessDeniedHandler accessDeniedHandler;

    public JwtAuthConverter(HttpServletRequest request, HttpServletResponse response, KeycloakAccessDeniedHandler accessDeniedHandler) {
        this.request = request;
        this.response = response;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        String companyId = request.getHeader(KEY_HEADER_COMPANY_ID);
        if (StringUtils.isNotBlank(companyId) && !jwt.getClaimAsStringList(KEY_COMPANY_ACCESS).contains(companyId)) {
            try {
                accessDeniedHandler.handle(request, response, new AccessDeniedException("You do not have access to the company:" + companyId));
            } catch (IOException ex) {
                Log.log.error(ex.getMessage(), ex);
            }
        }

        List<SimpleGrantedAuthority> roles = extractAuthorities(jwt);
        return new UserPrincipal(jwt, roles, companyId);
    }

    private List<SimpleGrantedAuthority> extractAuthorities(Jwt jwt) {
        Set<String> authorities = new HashSet<>();
        if (jwt.getClaims().get(REALM_ACCESS) instanceof Map<?,?> realmAccess && realmAccess.containsKey(ROLES)) {
            List<String> roles = (List<String>) realmAccess.get(ROLES);
            authorities.addAll(roles.stream()
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toSet()));
        }
        if (jwt.getClaims().get(RESOURCE_ACCESS) instanceof Map<?,?>) {
            Map<String, Object> resourceAccess = (Map<String, Object>) jwt.getClaims().get(RESOURCE_ACCESS);
            for (String key: resourceAccess.keySet()) {
                if (resourceAccess.get(key) instanceof Map<?, ?> roleAccChild && roleAccChild.containsKey(ROLES)) {
                    authorities.add(key);
                    List<String> roles = (List<String>) roleAccChild.get(ROLES);
                    authorities.addAll(roles.stream().map(v -> {
                        if (StringUtils.isNotBlank(v)) return v;
                        return null;
                    }).filter(Objects::nonNull).collect(Collectors.toSet()));
                }
            }
        }
        return authorities.stream().map(role -> new SimpleGrantedAuthority(ROLE_+role)).toList();
    }
}