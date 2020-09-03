package com.aionemu.gameserver.services.player;

import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.SellLimit;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import javolution.util.FastMap;

/**
 * @author Source
 */
public class PlayerLimitService {

	private static FastMap<Integer, Long> sellLimit = new FastMap<Integer, Long>().shared();

	public static boolean updateSellLimit(Player player, long reward) {
		if (!CustomConfig.LIMITS_ENABLED)
			return true;

		int accoutnId = player.getPlayerAccount().getId();
		Long limit = sellLimit.get(accoutnId);
		if (limit == null) {
			limit = (long) (SellLimit.getSellLimit(player.getPlayerAccount().getMaxPlayerLevel()) * player.getRates().getSellLimitRate());
			sellLimit.put(accoutnId, limit);
		}

		if (limit < reward) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DAY_CANNOT_SELL_NPC(limit));
			return false;
		}
		else {
			limit -= reward;
			sellLimit.putEntry(accoutnId, limit);
			return true;
		}
	}

	public void scheduleUpdate() {
		CronService.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sellLimit.clear();
			}

		}, CustomConfig.LIMITS_UPDATE, true);
	}

	public static PlayerLimitService getInstance() {
		return SingletonHolder.instance;
	}

	private static class SingletonHolder {

		protected static final PlayerLimitService instance = new PlayerLimitService();
	}
}