package quest.gelkmaros;

import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author VladimirZ
 */
public class _21033ExorcisingInfisto extends QuestHandler {

	private final static int questId = 21033;

	public _21033ExorcisingInfisto() {
		super(questId);
	}

	@Override
	public void register() {
		int[] npcs = { 799256, 204734 };
		for (int npc : npcs)
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		qe.registerQuestNpc(799256).addOnQuestStart(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		if (sendQuestNoneDialog(env, 799256, 182207829, 1))
			return true;

		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
		if (qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		if (qs.getStatus() == QuestStatus.START) {
			if (env.getTargetId() == 204734) {
				switch (env.getDialog()) {
					case START_DIALOG:
						if (var == 0)
							return sendQuestDialog(env, 1352);
					case STEP_TO_1:
						return defaultCloseDialog(env, 0, 1, true, false, 182207830, 1, 182207829, 1);
				}
			}
		}
		return sendQuestRewardDialog(env, 799256, 2375);
	}
}