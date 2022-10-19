package io.melody.core.auth.event.api;

import lombok.Data;

@Data
public class FindUserAuth {
	private String email;
	
	public FindUserAuth(String email) {
		this.email = email;
	}
}
