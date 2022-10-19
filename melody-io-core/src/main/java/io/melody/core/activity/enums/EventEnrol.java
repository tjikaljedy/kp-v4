package io.melody.core.activity.enums;

public enum EventEnrol {
	LOGIN(1, "LOGIN"), SIGN_UP(2, "SIGN_UP"), NOTIFY_OTP(3, "NOTIFY_OTP"), 
	VERIFY_OTP(4, "VERIFY_OTP"), UPDATE_BIO(5, "UPDATE_BIO"),
	UPDATE_PROFILE(6, "UPDATE_PROFILE"), COMPLETE_PROFILE(7, "COMPLETE_PROFILE"), ENROL(8,"ENROL");

	private final int id;
	private final String name;

	EventEnrol(int id, String name) {
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
