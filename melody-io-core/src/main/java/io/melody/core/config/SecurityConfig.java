package io.melody.core.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.messaging.rsocket.annotation.support.RSocketRequesterMethodArgumentResolver;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternRouteMatcher;

import io.melody.core.auth.provider.AuthFilter;
import io.melody.core.auth.provider.AuthUserManager;
import io.melody.core.auth.provider.JwtTokenProvider;
import reactor.core.publisher.Mono;

@Configuration
@EnableRSocketSecurity
public class SecurityConfig {

	@Bean
	SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
			JwtTokenProvider tokenProvider,
			ReactiveAuthenticationManager reactiveAuthenticationManager) {
		return http.securityMatcher(new NegatedServerWebExchangeMatcher(
				ServerWebExchangeMatchers.pathMatchers("/assets/upload**",
						"/assets/**", "./assets/**", "./assets/images/**")))

				.csrf(ServerHttpSecurity.CsrfSpec::disable)
				.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
				.authenticationManager(reactiveAuthenticationManager)
				.securityContextRepository(
						NoOpServerSecurityContextRepository.getInstance())
				.authorizeExchange(it -> it.pathMatchers("/ws/**").permitAll()
						.pathMatchers("/socket.io/**").permitAll()
						.pathMatchers("/v2/api/utils/**").permitAll()
						.pathMatchers("/v2/api/user/channel").permitAll()
						.pathMatchers("/v2/api/user/flux1").permitAll()
						.pathMatchers("/v2/api/user/flux2").permitAll()
						.pathMatchers("/v2/api/user/login").permitAll()
						.pathMatchers("/v2/api/user/signup").permitAll()
						.pathMatchers("/v2/api/user/verifyaccount").permitAll()
						.pathMatchers("/v2/api/user/recoverpassword")
						.permitAll().pathMatchers("/**").authenticated())
				.addFilterAt(new AuthFilter(tokenProvider),
						SecurityWebFiltersOrder.HTTP_BASIC)
				.build();
	}

	@Bean
	CorsWebFilter corsWebFilter() {
		var corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowedOriginPatterns(Arrays.asList("*"));
		corsConfiguration.setAllowedMethods(
				Arrays.asList("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH"));
		corsConfiguration.setAllowCredentials(true);
		// the below three lines will add the relevant CORS response headers
		corsConfiguration.addAllowedOriginPattern("*");
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedMethod("*");
		var source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsWebFilter(source);
	}

	@Bean
	public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
		StrictHttpFirewall firewall = new StrictHttpFirewall();
		firewall.setAllowUrlEncodedSlash(true);
		firewall.setAllowSemicolon(true);
		return firewall;
	}

	@Bean(name = "PasswordEncoder")
	public PasswordEncoder customPasswordEncoder() {
		return new PasswordEncoder() {
			@Override
			public String encode(CharSequence plainTextPassword) {
				return plainTextPassword.toString();
			}

			@Override
			public boolean matches(CharSequence plainTextPassword,
					String passwordInDatabase) {
				return plainTextPassword.toString().equals(passwordInDatabase);
			}
		};
	}

	@Bean(name = "reactiveAuthenticationManager")
	public ReactiveAuthenticationManager reactiveAuthenticationManager(
			AuthUserManager authUserManager,
			@Qualifier("PasswordEncoder") PasswordEncoder passwordEncoder) {
		var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(
				authUserManager);
		authenticationManager.setPasswordEncoder(passwordEncoder);
		return authenticationManager;
	}

	@SuppressWarnings("unused")
	private Mono<AuthorizationDecision> currentUserMatchesPath(
			Mono<Authentication> authentication, AuthorizationContext context) {

		return authentication.map(
				a -> context.getVariables().get("user").equals(a.getName()))
				.map(t -> new AuthorizationDecision(t));

	}

	@Bean
	public PayloadSocketAcceptorInterceptor rsocketInterceptor(
			RSocketSecurity rSocketSecurity,
			@Qualifier("reactiveAuthenticationManager") ReactiveAuthenticationManager reactiveAuthenticationManager) {
		return rSocketSecurity
				.authorizePayload(authorize -> authorize.route("publish")
						.authenticated().anyExchange().permitAll())
				.jwt(jwtspec -> {
					jwtspec.authenticationManager(
							reactiveAuthenticationManager);
				}).build();
	}

	@Bean
	RSocketMessageHandler messageHandler(RSocketStrategies strategies) {
		RSocketMessageHandler mh = new RSocketMessageHandler();
		mh.getArgumentResolverConfigurer().addCustomResolver(
				new RSocketRequesterMethodArgumentResolver());
		mh.setRouteMatcher(new PathPatternRouteMatcher());
		mh.setRSocketStrategies(strategies);
		return mh;
	}

}
