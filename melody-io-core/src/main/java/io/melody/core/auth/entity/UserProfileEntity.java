package io.melody.core.auth.entity;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import io.melody.core.enums.DbEnum;
import io.melody.core.enums.StatusEnum;
import io.melody.core.enums.StatusLogin;
import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@Data
@Document(collection = "userprofile")
public class UserProfileEntity implements Serializable {

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
	@Field("status")
	private String status;
	@Field("event")
	private String event;
	@Field("isComplete")
	private boolean isComplete;
	@Field("isIdVerified")
	private boolean isIdVerified;

	@DBRef(db=DbEnum.CORE_DB)
	@Setter(AccessLevel.NONE)
	private UserAuthEntity userAuth;

	public boolean isAuthVerified() {
		return this.userAuth != null && this.userAuth.isEmailVerified()
				&& (!this.getStatus().equals(StatusEnum.NOTIFY.getName())
						|| !this.getStatus().equals(StatusLogin.INITIAL.getName()));
	}

	public boolean isAllCompleted() {
		return this.isAuthVerified() && this.isComplete() && this.isIdVerified();
	}

	public boolean hasRole(String role) {
		return this.isAuthVerified() && this.userAuth.hasRole(role);
	}

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

	public void setUserAuth(UserAuthEntity userAuth) {
		this.userAuth = userAuth;
		this.email = (userAuth != null) ? userAuth.getEmail() : null;
	}
}
