package instance;

import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldPosition;

/**
 * @author xTz,Ritsu
 */
@InstanceID(310110000)
public class TheobomosLabInstance extends GeneralInstanceHandler {
	
	private boolean isInstanceDestroyed;
	private boolean isDead1 = false;
	private boolean isDead2 = false;
	
	@Override
	public void onDie(Npc npc) {
		if(isInstanceDestroyed)
			return;
		Creature master = npc.getMaster();
		if (master instanceof Player)
			return;

		int npcId = npc.getNpcId();
		switch (npcId) {
			case 280971:
			case 280972: {
				if(guardDie(npc))
				removeBuff();
			}
		}
	}
	
/**	@Override
	public void onEnterInstance(Player player) 
	{
		final QuestState qs = player.getQuestStateList().getQuestState(1094);
		if (qs != null && qs.getStatus() == QuestStatus.COMPLETE) 
			doors.get(37).setOpen(true);
		else
			doors.get(37).setOpen(false);
	}//this door is static door, so we cant control it.
	*/
	
	@Override
	public void onInstanceDestroy() {
		isDead1 = false;
		isDead2 = false;
		isInstanceDestroyed = true;
	}
	
	private boolean guardDie(Npc npc) {
		WorldPosition p = npc.getPosition();
		int npcId = npc.getNpcId();
		Npc orb = getNpc(280973);
		if(MathUtil.getDistance(orb, npc) <= 7) {
			switch (npcId) {
				case 280971: {
					isDead1 = true;
					break;
				}
				case 280972: {
					isDead2 = true;
					break;
				}
			}
			return true;
		}
		else {
			npc.getController().onDelete();
			spawn(npcId, p.getX(), p.getY(), p.getZ(), (byte) 41);
			return false;
		}
	}
	
	private void removeBuff() {
		ThreadPoolManager.getInstance().schedule(new Runnable()  {
			@Override
			public void run() {
				if(!isInstanceDestroyed && isDead1 && isDead2) {
					getNpc(214668).getEffectController().removeEffect(18481);
					getNpc(280973).getController().onDelete();
				}
			}
		}, 1000);
	}
}