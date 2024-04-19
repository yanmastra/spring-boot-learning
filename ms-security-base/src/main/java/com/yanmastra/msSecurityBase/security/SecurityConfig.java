package com.yanmastra.msSecurityBase.security;


import com.yanmastra.msSecurityBase.Log;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String GROUPS = "groups";
    private static final String REALM_ACCESS_CLAIM = "realm_access";
    private static final String ROLES_CLAIM = "roles";

    public SecurityConfig() {}


    @Autowired
    KeycloakAccessDeniedHandler keycloakAccessDeniedHandler;
    @Autowired
    KeycloakAuthenticationEntryPoint authEntryPoint;

    @Autowired
    KeycloakAuthenticationFailureHandler keycloakAuthenticationFailureHandler;

    @Autowired
    JwtAuthConverter jwtAuthConverter;

    @Autowired
    AuthorizeCustomizer authorizeCustomizer;
    @Autowired
    KeycloakLogoutHandler keycloakLogoutHandler;

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(sessionRegistry());
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(authorizeCustomizer)
                .addFilterBefore((servletRequest, servletResponse, filterChain) -> {
                    if (servletRequest instanceof HttpServletRequest httpServletRequest) {
                        Log.log.info(httpServletRequest.getMethod() + " --> " + httpServletRequest.getRequestURI()+", Auth:"+(StringUtils.isNotBlank(httpServletRequest.getHeader("Authorization"))));
                    }
                    filterChain.doFilter(servletRequest, servletResponse);
                }, BearerTokenAuthenticationFilter.class)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint)
                        .accessDeniedHandler(keycloakAccessDeniedHandler)
                )
                .formLogin(form -> form.failureHandler(keycloakAuthenticationFailureHandler))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(configure -> {
                    configure.jwtAuthenticationConverter(jwtAuthConverter);
                }).authenticationEntryPoint(authEntryPoint))
                .logout(logout -> logout.addLogoutHandler(keycloakLogoutHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
