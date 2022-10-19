package io.melody.core.auth.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import io.melody.core.auth.entity.JwtRefreshTokenEntity;
import io.melody.core.auth.entity.OAuthConfigEntity;

@Repository
public interface RefreshTokenRepo
		extends
			MongoRepository<JwtRefreshTokenEntity, String> {

	@Query("{'refresh_token_id' : ?0}")
	List<JwtRefreshTokenEntity> findByRefeshToken(String refreshTokenId);

	@Query("{'email' : ?0}")
	List<JwtRefreshTokenEntity> findByUserEmail(String email);
}
