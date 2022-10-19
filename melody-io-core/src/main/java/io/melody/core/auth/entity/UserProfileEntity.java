package io.melody.core.auth.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;


@Data
@Document(collection = "userprofile")
public class UserProfileEntity {
	@Id
	private Long _id;
	@Field( "email")
	private String email;

	@Field( "fullName")
	private String fullName;
	@Field( "dob")
	private String dob;
	@Field( "gender")
	private String gender;
	
	@Field( "active")
	private boolean active;
	@Field( "createdAt")
	private String createdAt;
	@Field( "updatedAt")
	private String updatedAt;

	@Override
	public int hashCode() {
		return 31 * email.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof UserProfileEntity)) {
			return false;
		}
		UserProfileEntity that = (UserProfileEntity) o;

		return email.equals(that.email);
	}

	@Override
	public String toString() {
		return this.email;
	}
}
