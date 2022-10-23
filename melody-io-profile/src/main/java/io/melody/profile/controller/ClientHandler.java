package io.melody.profile.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import reactor.core.publisher.Flux;

@Controller
public class ClientHandler {
    @MessageMapping("client-status")
    public Flux<String> statusUpdate(String status) {
       return Flux.empty();
    }
}
