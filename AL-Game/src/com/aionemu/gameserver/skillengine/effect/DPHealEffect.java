package com.aionemu.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.HealType;

/**
 * @author kecimis
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DPHealEffect")
public class DPHealEffect extends HealOverTimeEffect {

	@Override
	public void calculate(Effect effect) {
		super.calculate(effect, HealType.DP);
	}

	@Override
	public void onPeriodicAction(Effect effect) {
		super.onPeriodicAction(effect, HealType.DP);
	}

	@Override
	protected int getCurrentStatValue(Effect effect) {
		return ((Player) effect.getEffected()).getCommonData().getDp();
	}

	@Override
	protected int getMaxStatValue(Effect effect) {
		return ((Player)effect.getEffected()).getGameStats().getMaxDp().getCurrent();
	}
}