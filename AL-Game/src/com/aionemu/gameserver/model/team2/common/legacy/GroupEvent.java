package com.aionemu.gameserver.model.team2.common.legacy;

/**
 * @author Lyahim
 */
public enum GroupEvent {

	LEAVE(0),
	MOVEMENT(1),
	DISCONNECTED(3),
	JOIN(5),
	ENTER_OFFLINE(7),
	ENTER(13),
	UPDATE(13),
	UNK(9); // to do
	private int id;

	private GroupEvent(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}
}