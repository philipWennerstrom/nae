package com.aionemu.gameserver.skillengine.effect;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.model.Effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Sippolo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FpAttackInstantEffect")
public class FpAttackInstantEffect extends EffectTemplate {

	@XmlAttribute
	protected boolean percent;

	@Override
	public void calculate(Effect effect) {
		// Only players have FP
		if (effect.getEffected() instanceof Player)
			super.calculate(effect, null, null);
	}
	
	@Override
	public void applyEffect(Effect effect) {
		// Restriction to players because lack of FP on other Creatures
		if (!(effect.getEffected() instanceof Player))
			return;
		Player player = (Player) effect.getEffected();
		int maxFP = player.getLifeStats().getMaxFp();
		int newValue = value;
		// Support for values in percentage
		if (percent)
			newValue = (int) ((maxFP * value) / 100);
		player.getLifeStats().reduceFp(newValue);
	}
}