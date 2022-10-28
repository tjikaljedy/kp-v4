package io.melody.core.auth.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import io.melody.core.auth.entity.UserProfileEntity;

@Repository
public interface UserProfileRepo extends MongoRepository<UserProfileEntity, String> {
    
    @Query("{'email' : ?0 }")
	List<UserProfileEntity> findByProfileEmail(String email);
}


