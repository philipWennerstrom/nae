package quest.beshmundir;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Gigi
 */

public class _30311GroupAQuartzIsaQuartz extends QuestHandler {

	private final static int questId = 30311;

	public _30311GroupAQuartzIsaQuartz() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(799322).addOnQuestStart(questId);
		qe.registerQuestNpc(799322).addOnTalkEvent(questId);
		qe.registerQuestNpc(730275).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		QuestDialog dialog = env.getDialog();
			
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 799322) {
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}

		if (qs == null)
			return false;

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 730275: {
					switch (dialog) {
						case STEP_TO_1: {
							removeQuestItem(env, 182209714, 1);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return true;
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 799322) {
				switch (dialog) {
					case USE_OBJECT: {
						return sendQuestDialog(env, 10002);
					}
					case SELECT_REWARD: {
						return sendQuestDialog(env, 5);
					}
					default:
						return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}