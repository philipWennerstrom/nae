package com.aionemu.gameserver.taskmanager.tasks;

import java.util.Map;

import javolution.util.FastMap;

import com.aionemu.gameserver.model.IExpirable;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.taskmanager.AbstractPeriodicTaskManager;
import java.util.Iterator;

/**
 * @author Mr. Poke
 */
public class ExpireTimerTask extends AbstractPeriodicTaskManager {

	private FastMap<IExpirable, Player> expirables = new FastMap<IExpirable, Player>();

	/**
	 * @param period
	 */
	public ExpireTimerTask() {
		super(1000);
	}

	public static ExpireTimerTask getInstance() {
		return SingletonHolder._instance;
	}

	public void addTask(IExpirable expirable, Player player) {
		writeLock();
		try {
			expirables.put(expirable, player);
		}
		finally {
			writeUnlock();
		}
	}

	public void removePlayer(Player player) {
		writeLock();
		try {
			for (Map.Entry<IExpirable, Player> entry : expirables.entrySet()) {
				if (entry.getValue() == player)
					expirables.remove(entry.getKey());
			}
		}
		finally {
			writeUnlock();
		}
	}

	@Override
	public void run() {
		writeLock();
		try {
			int timeNow = (int) (System.currentTimeMillis() / 1000);
			for (Iterator<Map.Entry<IExpirable, Player>> i = expirables.entrySet().iterator(); i.hasNext(); ) {
				Map.Entry<IExpirable, Player> entry = i.next();
				IExpirable expirable = entry.getKey();
				Player player = entry.getValue();
				int min = (expirable.getExpireTime() - timeNow);
				if (min < 0 && expirable.canExpireNow()) {
					expirable.expireEnd(player);
					i.remove();
					continue;
				}
				switch (min) {
					case 1800:
					case 900:
					case 600:
					case 300:
					case 60:
						expirable.expireMessage(player, min / 60);
						break;
				}
			}
		}
		finally {
			writeUnlock();
		}
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final ExpireTimerTask _instance = new ExpireTimerTask();
	}
}