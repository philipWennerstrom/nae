package quest.sanctum;

import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

public class _1964ASouvenirForNoris extends QuestHandler {

	private final static int questId = 1964;

	public _1964ASouvenirForNoris() {
		super(questId);
	}

	@Override
	public void register() {
		int[] npcs = { 203726, 203776 };
		qe.registerQuestNpc(203726).addOnQuestStart(questId);
		for (int npc : npcs)
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		if (sendQuestNoneDialog(env, 203726, 182206033, 1))
			return true;

		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
		if (qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		if (qs.getStatus() == QuestStatus.START) {
			if (env.getTargetId() == 203776) {
				switch (env.getDialog()) {
					case START_DIALOG:
						if (var == 0)
							return sendQuestDialog(env, 1352);
					case STEP_TO_1:
						return defaultCloseDialog(env, 0, 1, true, false, 0, 0, 0, 182206033, 1);
				}
			}
		}
		return sendQuestRewardDialog(env, 203726, 2375);
	}
}