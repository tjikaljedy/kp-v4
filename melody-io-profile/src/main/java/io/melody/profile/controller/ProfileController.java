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

import io.rsocket.SocketAcceptor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class ProfileController {

    private  RSocketRequester.Builder rsocketRequesterBuilder;
    private  RSocketStrategies rsocketStrategies;
    private  RSocketRequester rsocketRequester;

    @Value("${core-config.core-rsocket.uri}")
    private String rsocketURI;

    @Autowired
    public ProfileController(RSocketRequester.Builder builder,
            @Qualifier("rSocketStrategies") RSocketStrategies strategies) {
        this.rsocketRequesterBuilder = builder;
        this.rsocketStrategies = strategies;

        System.out.println(">>>>>>>>  "+rsocketURI );
        SocketAcceptor responder = RSocketMessageHandler.responder(this.rsocketStrategies, new ClientHandler());
        this.rsocketRequester = rsocketRequesterBuilder
                .setupRoute("shell-client")
                .rsocketStrategies(this.rsocketStrategies)
                .setupData(IdentifierFactory.getInstance().generateIdentifier())
                .rsocketConnector(connector -> connector.acceptor(responder))
                .websocket(URI.create("wss://127.0.0.1:6565/ws"));

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
            System.out.println(">>>>>>>> TAI LUH");
           return Flux.empty();
        }
    }

}
