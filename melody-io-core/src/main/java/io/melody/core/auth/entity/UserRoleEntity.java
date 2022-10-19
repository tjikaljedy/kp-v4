package io.melody.core.auth.entity;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import io.melody.core.enums.DbEnum;
import lombok.Data;


@Data
@Document(collection = "userrole")
public class UserRoleEntity implements Serializable {

	
	private static final long serialVersionUID = -593988042133126252L;
	
	@Id
	private Long _id;
	@Field( "userID")
	private String userID;
	@Field( "name")
	private String name;
	@Field( "is_sys_admin")
	private String isSysAdmin;
	
	@DBRef(db = DbEnum.CORE_DB)
	private List<UserRightEntity> rights;
	
	public UserRoleEntity() {
	}
}
