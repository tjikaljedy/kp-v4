package io.melody.core.enums;

public enum RolesEnum {

	ROLE_SYS_ADMIN(0, "ROLE_SYS_ADMIN"), ROLE_PORTAL_ADMIN(1, "ROLE_PORTAL_ADMIN"),
	ROLE_PORTAL_USER(11, "ROLE_PORTAL_USER"), ROLE_USER(12, "ROLE_USER"), ROLE_PREMIUM_ADMIN(21, "ROLE_PREMIUM_ADMIN"),
	ROLE_PREMIUM_TEAM(22, "ROLE_PREMIUM_TEAM");

	private final int id;
	private final String name;

	RolesEnum(int id, String name) {
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
