package com.aionemu.gameserver.model.gameobjects;

import org.apache.commons.lang.StringUtils;

import com.aionemu.gameserver.controllers.NpcController;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.container.NpcLifeStats;
import com.aionemu.gameserver.model.stats.container.TrapGameStats;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;

/**
 * @author ATracer
 */
public class Trap extends SummonedObject<Creature> {

	/**
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 */
	public Trap(int objId, NpcController controller, SpawnTemplate spawnTemplate, NpcTemplate objectTemplate) {
		super(objId, controller, spawnTemplate, objectTemplate, objectTemplate.getLevel());
	}

	@Override
	protected void setupStatContainers(byte level) {
		setGameStats(new TrapGameStats(this));
		setLifeStats(new NpcLifeStats(this));
	}

	@Override
	public byte getLevel() {
		return (getCreator() == null ? 1 : getCreator().getLevel());
	}

	@Override
	public boolean isEnemy(Creature creature) {
		return getCreator().isEnemy(creature);
	}

	@Override
	public boolean isEnemyFrom(Player player) {
		return getCreator() != null ? getCreator().isEnemyFrom(player) : false;
	}

	/**
	 * @return NpcObjectType.TRAP
	 */
	@Override
	public NpcObjectType getNpcObjectType() {
		return NpcObjectType.TRAP;
	}

	@Override
	public String getMasterName() {
		return StringUtils.EMPTY;
	}
}