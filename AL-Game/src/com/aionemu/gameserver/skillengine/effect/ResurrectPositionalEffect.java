package com.aionemu.gameserver.skillengine.effect;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.network.aion.serverpackets.SM_RESURRECT;
import com.aionemu.gameserver.utils.PacketSendUtility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Sippolo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResurrectPositionalEffect")
public class ResurrectPositionalEffect extends ResurrectEffect {
	
	@Override
	public void applyEffect(Effect effect) {
		Player effector = (Player) effect.getEffector();
		Player effected = (Player) effect.getEffected();

		effected.setPlayerResActivate(true);
		effected.setResurrectionSkill(skillId);
		PacketSendUtility.sendPacket(effected, new SM_RESURRECT(effect.getEffector(), effect.getSkillId()));
		effected.setResPosState(true);
		effected.setResPosX(effector.getX());
		effected.setResPosY(effector.getY());
		effected.setResPosZ(effector.getZ());
	}

	@Override
	public void calculate(Effect effect) {
		if ((effect.getEffector() instanceof Player) && (effect.getEffected() instanceof Player)
			&& (effect.getEffected().getLifeStats().isAlreadyDead()))
			super.calculate(effect, null, null);
	}
}