package ai.instance.dragonLordsRefuge;

import java.util.ArrayList;
import java.util.List;

import ai.AggressiveNpcAI2;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.poll.AIAnswer;
import com.aionemu.gameserver.ai2.poll.AIAnswers;
import com.aionemu.gameserver.ai2.poll.AIQuestion;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.actions.PlayerActions;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import java.util.concurrent.Future;

/**
 * @author Cheatkiller
 *
 */
@AIName("gravitycrusher")
public class GravityCrusherAI2 extends AggressiveNpcAI2 {
   
   private Future<?> skillTask;
   private Future<?> transformTask;
   
	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		transform();
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				attackPlayer();
			}
		}, 2000);
	}

	private void transform() {
		transformTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				if (!isAlreadyDead()) {
				   if (skillTask != null)
					  skillTask.cancel(true);
					AI2Actions.useSkill(GravityCrusherAI2.this, 20967); //self destruct

					ThreadPoolManager.getInstance().schedule(new Runnable() {
						
						@Override
						public void run() {
							NpcShoutsService.getInstance().sendMsg(getOwner(), 1401554);
							spawn(283334, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) getOwner().getHeading());
							AI2Actions.deleteOwner(GravityCrusherAI2.this);
						}
					}, 3000);
					
				}
			}
		}, 30000);
	}
	
	@Override
	public void handleMoveArrived() {
	   super.handleMoveArrived();
	   skillTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run()	{
					AI2Actions.useSkill(GravityCrusherAI2.this, 20987);
				}
			},0, 2000);
	}

	private void attackPlayer() {
		List<Player> players = new ArrayList<Player>();
		for (Player player : getKnownList().getKnownPlayers().values()) {
			if (!PlayerActions.isAlreadyDead(player) && MathUtil.isIn3dRange(player, getOwner(), 200)) {
				players.add(player);
			}
		}
		Player player = !players.isEmpty() ? players.get(Rnd.get(players.size())) : null;
		getOwner().setTarget(player);
		setStateIfNot(AIState.WALKING);
		getOwner().setState(1);
		getOwner().getMoveController().moveToTargetObject();
		PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.START_EMOTE2, 0, getOwner().getObjectId()));
	}
	
	private void cancelTask() {
	   if (skillTask != null && !skillTask.isCancelled())
		  skillTask.cancel(true);
	   if (transformTask != null && !transformTask.isCancelled())
		  transformTask.cancel(true);
	}
	
	@Override
	public void handleDied() {
	   super.handleDied();
	   cancelTask();
	}
	
	@Override
	public void handleDespawned() {
	   super.handleDespawned();
	   cancelTask();
	}
	
	@Override
	public boolean canThink() {
	   return false;
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