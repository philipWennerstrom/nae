package quest.satra_treasure_hoard;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Ritsu
 *
 */
public class _28903KnockinOnSatrasGates extends QuestHandler{

	private static final int questId=28903;

	public _28903KnockinOnSatrasGates() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(800330).addOnQuestStart(questId);
		qe.registerQuestNpc(205865).addOnTalkEvent(questId);
		qe.registerQuestNpc(219345).addOnKillEvent(questId);
		qe.registerQuestNpc(219346).addOnKillEvent(questId);
	}

	@Override
	public boolean onKillEvent(QuestEnv env) 
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			switch (targetId)
			{
				case 219345:
				{
					int var = qs.getQuestVarById(0);
					if (var == 0) 
					{
						return defaultOnKillEvent(env, 219345, 0, 1, 0);
					}
				}
				break;
				case 219346:
				{
					int var = qs.getQuestVarById(1);
					if (var == 0)
						return defaultOnKillEvent(env, 219346, 0, 1, 1); // 2: 3
				}
				break;
			}
		}
		return false;
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 800330) {
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 1011);
				else
					return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 205865) {
				if (env.getDialog() == QuestDialog.START_DIALOG && qs.getQuestVarById(0) == 1) {
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return sendQuestDialog(env, 1352);
				}
				else
					return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 205865) {
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 5);
				else if (env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else
					return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}