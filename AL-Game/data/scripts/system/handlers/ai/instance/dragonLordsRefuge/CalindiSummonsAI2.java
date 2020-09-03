package ai.instance.dragonLordsRefuge;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.poll.AIAnswer;
import com.aionemu.gameserver.ai2.poll.AIAnswers;
import com.aionemu.gameserver.ai2.poll.AIQuestion;
import java.util.concurrent.Future;

/**
 * @author Cheatkiller
 * @modified Luzien
 *
 */
@AIName("calindisummon")
public class CalindiSummonsAI2 extends NpcAI2 {

	private Future<?> task;

	@Override
	protected void handleSpawned() {

		super.handleSpawned();
		final int skill = getOwner().getNpcId() == 283326 ? 20914 : 20916;
		int delay = getNpcId() == 283326 ? 500 : 2000;
		task = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				AI2Actions.useSkill(CalindiSummonsAI2.this, skill);
			}
		}, delay, delay);

		despawn();
	}

	private void despawn() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				getOwner().getController().onDelete();
			}
		}, 15000);
	}

	@Override
	public void handleDespawned() {
		task.cancel(true);
		super.handleDespawned();
	}

	@Override
	protected AIAnswer pollInstance(AIQuestion question) {
		switch (question) {
			case SHOULD_DECAY:
				return AIAnswers.NEGATIVE;
			case SHOULD_RESPAWN:
				return AIAnswers.NEGATIVE;
			case SHOULD_REWARD:
				return AIAnswers.NEGATIVE;
			default:
				return null;
		}
	}
}