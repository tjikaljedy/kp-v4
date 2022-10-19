package io.melody.core.auth.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import io.melody.core.auth.entity.OAuthConfigEntity;

@Repository
public interface OAuthConfigRepo extends MongoRepository<OAuthConfigEntity, String> {
	
	@Query("{ 'applicationName' : ?0 }")
	public List<OAuthConfigEntity> findClienByApplicationName(String name);
}


