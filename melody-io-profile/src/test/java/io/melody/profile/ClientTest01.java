package io.melody.profile;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.web.WebAppConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = TestApplication.class)
@WebAppConfiguration
class ClientTest01 {
	@Autowired 
	private RSocketRequester rSocketRequester;
	
	@SuppressWarnings("unchecked")
	@Test
	void testUser3() throws InterruptedException {
		
		org.json.simple.JSONObject data = new org.json.simple.JSONObject();
		data.put("x", "baruddd juganih");
		rSocketRequester.route("user.login").data(data).retrieveFlux(org.json.simple.JSONObject.class).blockLast();
		
		
	}
	
	/*@SuppressWarnings("unchecked")
	
	@Test
	void testUser2() {
		try {
			RSocket rsocketClient = RSocketConnector.create()
					.metadataMimeType(WellKnownMimeType.MESSAGE_RSOCKET_COMPOSITE_METADATA.getString())
					.connect(TcpClientTransport.create("127.0.0.1", 6565)).block();
			org.json.simple.JSONObject ob = new org.json.simple.JSONObject();
			ob.put("email", "test");

			rsocketClient.requestStream(Mono.just(DefaultPayload.create(ob)));
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	@Test
	void testUser() {
		try {
			RSocket rsocketClient = RSocketConnector.create()
					.metadataMimeType(WellKnownMimeType.MESSAGE_RSOCKET_COMPOSITE_METADATA.getString())
					.connect(TcpClientTransport.create("127.0.0.1", 6565)).block();
			org.json.simple.JSONObject ob = new org.json.simple.JSONObject();
			ob.put("email", "test");

			ByteBuf payloadData = ByteBufAllocator.DEFAULT.buffer().writeBytes(ob.toJSONString().getBytes());
			RoutingMetadata routingMetadata = TaggingMetadataCodec.createRoutingMetadata(ByteBufAllocator.DEFAULT,
					List.of("user.login"));

			CompositeByteBuf compositeMetadataBuffer = ByteBufAllocator.DEFAULT.compositeBuffer();
			CompositeMetadataCodec.encodeAndAddMetadata(compositeMetadataBuffer, ByteBufAllocator.DEFAULT,
					WellKnownMimeType.MESSAGE_RSOCKET_ROUTING, routingMetadata.getContent());

			rsocketClient.requestStream(DefaultPayload.create(payloadData, compositeMetadataBuffer))
					.map(Payload::getData).toIterable().forEach(System.out::println);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}*/

}
