package io.melody.core.auth.event.api;

import lombok.Data;

@Data
public class UserLoginEvent {

	private String email;
	public UserLoginEvent(String email) {
		this.email = email;
	}
}
