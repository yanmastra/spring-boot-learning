package com.yanmastra.msSecurityBase;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

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
				, type = SecuritySchemeType.OPENIDCONNECT
				, in = SecuritySchemeIn.COOKIE
		),
})
public class MsSecurityBaseApplication {
	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		SpringApplication.run(MsSecurityBaseApplication.class, args);
	}

}
