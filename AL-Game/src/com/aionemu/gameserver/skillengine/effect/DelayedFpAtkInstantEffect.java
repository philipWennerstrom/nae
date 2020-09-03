package com.aionemu.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAttribute;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author kecimis
 *
 */
public class DelayedFpAtkInstantEffect extends EffectTemplate {

	@XmlAttribute
	protected int delay;
	@XmlAttribute
	protected boolean percent;
	
	
	@Override
	public void calculate(Effect effect) {
		// Only players have FP
		if (effect.getEffected() instanceof Player)
			super.calculate(effect, null, null);
	}
	
	@Override
	public void applyEffect(final Effect effect) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (effect.getEffector().isEnemy(effect.getEffected()))
					calculateAndApplyDamage(effect);
			}
		}, delay);
	}
	
	private void calculateAndApplyDamage(Effect effect) {
		if (!(effect.getEffected() instanceof Player))
			return;
		
		int valueWithDelta = value + delta * effect.getSkillLevel();
		Player player = (Player) effect.getEffected();
		int maxFP = player.getLifeStats().getMaxFp();
		
		int newValue = valueWithDelta;
		// Support for values in percentage
		if (percent)
			newValue = (int) ((maxFP * valueWithDelta) / 100);
		
		player.getLifeStats().reduceFp(newValue);
	}
}