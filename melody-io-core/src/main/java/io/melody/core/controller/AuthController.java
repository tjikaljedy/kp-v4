package io.melody.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;

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

	@SuppressWarnings("unchecked")
	@MessageMapping("user.login")
	public Mono<org.json.simple.JSONObject> userLogin(
			org.json.simple.JSONObject payload, RSocketRequester requester) {

		//@formatter:off
		/*org.json.simple.JSONObject validation = coreProvider.validationDto(EventEnrol.LOGIN.getName(), payload);
		if (!validation.isEmpty()) 
			return authSlice.unexpectedFail(validation);	
		
		return authSlice.consume(payload).flatMap(dto -> {
			return authSlice.authorizeLogin(dto);
		});*/
		//@formatter:on

		// requester.rsocket().onClose().doFirst(() -> {
		// log.info("Client: {} CONNECTED.");

		// }).doOnError(error -> {

		// }).doFinally(consumer -> {

		// }).subscribe();
		// requester.route("client-status").data("OPEN").send();

		// requester.route("client-status").data("OPEN").retrieveFlux(String.class)
		// .doOnNext(s -> log.info("Client: {} ", s)).subscribe();

		/*
		 * requester.rsocket().onClose().doFirst(() -> {
		 * log.info(">>> OnClose");
		 * }).doOnError(error -> {
		 * 
		 * }).doFinally(consumer -> {
		 * requester.dispose();
		 * }).subscribe();
		 * 
		 * requester.route("client-status").data("OPEN").retrieveMono(String.class)
		 * .subscribe();
		 */

		requester.route("client-status").data("OPEN").send();
		System.out.println("SERVER >>>>>> user.login" + payload.toJSONString());

		org.json.simple.JSONObject rtest = new org.json.simple.JSONObject();
		rtest.put("nama", "dariserver");
		rtest.put("balikan", "user.login");

		return Mono.just(rtest);
	}

}
