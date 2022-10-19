package io.melody.core.auth.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import io.melody.core.auth.entity.UserAuthEntity;

@Repository
public interface UserAuthRepo extends MongoRepository<UserAuthEntity, String> {

	@Query("{'email' : ?0}")
	List<UserAuthEntity> findByEmail(String email);
	
	@Query("{'mobile' : ?0}")
	List<UserAuthEntity> findByMobile(String mobile);
}
