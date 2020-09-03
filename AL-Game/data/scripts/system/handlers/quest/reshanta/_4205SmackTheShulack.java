package quest.reshanta;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Cheatkiller
 *
 */
public class _4205SmackTheShulack extends QuestHandler {

	private final static int questId = 4205;

	public _4205SmackTheShulack() {
		super(questId);
	}

	public void register() {
		qe.registerQuestNpc(279010).addOnQuestStart(questId);
		qe.registerQuestNpc(279010).addOnTalkEvent(questId);
		qe.registerQuestNpc(204202).addOnTalkEvent(questId);
		qe.registerQuestNpc(204285).addOnTalkEvent(questId);
		qe.registerQuestNpc(218972).addOnKillEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 279010) { 
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			int var = qs.getQuestVarById(0);
			if (targetId == 279010)
			{
				if (dialog == QuestDialog.START_DIALOG) {
					if(qs.getQuestVarById(0) == 15) {
						return sendQuestDialog(env, 1352);
					}
				}
				else if (dialog == QuestDialog.STEP_TO_2) {
					return defaultCloseDialog(env, 15, 16);
				}
			}
			else if (targetId == 204202)
			{
				switch (dialog){
					case START_DIALOG:
						if (var == 16)
							return sendQuestDialog(env, 1693);
					case SET_REWARD:
						if (var == 16)
							changeQuestStep(env, 16, 16, true);
							return closeDialogWindow(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204285) {
				if (dialog == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env) {
		return defaultOnKillEvent(env, 218972, 0, 15);
	}
}