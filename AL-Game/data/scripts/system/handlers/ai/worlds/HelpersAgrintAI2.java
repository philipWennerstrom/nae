package ai.worlds;

import ai.AggressiveNpcAI2;
import com.aionemu.gameserver.ai2.AIName;

/**
 * @author xTz
 */
@AIName("helpers_agrint")
public class HelpersAgrintAI2 extends AggressiveNpcAI2 {

	@Override
	public int modifyOwnerDamage(int damage) {
		return 1;
	}

	@Override
	public int modifyDamage(int damage) {
		return 1;
	}
}