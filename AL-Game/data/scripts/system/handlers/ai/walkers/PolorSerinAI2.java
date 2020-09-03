package ai.walkers;

import org.apache.commons.lang.ArrayUtils;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.handler.MoveEventHandler;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.utils.MathUtil;

/**
 * @author Rolandas
 *
 */
@AIName("polorserin")
public class PolorSerinAI2 extends WalkGeneralRunnerAI2 {
	static final int[] stopAdults = { 203129, 203132 };
	
	@Override
	protected void handleMoveArrived() {
		boolean adultsNear = false;
		for (VisibleObject object : getOwner().getKnownList().getKnownObjects().values()) {
			if (object instanceof Npc) {
				Npc npc = (Npc)object;
				if (!ArrayUtils.contains(stopAdults, npc.getNpcId()))
					continue;
				if (MathUtil.isIn3dRange(npc, getOwner(), getOwner().getAggroRange())) {
					adultsNear = true;
					break;
				}
			}
		}
		if (adultsNear) {
			MoveEventHandler.onMoveArrived(this);
			getOwner().unsetState(CreatureState.WEAPON_EQUIPPED);
		} else {
			super.handleMoveArrived();
			getOwner().setState(CreatureState.WEAPON_EQUIPPED);
		}
	}
}