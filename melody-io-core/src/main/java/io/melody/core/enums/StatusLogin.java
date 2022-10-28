package io.melody.core.enums;

public enum StatusLogin {
	INITIAL(1, "INITIAL"), USER_LOGOUT(2, "USER_LOGOUT"), COMPLETE(3, "COMPLETE"), ACTIVE(4, "ACTIVE"),
	INACTIVE(5, "INACTIVE");

	private final int id;
	private final String name;

	StatusLogin(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
