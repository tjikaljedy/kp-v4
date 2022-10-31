package io.melody.core.controller;

import java.util.List;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Controller;

import io.melody.core.activity.enums.EventEnrol;
import io.melody.core.auth.AuthSlice;
import io.melody.core.infra.provider.CoreProvider;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
@Slf4j
@Controller
public class AuthController {
	@Autowired
	private transient CoreProvider coreProvider;
	@Autowired
	private transient AuthSlice authSlice;
	@Autowired
	private transient RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;

	@MessageMapping("user.login")
	public Mono<org.json.simple.JSONObject> userLogin(
			org.json.simple.JSONObject payload, RSocketRequester requester) {

		/*
		 * org.json.simple.JSONObject validation =
		 * coreProvider.validationDto(EventEnrol.LOGIN.getName(), payload);
		 * if (!validation.isEmpty())
		 * return authSlice.unexpectedFail(validation);
		 * 
		 * return authSlice.consume(payload).flatMap(dto -> {
		 * return authSlice.authorizeLogin(dto);
		 * });
		 */

		//String processInstanceId =runtimeService.startProcessInstanceByKey("trip", Variables.putValue("name", "trip1")).getProcessInstanceId();;
		//taskService.complete("Activity-Cancel-flight-compensation");
		
		return Mono.empty();

	}

	@MessageMapping("user.signup")
	public Mono<org.json.simple.JSONObject> userSignup(
			org.json.simple.JSONObject payload, RSocketRequester requester) {

		org.json.simple.JSONObject validation = coreProvider.validationDto(EventEnrol.SIGN_UP.getName(), payload);
		if (!validation.isEmpty())
			return authSlice.unexpectedFail(validation);

		return authSlice.consume(payload).flatMap(dto -> {
			return authSlice.authorizeSignup(dto);
		});

	}

}
