package io.melody.core.activity;

import java.io.Serializable;

import org.axonframework.common.IdentifierFactory;

import io.melody.core.activity.entity.ActivityEventEntity;
import lombok.Data;

@Data
public class ActivityDto implements Serializable {

	private static final long serialVersionUID = 7903431496127890925L;

	private String dtoID;
	private String activityType;
	private boolean validActivity;
	private ActivityEventEntity parent;
	private ActivityEventEntity last;
	private ActivityEventEntity next;
	private ActivityEventEntity suggest;
	
	public ActivityDto() {
		this.dtoID = IdentifierFactory.getInstance().generateIdentifier();
	}
	
	public ActivityDto(String activityType) {
		this.dtoID = IdentifierFactory.getInstance().generateIdentifier();
		this.activityType = activityType;
	}
}
