package com.aionemu.gameserver.model;

/**
 *
 * @author xTz
 */
public enum EventType {

	NONE(0, ""),
	CHRISTMAS(1 << 0, "christmas"), // 1
	HALLOWEEN(1 << 1, "halloween"), // 2
	VALENTINE(1 << 2, "valentine"); // 4

	private int id;
	private String theme;

	private EventType(int id, String theme) {
		this.id = id;
		this.theme = theme;
	}

	public int getId() {
		return id;
	}

	public String getTheme() {
		return theme;
	}

	public static EventType getEventType(String theme) {
		for (EventType type : values()) {
			if (theme.equals(type.getTheme())) {
				return type;
			}
		}
		return EventType.NONE;
	}
}