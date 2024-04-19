package com.yanmastra.msSecurityBase.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yanmastra.msSecurityBase.utils.DateTimeUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Transient;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Transient
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIncludeProperties({"id", "name", "username", "email", "email_verified", "company_access", "authorities", "issuer", "issued_at"})
public class UserPrincipal extends JwtAuthenticationToken {
    public UserPrincipal(Jwt jwt) {
        super(jwt);
    }

    public UserPrincipal(Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
        super(jwt, authorities);
    }

    public UserPrincipal(Jwt jwt, Collection<? extends GrantedAuthority> authorities, String name) {
        super(jwt, authorities, jwt.getSubject());
    }

    @JsonProperty("id")
    @Override
    public String getName() {
        return super.getName();
    }

    @JsonProperty("name")
    public String getUserName(){
        return getToken().getClaimAsString("name");
    }

    @JsonProperty("email")
    public String getEmail() {
        return getToken().getClaimAsString("email");
    }

    @JsonProperty("username")
    public String getUsername() {
        return getToken().getClaimAsString("preferred_username");
    }

    @JsonProperty("email_verified")
    public boolean isEmailVerified() {
        return getToken().getClaimAsBoolean("email_verified");
    }

    @JsonProperty("company_access")
    public Set<String> getCompanyAccess() {
        return new HashSet<>(getToken().getClaimAsStringList("company_access"));
    }

    @JsonProperty("issuer")
    public String getIssuer() {
        return getToken().getClaimAsString("iss");
    }

    @JsonProperty("issued_at")
    public LocalDateTime getIssuedAt() {
        return DateTimeUtils.toDateTime(getToken().getIssuedAt()+"");
    }
}
