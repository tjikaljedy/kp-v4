package io.melody.core.enums;

public enum StatusEnum {
	NEW(1, "NEW"), NOTIFY(2, "NOTIFY"), UPDATE(11, "UPDATE"), DELETE(12, "DELETE"), PENDING(13, "PENDING"),
	SUCCESS(14, "SUCCESS"), FAILURE(15, "FAIL");

	private final int id;
	private final String name;

	StatusEnum(int id, String name) {
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
