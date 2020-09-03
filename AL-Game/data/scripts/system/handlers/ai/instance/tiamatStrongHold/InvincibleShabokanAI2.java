package ai.instance.tiamatStrongHold;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;

/**
 * @author Cheatkiller
 *
 */
@AIName("invincibleshabokan")
public class InvincibleShabokanAI2 extends AggressiveNpcAI2 {

    private AtomicBoolean isHome = new AtomicBoolean(true);
    private Future<?> skillTask;
    private boolean isFinalBuff;

    @Override
    protected void handleAttack(Creature creature) {
        super.handleAttack(creature);
        if (isHome.compareAndSet(true, false)) {
            startSkillTask();
        }
        if (!isFinalBuff && getOwner().getLifeStats().getHpPercentage() <= 25) {
            isFinalBuff = true;
            AI2Actions.useSkill(this, 20941);
        }
    }

    private void startSkillTask() {
        skillTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (isAlreadyDead()) {
                    cancelTask();
                } else {
                    chooseRandomEvent();
                }
            }
        }, 5000, 30000);
    }

    private void cancelTask() {
        if (skillTask != null && !skillTask.isCancelled()) {
            skillTask.cancel(true);
        }
    }

    private void earthQuakeEvent() {
        AI2Actions.targetSelf(InvincibleShabokanAI2.this);
        Npc invisible = getPosition().getWorldMapInstance().getNpc(283217);//3.5
        SkillEngine.getInstance().getSkill(getOwner(), 20717, 55, getOwner()).useNoAnimationSkill();
        if (invisible == null) {
            spawn(283217, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0);//3.5
        }
    }

    private void sinkEvent() {
        AI2Actions.targetSelf(InvincibleShabokanAI2.this);
        SkillEngine.getInstance().getSkill(getOwner(), 20720, 55, getOwner()).useNoAnimationSkill();
        for (Player player : getKnownList().getKnownPlayers().values()) {
            if (isInRange(player, 30)) {
                spawn(283218, player.getX(), player.getY(), player.getZ(), (byte) 0);//3.5
                spawn(283219, player.getX(), player.getY(), player.getZ(), (byte) 0);//3.5
            }
        }
    }

    private void chooseRandomEvent() {
        int rand = Rnd.get(0, 1);
        if (rand == 0) {
            earthQuakeEvent();
        } else {
            sinkEvent();
        }
    }

    @Override
    protected void handleDied() {
        super.handleDied();
        cancelTask();
    }

    @Override
    protected void handleDespawned() {
        super.handleDespawned();
        cancelTask();
    }

    @Override
    protected void handleBackHome() {
        super.handleBackHome();
        cancelTask();
        getOwner().getEffectController().removeEffect(20941);
        isHome.set(true);
    }
}