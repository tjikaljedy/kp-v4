package io.melody.core.activity.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import io.melody.core.activity.entity.ActivityEventEntity;

@Repository
public interface ActivityRepo  extends MongoRepository<ActivityEventEntity, String> {
    @Query("{ 'activityEventID': ?0, 'active': true}")
	List<ActivityEventEntity> findByActivityId(String activityId);

	@Query("{ 'status' : ?0 , 'event': ?1, 'active': true}")
	List<ActivityEventEntity> findByStatusAndEvent(String status, String event);

	@Query("{ 'activityType' : ?0,'active': true }")
	List<ActivityEventEntity> findByActivityType(String type);

	// , {'flowIsDone': false}
	@Query("{ $and: [{'payload.userID': ?0}, {'activityType': ?1}, {'parentActivityEventID' : {$eq:null} } ]}")
	List<ActivityEventEntity> findParentByUserId(String userID, String activityType);

	@Query("{ $and: [{'payload.email': ?0}, {'activityType': ?1}, {'parentActivityEventID' : {$eq:null} } ]}")
	List<ActivityEventEntity> findParentByEmail(String email, String activityType);

	@Query("{ $and: [{'payload.email': ?0}, {'activityType': ?1}, {'parentActivityEventID' : {$eq:null} },{'flowIsDone': ?2} ]}")
	List<ActivityEventEntity> findParentByEmail(String email, String activityType,
			boolean flowIsDone);

	@Query("{ $and: [{'payload.email': ?0}, {'payload.login_device': ?1}, {'activityType': ?2}, {'parentActivityEventID' : {$eq:null} }, {'flowIsDone': ?3} ]}")
	List<ActivityEventEntity> findParentByDevice(String email, String deviceId,
			String activityType, boolean flowIsDone);
	
	@Query("{ $and: [{'payload.email': ?0}, {'payload.login_device': ?1}, {'activityType': ?2}, {'parentActivityEventID' : {$eq:null} } ]}")
	List<ActivityEventEntity> findParentByDevice(String email, String deviceId,
			String activityType);

	@Query("{ $and: [{'payload.email': ?0}, {'activityType': ?1}]}")
	List<ActivityEventEntity> findActivityEvent(String email, String activityType);
}
