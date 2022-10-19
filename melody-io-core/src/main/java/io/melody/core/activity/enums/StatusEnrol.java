package io.melody.core.activity.enums;

public enum StatusEnrol {
	INITIAL(1, "INITIAL"), NOTIFY(2, "NOTIFY"), REPLY(3, "REPLY"), IN_PROGRESS(4, "IN_PROGRESS"),
	COMPLETE_BIO(5, "COMPLETE_BIO"), COMPLETE(6, "COMPLETE");

	private final int id;
	private final String name;

	StatusEnrol(int id, String name) {
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
