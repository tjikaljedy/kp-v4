package io.melody.core.activity;

public class StateKey {

	private String key;
	private String idx;

	public StateKey(String key) {
		this.key = key;
	}

	public StateKey(String key, String idx) {
		this.key = key;
		this.idx = idx;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	@Override
	public int hashCode() {
		return 31 * idx.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof StateKey)) {
			return false;
		}
		StateKey that = (StateKey) o;

		return idx.equals(that.idx);
	}

	@Override
	public String toString() {
		return this.idx;
	}
}
