package ai.worlds.tiamarantasEye;

import ai.AggressiveNpcAI2;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.skillengine.SkillEngine;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Luzien
 * TODO: fight AI
 */
@AIName("berserker_sunayaka")
public class BerserkerSunayakaAI2 extends AggressiveNpcAI2 {

	private AtomicBoolean isHome = new AtomicBoolean(true);
	
	@Override
	public void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if(isHome.compareAndSet(true, false)) {
			if(getOwner().getNpcId() == 219359)
				SkillEngine.getInstance().getSkill(getOwner(), 20651, 1, getOwner()).useNoAnimationSkill(); //ragetask
		}
	}
	
	@Override
	public void handleBackHome() {
		super.handleBackHome();
		isHome.set(true);
		if (getOwner().getNpcId() == 219359) {
			getEffectController().removeEffect(20651);
			getEffectController().removeEffect(8763);
		}
		//SkillEngine.getInstance().getSkill(getOwner(), 20652, 1, getOwner()).useNoAnimationSkill();
	}
}