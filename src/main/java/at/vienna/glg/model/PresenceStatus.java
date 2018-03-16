package at.vienna.glg.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PresenceStatus {
	@JsonProperty("person")
	private String person;

	@JsonProperty("online")
	private boolean isOnline;

	public String getPerson() {
		return this.person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public boolean isOnline() {
		return this.isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}
}
