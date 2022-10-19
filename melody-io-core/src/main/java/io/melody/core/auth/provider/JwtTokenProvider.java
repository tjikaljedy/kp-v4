package io.melody.core.auth.provider;

import java.security.Key;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.melody.core.auth.entity.UserAuthEntity;
import io.melody.core.enums.AuthEnum;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {
	public static final String HEADER_PREFIX = "Bearer ";

	@Autowired
	private transient AuthUserManager authTokenProvider;

	@Value("${core-config.jwt.secret}")
	private String secret;

	@Value("${core-config.jwt.expiration-hours}")
	private long expirationTime;
	private Key secretKey;

	@PostConstruct
	public void init() {
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
	}

	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(this.secretKey).build()
				.parseClaimsJws(token).getBody();
	}

	public Date getExpirationDateFromToken(String token) {
		return getAllClaimsFromToken(token).getExpiration();
	}

	public Authentication validationToken(ServerHttpRequest request) {
		String bearerToken = this.validateBearerToken(
				request.getHeaders().getFirst(AuthEnum.TOKEN.getName()));
		final String inEmail = request.getHeaders()
				.getFirst(AuthEnum.USER.getName());

		if (StringUtils.isNotEmpty(bearerToken)
				&& bearerToken.startsWith(HEADER_PREFIX)) {
			Claims claims = this.getAllClaimsFromToken(bearerToken);
			final String claimEmail = (String) claims
					.get(AuthEnum.KEY_EMAIL.getName());
			boolean isValid = (StringUtils.isNotBlank(claimEmail)
					&& StringUtils.isNotBlank(inEmail))
					&& claimEmail.equals(inEmail);
			UserAuthEntity userAuth = authTokenProvider
					.obtainUserAuthByEmail(claimEmail);
			if (isValid && userAuth != null) {
				return new UsernamePasswordAuthenticationToken(
						userAuth.getEmail(), bearerToken,
						userAuth.getAuthorities());
			}
		}

		return null;
	}

	private String validateBearerToken(String bearerToken) {
		String retVal = null;
		if (bearerToken != null && bearerToken.startsWith(HEADER_PREFIX)) {
			final String jwtToken = bearerToken.substring(7);
			try {
				retVal = !isTokenExpired(jwtToken) ? jwtToken : null;
			} catch (Exception e) {
				log.warn("Unable to get JWT Token");
			}
		}

		return retVal;
	}

	@SuppressWarnings("unused")
	private Boolean validateToken(String token) {
		return !isTokenExpired(token);
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

}
