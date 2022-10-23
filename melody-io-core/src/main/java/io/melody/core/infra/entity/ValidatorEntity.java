package io.melody.core.infra.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class ValidatorEntity implements Serializable {
	private static final long serialVersionUID = 2573198913798066556L;
	public static final String QRY = "{ $and : [{'category':'SCHEMA' }] }";
	
	private String _id;
	private String event;
	private String jsonSchema;
	private String activityType;
	private String category;

}
