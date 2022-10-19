package io.melody.core.auth.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;


@Data
@Document(collection = "accessright")
public class AccessRightEntity implements Serializable {

	private static final long serialVersionUID = 6954402164154570470L;
	@Id
	private Long _id;
	@Field("name")
	private String name;
	@Field("titleName")
	private String titleName;
	
	public AccessRightEntity() {
	}
}
