package io.melody.core.auth.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import io.melody.core.auth.entity.UserDeviceEntitiy;

@Repository
public interface UserDeviceRepo extends MongoRepository<UserDeviceEntitiy, String> {

}


