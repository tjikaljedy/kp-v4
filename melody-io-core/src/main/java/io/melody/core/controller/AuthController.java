package io.melody.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Controller;

import io.melody.core.activity.enums.EventEnrol;
import io.melody.core.auth.AuthSlice;
import io.melody.core.infra.provider.CoreProvider;
import reactor.core.publisher.Mono;

@Controller
public class AuthController {
	@Autowired
	private transient CoreProvider coreProvider;
	@Autowired
	private transient AuthSlice authSlice;

	@MessageMapping("user.login")
	public Mono<org.json.simple.JSONObject> userLogin(
			org.json.simple.JSONObject payload, RSocketRequester requester) {

		org.json.simple.JSONObject validation = coreProvider.validationDto(EventEnrol.LOGIN.getName(), payload);
		if (!validation.isEmpty()) 
			return authSlice.unexpectedFail(validation);	
		
		return authSlice.consume(payload).flatMap(dto -> {
			
			return authSlice.authorizeLogin(dto);
		});
	
	}

}
