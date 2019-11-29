package com.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;


@Configuration
public class CORSFilter implements WebFluxConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowCredentials(true).allowedOrigins("*").allowedMethods("*").allowedHeaders("*")
				.exposedHeaders("Authorization","token")
				.maxAge(3600);
	}
}