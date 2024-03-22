package com.yanmastra.msSecurityBase;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecurityScheme(
		name = "Keycloak"
		, openIdConnectUrl = "https://keycloak.mahotama.com/realms/si-akademik/.well-known/openid-configuration"
		, scheme = "bearer"
		, type = SecuritySchemeType.OPENIDCONNECT
		, in = SecuritySchemeIn.HEADER
)
public class MsSecurityBaseApplication {
	public static void main(String[] args) {
		SpringApplication.run(MsSecurityBaseApplication.class, args);
	}

}
