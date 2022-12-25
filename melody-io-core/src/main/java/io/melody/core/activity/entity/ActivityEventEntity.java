package io.melody.core.activity.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.axonframework.common.IdentifierFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.github.pnavais.machine.model.StateTransition;

import io.melody.core.auth.AuthDto;
import io.melody.core.enums.DbEnum;
import io.melody.core.utils.AppUtils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
@Document(collection = "activityevent")
public class ActivityEventEntity implements Serializable {
	private static final long serialVersionUID = 6445083404707919582L;
	@Id
	@Field("_id")
	private Long id;
	@Field("activityEventID")
	private String activityEventID;
	@Field("activityType")
	private String activityType;
	@Field("active")
	private boolean active;
	@Field("status")
	private String status;
	@Field("target")
	private String target;
	@Field("event")
	private String event;
	@Field("payloadType")
	private String payloadType;
	@Field("payload")
	@Getter(AccessLevel.NONE)
	private org.json.simple.JSONObject payload;
	@Field("createdAt")
	private String createdAt;
	@Field("updatedAt")
	private String updatedAt;
	@Field("parentActivityEventID")
	private String parentActivityEventID;
	@Field("sequenceNumber")
	private int sequenceNumber;
	@Field("flowIsDone")
	private boolean flowIsDone;

	// New
	@Field("fork")
	private String fork;
	@Field("action")
	private String action;

	@DBRef(db = DbEnum.CORE_DB)
	@Getter(AccessLevel.NONE)
	private List<ActivityEventEntity> transitions;
	
	public ActivityEventEntity() {
		this.activityEventID = IdentifierFactory.getInstance().generateIdentifier();
		this.createdAt = AppUtils.dateToDateStringFormat(new Date(), AppUtils.DEFAULT_DATE_PATTERN, AppUtils.ZONE_GMT_PLUS7);
		this.updatedAt = AppUtils.dateToDateStringFormat(new Date(), AppUtils.DEFAULT_DATE_PATTERN, AppUtils.ZONE_GMT_PLUS7);
	}

	public ActivityEventEntity(String type) {
		this.activityEventID = IdentifierFactory.getInstance().generateIdentifier();
		this.activityType = type;
		this.active = true;
		this.flowIsDone = false;
		this.createdAt = AppUtils.dateToDateStringFormat(new Date(), AppUtils.DEFAULT_DATE_PATTERN, AppUtils.ZONE_GMT_PLUS7);
		this.updatedAt = AppUtils.dateToDateStringFormat(new Date(), AppUtils.DEFAULT_DATE_PATTERN, AppUtils.ZONE_GMT_PLUS7);

	}

	public ActivityEventEntity(ActivityEventEntity parent, AuthDto dto) {
		final int lastCount = parent.getTransitions().size();
		this.activityEventID = IdentifierFactory.getInstance().generateIdentifier();
		this.activityType = parent.getActivityType();
		this.sequenceNumber = lastCount + 1;
		this.payload = parent.getPayload();
		this.parentActivityEventID = parent.getActivityEventID();
		this.active = true;
		this.flowIsDone = false;
		this.createdAt = AppUtils.dateToDateStringFormat(new Date(), AppUtils.DEFAULT_DATE_PATTERN, AppUtils.ZONE_GMT_PLUS7);
		this.updatedAt = AppUtils.dateToDateStringFormat(new Date(), AppUtils.DEFAULT_DATE_PATTERN, AppUtils.ZONE_GMT_PLUS7);
		this.parentActivityEventID = parent.getActivityEventID();
		
		//TODO
		//this.event = dto.getEvent();
		//this.status = dto.getStatus();
	}

	@SuppressWarnings("unchecked")
	public ActivityEventEntity(StateTransition start, String userID, String type) {
		org.json.simple.JSONObject payload = new org.json.simple.JSONObject();
		payload.put("userID", userID);

		this.activityEventID = IdentifierFactory.getInstance().generateIdentifier();
		this.activityType = type;
		this.active = true;
		this.flowIsDone = false;
		this.createdAt = AppUtils.dateToDateStringFormat(new Date(), AppUtils.DEFAULT_DATE_PATTERN, AppUtils.ZONE_GMT_PLUS7);
		this.updatedAt = AppUtils.dateToDateStringFormat(new Date(), AppUtils.DEFAULT_DATE_PATTERN, AppUtils.ZONE_GMT_PLUS7);

		this.sequenceNumber = 0;
		this.event = start.getMessage().toString();
		this.status = start.getOrigin().getName();
		this.payload = payload;
	}

	@SuppressWarnings("unchecked")
	public ActivityEventEntity(String userID, String type) {
		org.json.simple.JSONObject payload = new org.json.simple.JSONObject();
		payload.put("userID", userID);

		this.activityEventID = IdentifierFactory.getInstance().generateIdentifier();
		this.activityType = type;
		this.active = true;
		this.flowIsDone = false;
		this.createdAt = AppUtils.dateToDateStringFormat(new Date(), AppUtils.DEFAULT_DATE_PATTERN, AppUtils.ZONE_GMT_PLUS7);
		this.updatedAt = AppUtils.dateToDateStringFormat(new Date(), AppUtils.DEFAULT_DATE_PATTERN, AppUtils.ZONE_GMT_PLUS7);

		this.sequenceNumber = 0;
		this.event = type;
		this.status = type;
		this.payload = payload;
	}

	public List<ActivityEventEntity> getTransitions() {
		if (transitions == null && CollectionUtils.isEmpty(transitions)) {
			transitions = new ArrayList<ActivityEventEntity>();

		} else {
			transitions.sort(java.util.Comparator
					.comparingInt(ActivityEventEntity::getSequenceNumber).reversed());
		}
		return transitions;
	}

	public org.json.simple.JSONObject getPayload() {
		if (this.payload == null) {
			this.payload = new org.json.simple.JSONObject();
		}
		return this.payload;
	}

	@SuppressWarnings("unchecked")
	public void createPayload(AuthDto inDto) {
		if (this.payload == null) {
			this.payload = new org.json.simple.JSONObject();
		}

		this.payload.put("email", inDto.getEmail());
	}

	@Override
	public int hashCode() {
		return 31 * status.hashCode() + event.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof ActivityEventEntity)) {
			return false;
		}
		ActivityEventEntity that = (ActivityEventEntity) o;

		return status.equals(that.status) && event.equals(that.event);
	}

	@Override
	public String toString() {
		return this.status + "-" + this.event;
	}
	
}
