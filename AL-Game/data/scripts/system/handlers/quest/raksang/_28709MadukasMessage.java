package quest.raksang;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author zhkchi
 *
 */
public class _28709MadukasMessage extends QuestHandler {

	private static final int questId = 28709;

	public _28709MadukasMessage() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(799429).addOnQuestStart(questId);
		qe.registerQuestNpc(799429).addOnTalkEvent(questId);
		qe.registerQuestNpc(798445).addOnTalkEvent(questId);
		qe.registerQuestNpc(798358).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 799429) {
				switch (env.getDialog()) {
					case START_DIALOG:{
						return sendQuestDialog(env, 1011);
					}
					case ACCEPT_QUEST_SIMPLE:{
						return sendQuestStartDialog(env);
					}
				}
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (targetId == 798445) {
				switch (dialog) {
					case START_DIALOG:
						return sendQuestDialog(env, 1352);
					case SELECT_ACTION_1353:
						return sendQuestDialog(env, 1353);
					case STEP_TO_1:
						return defaultCloseDialog(env, 0, 1);
				}
			}else if(targetId == 798358){
				switch (dialog) {
					case START_DIALOG:
						return sendQuestDialog(env, 2375);
					case SELECT_REWARD:
						changeQuestStep(env, 1, 1, true);
						return sendQuestDialog(env, 5);
				}
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 798358) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}