package com.yanmastra.integrationTest;

import com.yanmastra.msSecurityBase.MsSecurityBaseApplication;
import com.yanmastra.msSecurityBase.utils.KeyValueCacheUtils;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecuritySchemes({
		@SecurityScheme(
				name = "Keycloak"
				, openIdConnectUrl = "${spring.security.oauth2.resourceserver.jwt.issuer-uri}/.well-known/openid-configuration"
				, scheme = "bearer"
				, type = SecuritySchemeType.OPENIDCONNECT
				, in = SecuritySchemeIn.HEADER
		),
		@SecurityScheme(
				name = "Keycloak2"
				, openIdConnectUrl = "${spring.security.oauth2.resourceserver.jwt.issuer-uri}/.well-known/openid-configuration"
				, scheme = "bearer"
				, type = SecuritySchemeType.OPENIDCONNECT
				, in = SecuritySchemeIn.COOKIE
		),
})
public class IntegrationTestApplication {
	public static void main(String[] args) {
		Class[] classes = new Class[]{
				MsSecurityBaseApplication.class,
				IntegrationTestApplication.class
		};
		SpringApplication.run(classes, args);
	}

	@Value("${cache_directory}")
	public void setCacheDir(String cache_directory) {
		KeyValueCacheUtils.cache_directory = cache_directory;
	}
}
