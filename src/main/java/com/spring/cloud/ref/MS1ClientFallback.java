package com.spring.cloud.ref;

import org.springframework.stereotype.Component;

@Component
public class MS1ClientFallback implements MS1Client {
	@Override
	public String getMessage() {
		return "fallback-byfeign-hystrix-ms2";
	}
}
