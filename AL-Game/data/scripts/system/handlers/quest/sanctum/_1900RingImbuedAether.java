package quest.sanctum;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Mr. Poke, Dune11
 */
public class _1900RingImbuedAether extends QuestHandler {

	private final static int questId = 1900;

	public _1900RingImbuedAether() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203757).addOnQuestStart(questId);
		qe.registerQuestNpc(203757).addOnTalkEvent(questId);
		qe.registerQuestNpc(203739).addOnTalkEvent(questId);
		qe.registerQuestNpc(203766).addOnTalkEvent(questId);
		qe.registerQuestNpc(203797).addOnTalkEvent(questId);
		qe.registerQuestNpc(203795).addOnTalkEvent(questId);
		qe.registerQuestNpc(203830).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();

		if (sendQuestNoneDialog(env, 203757, 182206003, 1))
			return true;

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
			return false;

		if (env.getTargetId() == 203739) {
			if (qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) {
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 1352);
				else if (env.getDialog() == QuestDialog.STEP_TO_1) {
					return defaultCloseDialog(env, 0, 1);
				}
				else
					return sendQuestStartDialog(env);
			}
		}
		else if (env.getTargetId() == 203766) {
			if (qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 1) {
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 1693);
				else if (env.getDialog() == QuestDialog.STEP_TO_2) {
					return defaultCloseDialog(env, 1, 2);
				}
				else
					return sendQuestStartDialog(env);
			}
		}
		else if (env.getTargetId() == 203797) {
			if (qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 2) {
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 2034);
				else if (env.getDialog() == QuestDialog.STEP_TO_3) {
					return defaultCloseDialog(env, 2, 3);
				}
				else
					return sendQuestStartDialog(env);
			}
		}
		else if (env.getTargetId() == 203795) {
			if (qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 3) {
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 2375);
				else if (env.getDialog() == QuestDialog.STEP_TO_4) {
					return defaultCloseDialog(env, 3, 0, true, false);
				}
				else
					return sendQuestStartDialog(env);
			}
		}
		else if (env.getTargetId() == 203830) {
			if (env.getDialog() == QuestDialog.USE_OBJECT && qs.getStatus() == QuestStatus.REWARD)
				return sendQuestDialog(env, 2716);
			else if (env.getDialogId() == 1009 && qs.getStatus() != QuestStatus.COMPLETE
				&& qs.getStatus() != QuestStatus.NONE) {
				removeQuestItem(env, 182206003, 1);
				return sendQuestDialog(env, 5);
			}
			else
				return sendQuestEndDialog(env);
		}
		return sendQuestRewardDialog(env, 203830, 0);
	}
}