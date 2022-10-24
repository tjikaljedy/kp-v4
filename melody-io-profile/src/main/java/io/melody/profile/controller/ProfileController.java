package io.melody.profile.controller;

import java.net.URI;

import org.axonframework.common.IdentifierFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import io.rsocket.SocketAcceptor;
import io.rsocket.metadata.WellKnownMimeType;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class ProfileController {
    // @Autowired
    private RSocketStrategies rSocketStrategies;
    private RSocketRequester.Builder rsocketRequesterBuilder;
    private RSocketRequester rsocketRequester;

    @Value("${core-config.core-rsocket.uri}")
    private String rsocketURI;

    @Autowired
    @SuppressWarnings("deprecation")
    public ProfileController(RSocketRequester.Builder builder,
            @Qualifier("rSocketStrategies") RSocketStrategies strategies) {
        MimeType metadataMimeType = MimeTypeUtils.parseMimeType(
                WellKnownMimeType.MESSAGE_RSOCKET_COMPOSITE_METADATA.getString());
        this.rsocketRequesterBuilder = builder;
        this.rSocketStrategies = strategies;
        SocketAcceptor responder = RSocketMessageHandler.responder(this.rSocketStrategies, new ClientHandler());
        this.rsocketRequester = rsocketRequesterBuilder
                .setupRoute("collab-client")
                .setupData(IdentifierFactory.getInstance().generateIdentifier())
                .rsocketStrategies(this.rSocketStrategies)
                .rsocketConnector(connector -> connector.acceptor(responder))
                .metadataMimeType(metadataMimeType)
                .connectWebSocket(URI.create("ws://localhost:6565/ws")).block();

        this.rsocketRequester.rsocket()
                .onClose()
                .doOnError(error -> log.warn("Connection CLOSED"))
                .doFinally(consumer -> log.info("Client DISCONNECTED"))
                .subscribe();

    }

    @SuppressWarnings("unchecked")
    @MessageMapping("profile.verify")
    public Mono<org.json.simple.JSONObject> userLogin(@Payload org.json.simple.JSONObject dto) {
        System.out.println(">>>>>> DATA PROFILE: " + dto);

        org.json.simple.JSONObject resp = new org.json.simple.JSONObject();
        resp.put("status", "success");
        resp.put("profile", "data me");

        return Mono.just(resp);
    }

    public class ClientHandler {
        @MessageMapping("client-status")
        public Flux<String> statusUpdate(String status) {
            System.out.println(">>>>>>>> "+ status);
            return Flux.empty();
        }
    }

}
