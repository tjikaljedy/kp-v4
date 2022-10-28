package io.melody.core.auth.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.axonframework.common.IdentifierFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;

import io.melody.core.enums.DbEnum;
import io.melody.core.utils.AppUtils;
import io.melody.core.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Document(collection = "userauth")
public class UserAuthEntity implements UserDetails {
	
	@Id
	private Long _id;
	@Field("userID")
	private String userID;
	@Field("email")
	private String email;
	@Setter(AccessLevel.NONE)
	@Field("username")
	private String username;
	@Setter(AccessLevel.NONE)
	@Field("password")
	private String password;
	@Field("createdAt")
	private String createdAt;
	@Field("updatedAt")
	private String updatedAt;
	@Field("userRef")
	private String userRef;
	@Field("mobile")
	private String mobile;
	@Field("isExpiryPass")
	private boolean isExpiryPass;
	@Field("isEmailVerified")
	private boolean isEmailVerified;
	@Field("oneTimePassword")
	private String oneTimePassword;
	@Field("otpRequestTime")
	private long otpRequestTime;
	@Field("otpAttempt")
	private long otpAttempt;
	@Field("otpNextAttemptAllow")
	private long otpNextAttemptAllow;
	@Field("otpToken")
	private String otpToken;

	// UserDetails
	@Setter(AccessLevel.NONE)
	@Field("isEnabled")
	private boolean isEnabled;
	@Setter(AccessLevel.NONE)
	@Field("isAccountNonExpired")
	private boolean isAccountNonExpired;
	@Setter(AccessLevel.NONE)
	@Field("isAccountNonLocked")
	private boolean isAccountNonLocked;
	@Setter(AccessLevel.NONE)
	@Field("isCredentialsNonExpired")
	private boolean isCredentialsNonExpired;
	@Field("roles")
	private List<String> roles;

	@Transient
	private transient List<UserRoleEntity> userRoles;

	public UserAuthEntity() {
		this.isExpiryPass = false;
		this.isEmailVerified = false;
		this.isEnabled = true;
		this.isAccountNonExpired = true;
		this.isAccountNonLocked = true;
		this.isCredentialsNonExpired = true;
		this.createdAt = AppUtils.dateToDateStringFormat(new Date(), "yyyy-MM-dd HH:mm:ss", AppUtils.ZONE_GMT_PLUS7);
		this.updatedAt = AppUtils.dateToDateStringFormat(new Date(), "yyyy-MM-dd HH:mm:ss", AppUtils.ZONE_GMT_PLUS7);
		this.initialUserRoles();
	}

	public UserAuthEntity(String username, String email, String password, String userRef) {
		this.userID = IdentifierFactory.getInstance().generateIdentifier();
		this.username = username;
		this.password = AppUtils.hashOfPassword(password);
		this.email = email;
		this.userRef = userRef;
		this.isExpiryPass = false;
		this.isEmailVerified = false;
		this.isEnabled = true;
		this.isAccountNonExpired = true;
		this.isAccountNonLocked = true;
		this.isCredentialsNonExpired = true;
		this.createdAt = AppUtils.dateToDateStringFormat(new Date(), "yyyy-MM-dd HH:mm:ss", AppUtils.ZONE_GMT_PLUS7);
		this.updatedAt = AppUtils.dateToDateStringFormat(new Date(), "yyyy-MM-dd HH:mm:ss", AppUtils.ZONE_GMT_PLUS7);
		this.initialUserRoles();
	}

	public boolean isValidPassword(String passInDto) {
		return BCrypt.checkpw(passInDto, this.password);
	}
	
	public List<String> getRoles() {
		if (CollectionUtils.isEmpty(roles)) {
			roles = new ArrayList<String>();
		}
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public boolean hasRole(String role) {
		return this.roles.stream().anyMatch(role::equals);
	}

	// UserDetail Implementation
	public void initialUserRoles() {
		this.userRoles = new ArrayList<UserRoleEntity>();
		UserRoleEntity role = new UserRoleEntity();
		this.userRoles.add(role);
		this.roles = this.userRoles.stream().map(s -> {
			return s.getName();
		}).collect(Collectors.toList());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (CollectionUtils.isEmpty(this.roles)) {
			this.initialUserRoles();
		}
		return CollectionUtils.isNotEmpty(this.userRoles)
				? this.userRoles.stream()
						.map(a -> new SimpleGrantedAuthority(a.getName()))
						.collect(Collectors.toList())
				: null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.isAccountNonExpired;
	}

	public void setAccountNonExpired(boolean isAccountNonExpired) {
		this.isAccountNonExpired = isAccountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.isAccountNonLocked;
	}

	public void setAccountNonLocked(boolean isAccountNonLocked) {
		this.isAccountNonLocked = isAccountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.isCredentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean isCredentialsNonExpired) {
		this.isCredentialsNonExpired = isCredentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = SecurityUtils.hashOfPassword(password);
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	// UserDetail Implementation

	// Common
	@Override
	public int hashCode() {
		return 31 * email.hashCode() + username.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof UserAuthEntity)) {
			return false;
		}
		UserAuthEntity that = (UserAuthEntity) o;

		return email.equals(that.email) && username.equals(that.username);
	}

	@Override
	public String toString() {
		return this.email + "-" + this.username;
	}

}
