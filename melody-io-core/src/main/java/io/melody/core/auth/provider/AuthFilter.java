package io.melody.core.auth.provider;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AuthFilter implements WebFilter {

	public static final String HEADER_PREFIX = "Bearer ";

	private final JwtTokenProvider tokenProvider;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

		Authentication authentication = this.tokenProvider
				.validationToken(exchange.getRequest());
		if (authentication != null) {
			return chain.filter(exchange)
					.contextWrite(ReactiveSecurityContextHolder
							.withAuthentication(authentication));
		}
		return chain.filter(exchange);
	}

}
