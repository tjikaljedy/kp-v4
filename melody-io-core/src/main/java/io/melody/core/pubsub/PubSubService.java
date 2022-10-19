package io.melody.core.pubsub;

import io.melody.core.infra.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PubSubService {
    Mono<Void> publish(Message message);
    Flux<Message> subscribe();
}