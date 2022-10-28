package io.melody.core.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class PubSubController {
    // @Value("${core-config.swaydb.uri}")
    // private String swayDbUri;
    // private swaydb.java.Map<String, Byte[], Void> sessionMap;
    private Map<String, io.rsocket.RSocket> sessionMap;

    @PostConstruct
    public void init() {
        try {
            sessionMap = new HashMap<String, io.rsocket.RSocket>();
            // this.inMemorySession();
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    @ConnectMapping("collab-client")
    public void onClientConnect(RSocketRequester requester,
            @Payload String client) {

        requester.rsocket()
                .onClose()
                .doFirst(() -> {
                    log.info("Client: {} CONNECTED.", client);
                    sessionMap.put(client, requester.rsocket());
                })
                .doOnError(error -> {
                    log.warn("Channel to client {} CLOSED", client);
                })
                .doFinally(consumer -> {
                    // this.getSessionMap().remove(client);
                    sessionMap.remove(client);
                    log.info("Client {} DISCONNECTED", client);
                })
                .subscribe();
    
        // Callback to client, confirming connection
        requester.route("client-status")
                .data("OPEN")
                .retrieveFlux(String.class)
                .doOnNext(s -> log.info("Client: {} Free Memory: {}.", client, s))
                .subscribe();
    }

    // private void inMemorySession() {
    // this.sessionMap = EventuallyPersistentMap
    // .functionsOff(Paths.get(swayDbUri),
    // swaydb.java.serializers.Default.stringSerializer(),
    // swaydb.java.serializers.Default.javaByteArraySerializer())
    // .setMaxSegmentsToPush(5).setMaxMemoryLevelSize(StorageUnits.mb(50)).get();
    // }

    // private swaydb.java.Map<String, Byte[], Void> getSessionMap() {
    // return this.sessionMap;
    // }

}