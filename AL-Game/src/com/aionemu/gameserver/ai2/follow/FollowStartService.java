package com.aionemu.gameserver.ai2.follow;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Summon;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import java.util.concurrent.Future;

/**
 *
 * @author xTz
 */
public class FollowStartService {
	/**
	 * Schedule new following checker task
	 * 
	 * @param player
	 * @param Creture
	 * @param target
	 * @return
	 */
	public static final Future<?> newFollowingToTargetCheckTask(Summon follower, Creature leading) {
		return ThreadPoolManager.getInstance().scheduleAtFixedRate(new FollowSummonTaskAI(leading, follower), 1000, 1000);
	}
}