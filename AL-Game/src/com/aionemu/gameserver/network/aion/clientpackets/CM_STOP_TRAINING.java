package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;

/**
 *
 * @author xTz
 */
public class CM_STOP_TRAINING extends AionClientPacket {

	public CM_STOP_TRAINING(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		// nothing to read
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();

		switch (player.getWorldId()) {
			case 300320000:
			case 300300000:
				player.getPosition().getWorldMapInstance().getInstanceHandler().onStopTraining(player);	
				break;
		}
	}
}