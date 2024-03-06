package com.yanmastra.integrationTest;

import com.yanmastra.microservices.utils.KeyValueCacheUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IntegrationTestApplication {
	public static void main(String[] args) {
		SpringApplication.run(IntegrationTestApplication.class, args);
	}

	@Value("${cache_directory}")
	public void setCacheDir(String cache_directory) {
		KeyValueCacheUtils.cache_directory = cache_directory;
	}
}
