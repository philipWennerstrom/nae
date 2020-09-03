package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * This packet is response for CM_QUIT
 * 
 * @author -Nemesiss-
 */
public class SM_QUIT_RESPONSE extends AionServerPacket {

	private boolean edit_mode = false;

	public SM_QUIT_RESPONSE() {
	}

	public SM_QUIT_RESPONSE(boolean edit_mode) {
		this.edit_mode = edit_mode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		writeD(edit_mode ? 2 : 1);// 1 normal, 2 plastic surgery/gender switch
		writeC(0x00);//unk
		writeC(0xFF);//unk 3.0
		writeC(0xFF);//unk 3.0
		writeC(0xFF);//unk 3.0
		writeC(0xFF);//unk 3.0
	}
}