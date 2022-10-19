package io.melody.core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Controller;

import io.nats.client.Connection;
import io.nats.client.Connection.Status;
import io.nats.client.Dispatcher;
import io.nats.client.JetStream;
import io.nats.client.JetStreamApiException;
import io.nats.client.JetStreamManagement;
import io.nats.client.MessageHandler;
import io.nats.client.Nats;
import io.nats.client.Options;
import io.nats.client.PushSubscribeOptions;
import io.nats.client.api.StreamConfiguration;
import io.nats.client.api.StreamInfo;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
//@Slf4j
//@Controller
public class StreamController {
	/*private Connection nc;
	private Dispatcher dispatcher;
	private JetStream js;

	public StreamController() {

		try {
			Options options = new Options.Builder()
					.server("nats://127.0.0.1:4222")
					.connectionTimeout(Duration.ofSeconds(10))
					.connectionListener((conn, type) -> {
						if (conn.getStatus()  == Status.CONNECTED) {
							try {
								js = conn.jetStream();
								dispatcher = nc.createDispatcher();
								JetStreamManagement jsm = conn.jetStreamManagement();
								StreamConfiguration streamConfig = StreamConfiguration.builder()
						                .name("channel-stream")
						                .subjects("channel-subject")
						                .build();
						            StreamInfo streamInfo = jsm.addStream(streamConfig);
						            System.out.println(streamInfo);
						            
								doCreateListener();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						System.out.println(	conn.getStatus());
					}).build();
			nc = Nats.connect(options);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void doCreateListener() throws IOException, JetStreamApiException {
		PushSubscribeOptions so = PushSubscribeOptions.builder()
				.stream("channel-stream").build();

		MessageHandler handler = msg -> {
			System.out.println(msg);
			msg.ack();
		};
		
		if (js != null) {
			js.subscribe("channel-subject", dispatcher, handler, false, so);
		}
	}
*/
	/*
	 * implements ApplicationListener<ApplicationReadyEvent> {
	 * 
	 * @Autowired private RSocketRequester.Builder builder; private
	 * RSocketRequester client;
	 * 
	 * @Value("${core-config.cl-rsocket.enabled}") private boolean
	 * isEnabledRSocket;
	 * 
	 * @Value("${core-config.cl-rsocket.host}") private String rsocketHost;
	 * 
	 * @Value("${core-config.cl-rsocket.port}") private int rsocketPort;
	 * 
	 * @MessageMapping("initial") public Flux<String> initial(@Payload String
	 * message) { log.info(">>> INITIAL-RSOCKET <<<"); return Flux.empty(); }
	 * 
	 * @Override public void onApplicationEvent(ApplicationReadyEvent event) {
	 * try { client = builder.tcp(rsocketHost, rsocketPort);
	 * client.route("initial").data("initial").retrieveFlux(String.class).
	 * subscribe(); } catch (Exception e) { log.error(e.getMessage()); }
	 * 
	 * }
	 */

}
