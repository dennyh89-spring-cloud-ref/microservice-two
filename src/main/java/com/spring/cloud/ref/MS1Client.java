package com.spring.cloud.ref;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.cloud.ref.MicroserviceTwoApplication.Ms1ClientConfiguration;

@FeignClient(path = "/example", name = "microservice-one", fallback = MS1ClientFallback.class)
public interface MS1Client {
	@RequestMapping(method = RequestMethod.GET)
	String getMessage();
}