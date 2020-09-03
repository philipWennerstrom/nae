package quest.beluslan;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Gigi
 */
public class _28601NightmaresandDreamscapes extends QuestHandler {

	private final static int questId = 28601;
	private final static int[] npc_ids = { 204702, 205234 };

	public _28601NightmaresandDreamscapes() {
		super(questId);
	}

	@Override
	public void register() {
		for (int npc_id : npc_ids)
			qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
		qe.registerQuestNpc(204702).addOnQuestStart(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 204702) {
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env, 182213006, 1);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 205234) {
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 2375);
				}
				else if (env.getDialog() == QuestDialog.SELECT_REWARD) {
					return defaultCloseDialog(env, 0, 0, true, true, 0, 0, 182213006, 1);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 205234) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}