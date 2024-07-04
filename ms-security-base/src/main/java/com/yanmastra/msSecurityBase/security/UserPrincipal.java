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
import java.util.List;
import java.util.Set;

import static com.yanmastra.msSecurityBase.security.SecurityConstant.*;

@Transient
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIncludeProperties({KEY_ID, KEY_NAME, KEY_USERNAME, KEY_EMAIL, KEY_EMAIL_VERIFICATION, KEY_COMPANY_ACCESS,
        KEY_AUTHORITIES, KEY_ISSUER, KEY_ISSUER_AT, KEY_SELECTED_COMPANY_ID})
public class UserPrincipal extends JwtAuthenticationToken {
    public UserPrincipal(Jwt jwt) {
        super(jwt);
    }

    public UserPrincipal(Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
        super(jwt, authorities);
    }

    public UserPrincipal(Jwt jwt, Collection<? extends GrantedAuthority> authorities, String companyId) {
        super(jwt, authorities, jwt.getSubject());
        this.companyId = companyId;
    }

    private String companyId;

    @JsonProperty(KEY_ID)
    @Override
    public String getName() {
        return super.getName();
    }

    @JsonProperty(KEY_NAME)
    public String getUserName(){
        return getToken().getClaimAsString(KEY_NAME);
    }

    @JsonProperty(KEY_EMAIL)
    public String getEmail() {
        return getToken().getClaimAsString(KEY_EMAIL);
    }

    @JsonProperty(KEY_USERNAME)
    public String getUsername() {
        return getToken().getClaimAsString(KEY_PREFERRED_USERNAME);
    }

    @JsonProperty(KEY_EMAIL_VERIFICATION)
    public boolean isEmailVerified() {
        return getToken().getClaimAsBoolean(KEY_EMAIL_VERIFICATION);
    }

    @JsonProperty(KEY_COMPANY_ACCESS)
    public Set<String> getCompanyAccess() {
        List<String> companyAccess = getToken().getClaimAsStringList(KEY_COMPANY_ACCESS);
        return companyAccess == null ? new HashSet<>() : new HashSet<>(companyAccess);
    }

    @JsonProperty(KEY_SELECTED_COMPANY_ID)
    public String getSelectedCompanyId() {
        return companyId;
    }

    @JsonProperty(KEY_ISSUER)
    public String getIssuer() {
        return getToken().getClaimAsString(KEY_ISS);
    }

    @JsonProperty(KEY_ISSUER_AT)
    public LocalDateTime getIssuedAt() {
        return DateTimeUtils.toDateTime(getToken().getIssuedAt()+"");
    }
}
