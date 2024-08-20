package com.yanmastra.msSecurityBase.security;


import com.yanmastra.msSecurityBase.Log;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
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

import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public SecurityConfig(KeycloakAccessDeniedHandler keycloakAccessDeniedHandler, KeycloakAuthenticationEntryPoint authEntryPoint, KeycloakAuthenticationFailureHandler keycloakAuthenticationFailureHandler, JwtAuthConverter jwtAuthConverter, AuthorizeCustomizer authorizeCustomizer, KeycloakLogoutHandler keycloakLogoutHandler) {
        this.keycloakAccessDeniedHandler = keycloakAccessDeniedHandler;
        this.authEntryPoint = authEntryPoint;
        this.keycloakAuthenticationFailureHandler = keycloakAuthenticationFailureHandler;
        this.jwtAuthConverter = jwtAuthConverter;
        this.authorizeCustomizer = authorizeCustomizer;
        this.keycloakLogoutHandler = keycloakLogoutHandler;
    }

    private final KeycloakAccessDeniedHandler keycloakAccessDeniedHandler;
    private final KeycloakAuthenticationEntryPoint authEntryPoint;
    private final KeycloakAuthenticationFailureHandler keycloakAuthenticationFailureHandler;
    private final JwtAuthConverter jwtAuthConverter;
    private final AuthorizeCustomizer authorizeCustomizer;
    private final KeycloakLogoutHandler keycloakLogoutHandler;

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
                        Log.log.info(httpServletRequest.getMethod() + "\t --> " + httpServletRequest.getRequestURI()+", Auth:"+(StringUtils.isNotBlank(httpServletRequest.getHeader("Authorization"))));

                        Cookie[] cookies = httpServletRequest.getCookies();
                        if (cookies != null) {
                            Log.log.debug("\t --> " + String.join(",", Stream.of(cookies).map(c -> "Cookie: " + c.getName() + "=" + c.getValue() + "; path=" + c.getPath() + "; exp=" + c.getMaxAge())
                                    .toList()));
                        }
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
