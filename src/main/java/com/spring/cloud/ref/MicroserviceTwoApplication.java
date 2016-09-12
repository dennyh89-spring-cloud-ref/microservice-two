package com.spring.cloud.ref;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@SpringBootApplication
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableResourceServer
@EnableOAuth2Client
@EnableFeignClients
public class MicroserviceTwoApplication {
	private static final Logger LOG = LoggerFactory.getLogger(MicroserviceTwoApplication.class);
	public static void main(String[] args) {

		SpringApplication.run(MicroserviceTwoApplication.class, args);

	}

	// Defining Beans
	@LoadBalanced
	@Bean
	public OAuth2RestTemplate loadBalancedOauth2RestTemplate(OAuth2ProtectedResourceDetails resource,
			OAuth2ClientContext oauth2Context) {
		return new OAuth2RestTemplate(resource, oauth2Context);
	}

	

	@Configuration
	public class Ms1ClientConfiguration {
		@Bean
		feign.Logger.Level feignLoggerLevel() {
			return feign.Logger.Level.HEADERS;
		}
		
		@Bean
		public RequestInterceptor oAuth2RequestInterceptor(OAuth2ProtectedResourceDetails resource,
				OAuth2ClientContext oauth2Context) {
			return new OAuth2FeignRequestInterceptor(oauth2Context, resource);
		}
	}

}
