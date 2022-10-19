package io.melody.core.auth.cmd.api;

import java.io.Serializable;

import org.axonframework.common.IdentifierFactory;

public class TokenId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7867015380584167664L;
	private String tokenId;

	public TokenId() {
		this.tokenId = IdentifierFactory.getInstance().generateIdentifier();
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	@Override
	public int hashCode() {
		return 31 * tokenId.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof TokenId)) {
			return false;
		}
		TokenId that = (TokenId) o;

		return tokenId.equals(that.tokenId);
	}

	@Override
	public String toString() {
		return this.tokenId;
	}
}
