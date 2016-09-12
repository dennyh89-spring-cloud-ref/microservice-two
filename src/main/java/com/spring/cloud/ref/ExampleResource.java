package com.spring.cloud.ref;

import java.security.Principal;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@RestController
@RequestMapping("/example2")
public class ExampleResource {
	private static final Logger LOG = LoggerFactory.getLogger(ExampleResource.class);

	@Autowired
	private MsOneBean msOneBean;

	@RequestMapping(method = RequestMethod.GET)
	public String getMessage(@Value("${x.message}") String message,
			@RequestHeader(value = "Authorization") String authorizationHeader, Principal currentUser) {

		LOG.info("getMessage Called with: User={}, Auth={}", currentUser.getName(), authorizationHeader);
		return message;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/composite")
	public String getCompositeMessages(@Value("${x.message}") String message2) {
		StringJoiner sj = new StringJoiner(":", "[", "]");
		String messageMsOne = msOneBean.getMessage();
		sj.add(messageMsOne);
		sj.add(message2);
		return sj.toString();
	}

	@Autowired
	private MS1Client ms1Client;

	@RequestMapping(method = RequestMethod.GET, path = "/composite-feign")
	public String getCompositeMessagesFeign(@Value("${x.message}") String message2) {

		StringJoiner sj = new StringJoiner(":", "[", "]");
		String messageMsOne = ms1Client.getMessage();
		sj.add(messageMsOne + " retrieved with Feign");
		sj.add(message2);
		return sj.toString();
	}

	@Component
	public class MsOneBean {

		@Autowired
		@LoadBalanced
		private OAuth2RestTemplate restTemplate;

		@HystrixCommand(fallbackMethod = "getFallBackMessage"  )
		//, commandProperties = @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE") )
		public String getMessage() {

			return this.restTemplate.getForObject("http://microservice-one/example", String.class);
		}

		public String getFallBackMessage() {
			return "FallbackMessage MS1 - generated by HystrixCommand in MS2";
		}
	}
}
