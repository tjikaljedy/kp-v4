package io.melody.core.auth.cmd.api;

import io.melody.core.auth.AuthDto;
import lombok.Data;

@Data
public class LoginUserCommand {
	private AuthDto userAuthDto;
	
	public LoginUserCommand(AuthDto userAuthDto) {
		this.userAuthDto = userAuthDto;
	}
}
