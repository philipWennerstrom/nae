package com.aionemu.chatserver.network.aion.serverpackets;

import org.jboss.netty.buffer.ChannelBuffer;

import com.aionemu.chatserver.network.aion.AbstractServerPacket;
import com.aionemu.chatserver.network.netty.handler.ClientChannelHandler;

/**
 * @author ATracer
 */
public class SM_PLAYER_AUTH_RESPONSE extends AbstractServerPacket {

	public SM_PLAYER_AUTH_RESPONSE() {
		super(0x02);
	}

	@Override
	protected void writeImpl(ClientChannelHandler clientChannelHandler, ChannelBuffer buf) {
		writeC(buf, getOpCode());
		writeC(buf, 0x40); // ?
		writeH(buf, 0x01); // ?
		writeD(buf, 0x0BDD0000); // TODO this is actually dynamic
	}
}