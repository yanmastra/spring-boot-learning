package com.yanmastra.pos.config;

import com.yanmastra.msSecurityBase.Log;
import com.yanmastra.msSecurityBase.security.AuthorizeCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

@Configuration
public class AppConfiguration {

    @Primary
    @Bean
    AuthorizeCustomizer createAuthCustomizer() {
        return new AuthorizeCustomizer() {
            @Override
            public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
                Log.log.info("on create customizer:"+auth.toString());
                auth.requestMatchers(HttpMethod.GET, "/favicon.ico").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/public").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/integration-test").hasRole("testuser")
                        .anyRequest().authenticated();
            }
        };
    }
}
