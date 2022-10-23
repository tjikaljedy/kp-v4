package io.melody.core.pubsub;

import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.XStream;

import lombok.extern.slf4j.Slf4j;
import swaydb.java.StorageUnits;
import swaydb.java.eventually.persistent.EventuallyPersistentMap;

@Slf4j
@Service
public class PubSubService {
    @Value("${core-config.swaydb.uri}")
    private String swayDbUri;
    private swaydb.java.Map<String, String, Void> sessionMap;
    private final XStream xstream = new XStream();

    @PostConstruct
    public void init() {
        try {
            this.inMemorySession();
        } catch (Exception e) {
            log.warn(e.getMessage());
        }

    }

    @ConnectMapping("shell-client")
    public void onClientConnect(RSocketRequester requester,
            @Payload String client) {

        requester.rsocket()
                .onClose()
                .doFirst(() -> {
                    // Add all new clients to a client list
                    log.info("Client: {} CONNECTED.", client);
                    this.getSessionMap().put(client, xstream.toXML(requester));
                    ;
                })
                .doOnError(error -> {
                    log.warn("Channel to client {} CLOSED", client);
                })
                .doFinally(consumer -> {
                    this.getSessionMap().remove(client);
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

    private void inMemorySession() {
        this.sessionMap = EventuallyPersistentMap
                .functionsOff(Paths.get(swayDbUri), swaydb.java.serializers.Default.stringSerializer(),
                        swaydb.java.serializers.Default.stringSerializer())
                .setMaxSegmentsToPush(5).setMaxMemoryLevelSize(StorageUnits.mb(50)).get();
    }

    private swaydb.java.Map<String, String, Void> getSessionMap() {
        return this.sessionMap;
    }

}