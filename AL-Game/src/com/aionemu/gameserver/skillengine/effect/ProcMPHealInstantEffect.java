package com.aionemu.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.HealType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProcMPHealInstantEffect")
public class ProcMPHealInstantEffect extends AbstractHealEffect {

	@Override
	public void calculate(Effect effect) {
		super.calculate(effect, HealType.MP);
	}
	
	@Override
	public void applyEffect(Effect effect) {
		super.applyEffect(effect, HealType.MP);
	}

	@Override
	protected int getCurrentStatValue(Effect effect) {
		return effect.getEffected().getLifeStats().getCurrentMp();
	}

	@Override
	protected int getMaxStatValue(Effect effect) {
		return effect.getEffected().getGameStats().getMaxMp().getCurrent();
	}
}