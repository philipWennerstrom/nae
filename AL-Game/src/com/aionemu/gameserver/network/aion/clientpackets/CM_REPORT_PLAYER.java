package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.utils.audit.AuditLogger;

/**
 * Received when a player reports another player with /ReportAutoHunting
 * 
 * @author Jego
 */
public class CM_REPORT_PLAYER extends AionClientPacket {

	private String player;

	/**
	 * A player gets reported.
	 * 
	 * @param opcode
	 */
	public CM_REPORT_PLAYER(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		readB(1); // unknown byte.
		player = readS(); // the name of the reported person.
	}

	@Override
	protected void runImpl() {
		Player p = getConnection().getActivePlayer();
		AuditLogger.info(p, "Reports the player: " + player);
	}
}