package io.melody.core.auth.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.joda.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import io.melody.core.enums.DbEnum;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Document(collection = "jwtrefreshtoken")
public class JwtRefreshTokenEntity implements Serializable {

	private static final long serialVersionUID = 6651582597095901547L;

	@Id
	private String _id;

	@Field("refresh_token_id")
	private String refeshTokenID;

	@Field("email")
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private String email;

	@Field("iat")
	private long iat;

	@Field("exp")
	private long exp;

	@DBRef(db = DbEnum.CORE_DB)
	@Setter(AccessLevel.NONE)
	private UserAuthEntity userAuth;

	public void setUserAuth(UserAuthEntity userAuth) {
		this.userAuth = userAuth;
		this.email = (userAuth != null) ? userAuth.getEmail() : null;
	}

	public void setExpiration(long days) {
		Long expirationTimeLong = TimeUnit.DAYS.toSeconds(days);

		final Date createdDate = new Date();
		final Date expirationDate = new Date(
				createdDate.getTime() + expirationTimeLong * 1000);

		this.iat = createdDate.getTime();
		this.exp = expirationDate.getTime();
	}

	public boolean getExpiration() {
		return Instant.ofEpochMilli(this.exp).isBeforeNow();
	}
}
