package io.melody.profile;

import java.net.URI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.test.context.ActiveProfiles;

@SpringBootApplication
@ActiveProfiles("test")
@EnableAutoConfiguration(exclude = { WebSocketServletAutoConfiguration.class,
		ReactiveUserDetailsServiceAutoConfiguration.class })
public class TestApplication {
	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}

	@Bean
    public RSocketRequester rSocketRequester(
            RSocketStrategies rSocketStrategies,
            RSocketProperties rSocketProps) {
        return RSocketRequester.builder()
                .rsocketStrategies(rSocketStrategies)
                .websocket(getURI(rSocketProps));
    }

	private URI getURI(RSocketProperties rSocketProps) {
        return URI.create("wss://127.0.0.1:6565/ws");
    }
   

}