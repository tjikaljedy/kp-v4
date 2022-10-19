package io.melody.core.auth.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import io.melody.core.auth.entity.UserProfileEntity;

@Repository
public interface UserProfileRepo extends MongoRepository<UserProfileEntity, String> {

}


