package io.melody.core.enums;

public enum InfoEnum {
	INFO(1, "info"), NEXT_FLOW(2, "nextFlow"), STATUS(3, "status"), ERROR(4, "error");

	private final int id;
	private final String name;

	InfoEnum(int id, String name) {
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
