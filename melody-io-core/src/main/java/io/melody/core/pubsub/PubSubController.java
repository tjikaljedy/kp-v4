package io.melody.core.pubsub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

import io.melody.core.infra.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
//@Controller
public class PubSubController {

   /* 
    String usernameClaim;

    private final PubSubService messagingService;

    public PubSubController(PubSubService messagingService) {
        this.messagingService = messagingService;
    }

    @ConnectMapping
    void onConnect(RSocketRequester requester) {
        Objects.requireNonNull(requester.rsocket(), "rsocket  should not be null")
                .onClose()
                .doOnError(error -> log.warn(requester.rsocketClient() + " Closed"))
                .doFinally(consumer -> log.info(requester.rsocketClient() + " Disconnected"))
                .subscribe();
    }
    
	
	@MessageMapping("user.login")
    public Flux<org.json.simple.JSONObject> userLogin(@Payload org.json.simple.JSONObject dto) {
		System.out.println(">>>>>> DATA: "+ dto);
		//System.out.println(">[users.{email}.get] " +dto);
        //return reactorQueryGateway.subscriptionQueryMany(new FindUserAuth(email), UserAuthDto.class);
		org.json.simple.JSONObject ddd = new org.json.simple.JSONObject();
		return Flux.just(ddd);
    }*/

    //@MessageMapping("publish")
    //Mono<Void> publish(String message, @AuthenticationPrincipal Mono<Jwt> token) {
    //    return token.map(jwt -> jwt.getClaimAsString(usernameClaim))
    //            .flatMap(username -> messagingService.publish(new Message(username, message)));
    //}

    //@MessageMapping("subscribe")
    //Flux<Message> subscribe() {
    //    return messagingService.subscribe();
   // }
    
	//@formatter:off
	/*
	@MessageMapping("user.get")
    public Flux<UserAuthDto> user_subscribe() {
		System.out.println(">[users.{email}.get] ");
		return Flux.empty();
        //return reactorQueryGateway.subscriptionQueryMany(new FindUserAuth(email), UserAuthDto.class);
    }*/
	
	/*@Autowired
	private JwtProfileProvider jwtProvider;
	
	private final ReactorQueryGateway reactorQueryGateway;
	private final ReactorCommandGateway reactorCommandGateway;
	public AuthController(ReactorQueryGateway reactorQueryGateway,ReactorCommandGateway reactorCommandGateway) {
        this.reactorQueryGateway = reactorQueryGateway;
        this.reactorCommandGateway = reactorCommandGateway;
    }
	
	@PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<org.json.simple.JSONObject>> userLogin(@RequestBody org.json.simple.JSONObject message) {
		//@formatter:off
		org.json.simple.JSONObject validation = coreService.validationDto(ActivityId.LOGIN, message);
		if (!validation.isEmpty()) {
			return Mono.just(ProfileWrapper.unexpectedFail(validation));
		}  else {
			return ProfileWrapper.consume(message)
					.flatMap(dto -> {
						if (StringUtils.isEmpty(dto.getStatus()) & StringUtils.isEmpty(dto.getEvent())) {
							dto.setStatus(StatusLogin.INITIAL.getName());
							dto.setEvent(EventLogin.LOGIN.getName());
						}
						
						return jwtProvider.authorizeLogin(dto);
					});
		}
		//@formatter:on

	}
	
	@MessageMapping("user.{email}.get")
    public Flux<UserAuthDto> user_subscribe(@DestinationVariable String email) {
		System.out.println(">[users.{email}.get] " +email);
        return reactorQueryGateway.subscriptionQueryMany(new FindUserAuth(email), UserAuthDto.class);
    }
	*/
	//@formatter:on
}