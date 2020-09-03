package com.aionemu.gameserver.model.gameobjects;

import org.apache.commons.lang.StringUtils;

import com.aionemu.gameserver.controllers.NpcController;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.container.HomingGameStats;
import com.aionemu.gameserver.model.stats.container.NpcLifeStats;
import com.aionemu.gameserver.model.templates.item.ItemAttackType;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;

/**
 * @author ATracer
 */
public class Homing extends SummonedObject<Creature> {

	/**
	 * Number of performed attacks
	 */
	private int attackCount;
	
	private int skillId;
	
	/**
	 * Skill id of this homing. 
	 * 0 - usually attack, other - skills.
	 */
	private int activeSkillId;

	/**
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 * @param level
	 */
	public Homing(int objId, NpcController controller, SpawnTemplate spawnTemplate, NpcTemplate objectTemplate, byte level, int skillId) {
		super(objId, controller, spawnTemplate, objectTemplate, level);
		this.skillId = skillId;
	}
	
	@Override
	protected void setupStatContainers(byte level) {
		setGameStats(new HomingGameStats(this));
		setLifeStats(new NpcLifeStats(this));
	}

	/**
	 * @param attackCount
	 *          the attackCount to set
	 */
	public void setAttackCount(int attackCount) {
		this.attackCount = attackCount;
	}

	/**
	 * @return the attackCount
	 */
	public int getAttackCount() {
		return attackCount;
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
	 * @return NpcObjectType.HOMING
	 */
	@Override
	public NpcObjectType getNpcObjectType() {
		return NpcObjectType.HOMING;
	}

	@Override
	public String getMasterName() {
		return StringUtils.EMPTY;
	}

	@Override
	public ItemAttackType getAttackType() {
		if (getName().contains("fire"))
			return ItemAttackType.MAGICAL_FIRE;
		else if (getName().contains("stone"))
			return ItemAttackType.MAGICAL_EARTH;
		else if (getName().contains("water"))
			return ItemAttackType.MAGICAL_WATER;
		else if ((getName().contains("wind")) || (getName().contains("cyclone")))
			return ItemAttackType.MAGICAL_WIND;
		return ItemAttackType.PHYSICAL;
	}
	
	public int getSkillId() {
		return skillId;
	}
	
	public int getActiveSkillId() {
		return activeSkillId;
	}

	public void setActiveSkillId(int activeSkillId) {
		this.activeSkillId = activeSkillId;
	}
}