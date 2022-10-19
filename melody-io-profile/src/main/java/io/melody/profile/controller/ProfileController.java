package io.melody.profile.controller;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class ProfileController {

	@SuppressWarnings("unchecked")
	@MessageMapping("profile.verify")
    public Mono<org.json.simple.JSONObject> userLogin(@Payload org.json.simple.JSONObject dto) {
		System.out.println(">>>>>> DATA PROFILE: "+ dto);
		
		org.json.simple.JSONObject resp = new org.json.simple.JSONObject();
		resp.put("status", "success");
		resp.put("profile", "data me");
		
		return Mono.just(resp);
    }
	
	@MessageMapping("my-channel")
    public Flux<Long> channel(Flux<Notification> notifications) {
        final AtomicLong notificationCount = new AtomicLong(0);
        return notifications.doOnNext(notification -> {
            log.info("Received notification for channel: " + notification);
            notificationCount.incrementAndGet();
        })
                .switchMap(notification -> Flux.interval(Duration.ofSeconds(1)).map(new Object() {
                    private Function<Long, Long> numberOfMessages(AtomicLong notificationCount) {
                        long count = notificationCount.get();
                        log.info("Return flux with count: " + count);
                        return i -> count;
                    }
                }.numberOfMessages(notificationCount))).log();
    }
	
	
	@MessageMapping("number.stream")
    public Flux<Integer> responseStream(Integer number) {
        return Flux.range(1, number);
                //.delayElements(Duration.ofSeconds(1));
    }
	
	@MessageMapping("number.channel")
    public Flux<Long> biDirectionalStream(Flux<Long> numberFlux) {
        return numberFlux.flatMap(n->{
        	
        	return Flux.just(new Long(n * n));
        });
              
    }
}
