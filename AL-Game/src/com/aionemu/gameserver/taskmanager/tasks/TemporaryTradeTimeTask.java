package com.aionemu.gameserver.taskmanager.tasks;

import java.util.Collection;
import java.util.Map;

import javolution.util.FastMap;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.taskmanager.AbstractPeriodicTaskManager;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

/**
 * @author Mr. Poke
 */
public class TemporaryTradeTimeTask extends AbstractPeriodicTaskManager {

	private final FastMap<Item, Collection<Integer>> items = new FastMap<Item, Collection<Integer>>();
	private final FastMap<Integer, Item> itemById = new FastMap<Integer, Item>();

	/**
	 * @param period
	 */
	public TemporaryTradeTimeTask() {
		super(1000);
	}

	public static TemporaryTradeTimeTask getInstance() {
		return SingletonHolder._instance;
	}

	public void addTask(Item item, Collection<Integer> players) {
		writeLock();
		try {
			items.put(item, players);
			itemById.put(item.getObjectId(), item);
		}
		finally {
			writeUnlock();
		}
	}

	public boolean canTrade(Item item, int playerObjectId) {
		Collection<Integer> players = items.get(item);
		if (players == null)
			return false;
		return players.contains(playerObjectId);
	}

	public boolean hasItem(Item item) {
		readLock();
		try {
			return items.containsKey(item);
		}
		finally {
			readUnlock();
		}
	}

	public Item getItem(int objectId) {
		readLock();
		try {
			return itemById.get(objectId);
		}
		finally {
			readUnlock();
		}
	}

	@Override
	public void run() {
		writeLock();
		try {
			for (Map.Entry<Item, Collection<Integer>> entry : items.entrySet()) {
				Item item = entry.getKey();
				int time = (item.getTemporaryExchangeTime() - (int) (System.currentTimeMillis() / 1000));
				if (time == 60) {
					for (int playerId : entry.getValue()) {
						Player player = World.getInstance().findPlayer(playerId);
						if (player != null)
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_END_OF_EXCHANGE_TIME(item.getNameID(), time));
					}
				}
				else if (time <= 0) {
					for (int playerId : entry.getValue()) {
						Player player = World.getInstance().findPlayer(playerId);
						if (player != null)
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_EXCHANGE_TIME_OVER(item.getNameID()));
					}
					item.setTemporaryExchangeTime(0);
					items.remove(item);
					itemById.remove(item.getObjectId());
				}
			}
		}
		finally {
			writeUnlock();
		}
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final TemporaryTradeTimeTask _instance = new TemporaryTradeTimeTask();
	}
}