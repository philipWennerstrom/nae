package com.aionemu.gameserver.model.gameobjects.player.motion;

import java.util.HashMap;
import java.util.Map;

import com.aionemu.gameserver.model.IExpirable;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author MrPoke
 *
 */
public class Motion implements IExpirable{
	
	static final Map<Integer, Integer> motionType = new HashMap<Integer, Integer>();
	static{
		motionType.put(1, 1);
		motionType.put(2, 2);
		motionType.put(3, 3);
		motionType.put(4, 4);
		motionType.put(5, 1);
		motionType.put(6, 2);
		motionType.put(7, 3);
		motionType.put(8, 4);
	}
	private int id;
	private int deletionTime = 0;
	private boolean active = false;

	/**
	 * @param id
	 * @param deletionTime
	 */
	public Motion(int id, int deletionTime, boolean isActive) {
		this.id = id;
		this.deletionTime = deletionTime;
		this.active = isActive;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	public int getRemainingTime(){
		if (deletionTime == 0)
			return 0;
		return deletionTime-(int)(System.currentTimeMillis()/1000);
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public int getExpireTime() {
		return deletionTime;
	}

	@Override
	public void expireEnd(Player player) {
		player.getMotions().remove(id);
	}

	@Override
	public void expireMessage(Player player, int time) {
	}

	@Override
	public boolean canExpireNow() {
		return true;
	}
}