package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.siege.FortressLocation;
import com.aionemu.gameserver.model.siege.Influence;
import com.aionemu.gameserver.model.siege.SourceLocation;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.services.SiegeService;
import java.util.Map;

public class SM_FORTRESS_STATUS extends AionServerPacket {

	@Override
	protected void writeImpl(AionConnection con) {
		Map<Integer, FortressLocation> fortresses = SiegeService.getInstance().getFortresses();
		Map<Integer, SourceLocation> sources = SiegeService.getInstance().getSources();
		Influence inf = Influence.getInstance();

		writeC(1);
		writeD(SiegeService.getInstance().getSecondsBeforeHourEnd());
		writeF(inf.getGlobalElyosInfluence());
		writeF(inf.getGlobalAsmodiansInfluence());
		writeF(inf.getGlobalBalaursInfluence());
		writeH(4);
		writeD(210050000);
		writeF(inf.getInggisonElyosInfluence());
		writeF(inf.getInggisonAsmodiansInfluence());
		writeF(inf.getInggisonBalaursInfluence());
		writeD(220070000);
		writeF(inf.getGelkmarosElyosInfluence());
		writeF(inf.getGelkmarosAsmodiansInfluence());
		writeF(inf.getGelkmarosBalaursInfluence());
		writeD(400010000);
		writeF(inf.getAbyssElyosInfluence());
		writeF(inf.getAbyssAsmodiansInfluence());
		writeF(inf.getAbyssBalaursInfluence());
		writeD(600030000);
		writeF(inf.getTiamarantaElyosInfluence());
		writeF(inf.getTiamarantaAsmodiansInfluence());
		writeF(inf.getTiamarantaBalaursInfluence());
		writeH(fortresses.size() + sources.size());

		for (FortressLocation fortress : fortresses.values()) {
			writeD(fortress.getLocationId());
			writeC(fortress.getNextState());
		}

		for (SourceLocation source : sources.values()) {
			writeD(source.getLocationId());
			writeC(source.getNextState());
		}
	}
}