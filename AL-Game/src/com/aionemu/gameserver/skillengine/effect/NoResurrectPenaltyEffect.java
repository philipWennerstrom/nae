package com.aionemu.gameserver.skillengine.effect;

import com.aionemu.gameserver.skillengine.model.Effect;

public class NoResurrectPenaltyEffect extends BufEffect {

	@Override
	public void calculate(Effect effect) {
		effect.addSucessEffect(this);
	}
	
	@Override
	public void startEffect(Effect effect) {
		effect.setNoResurrectPenalty(true);
	}	
}