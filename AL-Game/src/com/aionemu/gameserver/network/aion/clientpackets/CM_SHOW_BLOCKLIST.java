package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_BLOCK_LIST;

/**
 * Send when the client requests the blocklist
 * 
 * @author Ben
 */
public class CM_SHOW_BLOCKLIST extends AionClientPacket {

	public CM_SHOW_BLOCKLIST(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl() {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl() {
		sendPacket(new SM_BLOCK_LIST());

	}
}