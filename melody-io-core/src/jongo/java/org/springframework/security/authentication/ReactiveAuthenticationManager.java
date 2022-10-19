package org.springframework.security.authentication;

import org.springframework.security.core.Authentication;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ReactiveAuthenticationManager {
	Mono<Authentication> authenticate(Authentication authentication);
}
