package io.melody.core.auth.cmd.api;

import lombok.Data;

@Data
public class AddUserCommand {
	private String email;
	
	public AddUserCommand(String email) {
		this.email = email;
	}
}
