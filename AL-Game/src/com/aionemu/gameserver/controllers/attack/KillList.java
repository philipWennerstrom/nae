package com.aionemu.gameserver.controllers.attack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aionemu.gameserver.configs.main.CustomConfig;

import javolution.util.FastMap;

/**
 * @author Sarynth
 */
public class KillList {

	private FastMap<Integer, List<Long>> killList;

	public KillList() {
		killList = new FastMap<Integer, List<Long>>();
	}

	/**
	 * @param winnerId
	 * @param victimId
	 * @return killsForVictimId
	 */
	public int getKillsFor(int victimId) {
		List<Long> killTimes = killList.get(victimId);

		if (killTimes == null)
			return 0;

		long now = System.currentTimeMillis();
		int killCount = 0;

		for (Iterator<Long> i = killTimes.iterator(); i.hasNext();) {
			if (now - i.next().longValue() > CustomConfig.PVP_DAY_DURATION) {
				i.remove();
			}
			else {
				killCount++;
			}
		}

		return killCount;
	}

	/**
	 * @param victimId
	 */
	public void addKillFor(int victimId) {
		List<Long> killTimes = killList.get(victimId);
		if (killTimes == null) {
			killTimes = new ArrayList<Long>();
			killList.put(victimId, killTimes);
		}

		killTimes.add(System.currentTimeMillis());
	}
}