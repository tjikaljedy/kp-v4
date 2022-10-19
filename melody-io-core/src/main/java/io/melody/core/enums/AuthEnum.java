package io.melody.core.enums;

public enum AuthEnum {
	TOKEN(1, "x-auth-token"), USER(2, "x-auth-user"), POST_ID(3, "post-id"), MODE(4, "x-auth-mode"),
	STREAM_MODE(4, "x-stream-mode"), KEY_EMAIL(11, "email"), KEY_DEVICE(12, "deviceId"), KEY_OTP(13, "otpToken"),
	KEY_DATA(15, "data"), KEY_POSTID(15, "postID");

	private final int id;
	private final String name;

	AuthEnum(int id, String name) {
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
