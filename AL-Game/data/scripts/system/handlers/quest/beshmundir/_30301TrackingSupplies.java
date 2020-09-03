package quest.beshmundir;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Gigi
 */

public class _30301TrackingSupplies extends QuestHandler {

	private final static int questId = 30301;

	public _30301TrackingSupplies() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(799225).addOnQuestStart(questId);
		qe.registerQuestNpc(799225).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		if (targetId == 799225) {
			if (qs == null || qs.getStatus() == QuestStatus.NONE) {
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 1011);
				else
					return sendQuestStartDialog(env);
			}
			else if (qs.getStatus() == QuestStatus.START) {
				long itemCount;
				if (env.getDialog() == QuestDialog.START_DIALOG && qs.getQuestVarById(0) == 0) {
					return sendQuestDialog(env, 2375);
				}
				else if (env.getDialogId() == 34 && qs.getQuestVarById(0) == 0) {
					itemCount = player.getInventory().getItemCountByItemId(182209701);
					if (itemCount > 0) {
						removeQuestItem(env, 182209701, 1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return sendQuestDialog(env, 5);
					}
					else {
						return sendQuestDialog(env, 2716);
					}
				}
				else
					return sendQuestEndDialog(env);
			}
			else {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}