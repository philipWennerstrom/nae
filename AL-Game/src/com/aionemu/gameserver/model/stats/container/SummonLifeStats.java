package com.aionemu.gameserver.model.stats.container;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Summon;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.LOG;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SUMMON_UPDATE;
import com.aionemu.gameserver.services.LifeStatsRestoreService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer
 */
public class SummonLifeStats extends CreatureLifeStats<Summon> {

	public SummonLifeStats(Summon owner) {
		super(owner, owner.getGameStats().getMaxHp().getCurrent(), owner.getGameStats().getMaxMp().getCurrent());
	}

	@Override
	protected void onIncreaseHp(TYPE type, int value, int skillId, LOG log) {
		Creature master = getOwner().getMaster();
		sendAttackStatusPacketUpdate(type, value, skillId, log);

		if (master instanceof Player) {
			PacketSendUtility.sendPacket((Player) master, new SM_SUMMON_UPDATE(getOwner()));
		}
	}

	@Override
	protected void onIncreaseMp(TYPE type, int value, int skillId, LOG log) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onReduceHp() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onReduceMp() {
		// TODO Auto-generated method stub
	}

	@Override
	public Summon getOwner() {
		return (Summon) super.getOwner();
	}

	@Override
	public void triggerRestoreTask() {
		restoreLock.lock();
		try {
			if (lifeRestoreTask == null && !alreadyDead) {
				this.lifeRestoreTask = LifeStatsRestoreService.getInstance().scheduleHpRestoreTask(this);
			}
		}
		finally {
			restoreLock.unlock();
		}
	}
}