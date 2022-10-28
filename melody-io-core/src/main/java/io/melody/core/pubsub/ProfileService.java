package io.melody.core.pubsub;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import io.melody.core.auth.AuthDto;
import reactor.core.publisher.Mono;

@Service
public class ProfileService {
	// @Autowired
	// private RSocketStrategies rSocketStrategies;

	// private transient RSocketRequester rSocketRequester;

	@PostConstruct
	public void init() {
		// rSocketRequester = RSocketRequester.builder()
		// .rsocketStrategies(rSocketStrategies)
		// .websocket(URI.create("wss://127.0.0.1:7565/ws"));
	}

	public Mono<AuthDto> verityProfile(AuthDto inDto) {
		// org.json.simple.JSONObject data = new org.json.simple.JSONObject();
		// data.put("profile", "check me please");
		// return rSocketRequester.route("profile.verify").data(data)
		// .retrieveMono(org.json.simple.JSONObject.class).flatMap(out->{
		// System.out.println(out);
		// return Mono.justOrEmpty(inDto);

		// });
		// .retrieveFlux(org.json.simple.JSONObject.class).flatMap(out -> {
		// return Flux.just(inDto);
		// });

	
		return Mono.just(inDto);
	}
}
