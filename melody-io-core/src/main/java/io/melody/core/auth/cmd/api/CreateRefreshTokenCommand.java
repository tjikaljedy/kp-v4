package io.melody.core.auth.cmd.api;

import lombok.Data;

@Data
public class CreateRefreshTokenCommand {
	private TokenId tokenId;
	
	public CreateRefreshTokenCommand(TokenId tokenId) {
		this.tokenId = tokenId;
	}
}
