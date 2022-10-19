package io.melody.core;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.io.IOException;
import java.security.Key;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.nats.client.Connection;
import io.nats.client.JetStream;
import io.nats.client.Nats;
/*import io.nats.client.Connection;
import io.nats.client.JetStream;
import io.nats.client.Nats;
import io.nats.client.api.PublishAck;*/
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = TestApplication.class)
public class ClientTest02 {
	
	

	@Test
	void natsStreaming() throws IOException, InterruptedException  {
		Connection nc = Nats.connect("nats://localhost:4222");
		JetStream js = nc.jetStream();
	
		 
		/*AsyncSubscription subscription = js.subscribe(
		          topic, msg -> log.info("Received message on {}", msg.getSubject()));

		        if (subscription == null) {
		            log.error("Error subscribing to {}", topic);
		        } else {
		            subscriptions.put(topic, subscription);
		        }*/
	}
	
	@Test
	void generateSecretKey() {
		Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
		String jws = Jwts.builder().setSubject("K3l4spintar@123").signWith(key).compact();
		System.out.println(jws);
	}
	
	
}
