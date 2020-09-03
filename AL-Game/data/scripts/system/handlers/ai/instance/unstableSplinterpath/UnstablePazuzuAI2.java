package ai.instance.unstableSplinterpath;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import ai.AggressiveNpcAI2;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;


/**
 * @author Luzien
 * @edit Cheatkiller
 */
@AIName("unstablepazuzu")
public class UnstablePazuzuAI2 extends AggressiveNpcAI2 {

	private AtomicBoolean isHome = new AtomicBoolean(true);
	private Future<?> task;

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isHome.compareAndSet(true, false)) {
			NpcShoutsService.getInstance().sendMsg(getOwner(), 342219, getObjectId(), 0, 0);
			startTask();
		}
	}

	@Override
	protected void handleBackHome() {
		super.handleBackHome();
		cancelTask();
		isHome.set(true);
	}
	
	@Override
	protected void handleDied() {
		super.handleDied();
		cancelTask();
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1500003, getObjectId(), 0, 0);
	}

	private void startTask() {
		task = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				SkillEngine.getInstance().getSkill(getOwner(), 19145, 55, getOwner()).useNoAnimationSkill();
				if (getPosition().getWorldMapInstance().getNpc(284024) == null) {
					spawn(284024, 651.351990f, 326.425995f, 465.523987f, (byte) 8);
					spawn(284024, 666.604980f, 314.497009f, 465.394012f, (byte) 27);
					spawn(284024, 685.588989f, 342.955994f, 465.908997f, (byte) 68);
					spawn(284024, 651.322021f, 346.554993f, 465.563995f, (byte) 111);
				}
			}

		}, 5000, 70000); 
	}

	private void cancelTask() {
		if (task != null && !task.isCancelled()) {
			task.cancel(true);
		}
	}
}