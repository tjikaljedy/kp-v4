package io.melody.core.auth.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document(collection = "userright")
public class UserRightEntity implements Serializable {

	
	private static final long serialVersionUID = -487772019627474276L;

	@Id
	private Long _id;
	@Field( "roleID")
	private String roleID;
	@Field( "rightName")
	private String rightName;
	
	public UserRightEntity() {
	}
}
