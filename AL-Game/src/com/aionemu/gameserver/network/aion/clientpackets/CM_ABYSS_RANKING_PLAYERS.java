package com.aionemu.gameserver.network.aion.clientpackets;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.AbyssRank.AbyssRankUpdateType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ABYSS_RANKING_PLAYERS;
import com.aionemu.gameserver.services.abyss.AbyssRankingCache;

/**
 * @author SheppeR
 */
public class CM_ABYSS_RANKING_PLAYERS extends AionClientPacket {

	private Race queriedRace;
	private int raceId;
	private AbyssRankUpdateType updateType;

	private static final Logger log = LoggerFactory.getLogger(CM_ABYSS_RANKING_PLAYERS.class);

	public CM_ABYSS_RANKING_PLAYERS(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl() {
		raceId = readC();
		switch (raceId) {
			case 0:
				queriedRace = Race.ELYOS;
				updateType = AbyssRankUpdateType.PLAYER_ELYOS;
				break;
			case 1:
				queriedRace = Race.ASMODIANS;
				updateType = AbyssRankUpdateType.PLAYER_ASMODIANS;
				break;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl() {
		if (queriedRace != null) {
			Player player = this.getConnection().getActivePlayer();
			if (player.isAbyssRankListUpdated(updateType)){
				sendPacket(new SM_ABYSS_RANKING_PLAYERS(AbyssRankingCache.getInstance().getLastUpdate(), queriedRace));
			}
			else{
				List<SM_ABYSS_RANKING_PLAYERS> results = AbyssRankingCache.getInstance().getPlayers(queriedRace);
				for (SM_ABYSS_RANKING_PLAYERS packet : results)
					sendPacket(packet);
				player.setAbyssRankListUpdated(updateType);
			}
		}
		else {
			log.warn("Received invalid raceId: " + raceId);
		}
	}
}