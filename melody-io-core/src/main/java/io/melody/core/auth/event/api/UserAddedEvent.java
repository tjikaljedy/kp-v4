package io.melody.core.auth.event.api;

import lombok.Data;

@Data
public class UserAddedEvent {
	private String email;

	public UserAddedEvent(String email) {
		this.email = email;
	}
}
