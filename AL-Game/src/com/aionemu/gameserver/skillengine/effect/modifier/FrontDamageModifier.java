package com.aionemu.gameserver.skillengine.effect.modifier;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.PositionUtil;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FrontDamageModifier")
public class FrontDamageModifier extends ActionModifier {

	@Override
	public int analyze(Effect effect) {
		return value + effect.getSkillLevel() * delta;
	}

	@Override
	public boolean check(Effect effect) {
		return PositionUtil.isInFrontOfTarget(effect.getEffector(), effect.getEffected());
	}
}