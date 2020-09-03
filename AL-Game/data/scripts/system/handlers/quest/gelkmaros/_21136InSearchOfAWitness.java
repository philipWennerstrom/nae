package quest.gelkmaros;

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
public class _21136InSearchOfAWitness extends QuestHandler {

	private final static int questId = 21136;

	public _21136InSearchOfAWitness() {
		super(questId);
	}

	public void register() {
		qe.registerQuestNpc(799271).addOnQuestStart(questId);
		qe.registerQuestNpc(799413).addOnTalkEvent(questId);
		qe.registerQuestNpc(799414).addOnTalkEvent(questId);
		qe.registerQuestNpc(730355).addOnTalkEvent(questId);
		qe.registerCanAct(getQuestId(), 730355);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 799271) { 
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env, 182207919, 1);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 799413) {
				if (dialog == QuestDialog.START_DIALOG) {
					if(qs.getQuestVarById(0) == 0)
						return sendQuestDialog(env, 1352);
				}
				else if (dialog == QuestDialog.STEP_TO_1) {
					removeQuestItem(env, 182207919, 1);
					return defaultCloseDialog(env, 0, 1);
				}
			}
			else if (targetId == 799414) {
				if (dialog == QuestDialog.START_DIALOG) {
					if(qs.getQuestVarById(0) == 1)
						return sendQuestDialog(env,1693);
				}
				else if (dialog == QuestDialog.STEP_TO_2) {
					qs.setQuestVar(2);
					return defaultCloseDialog(env, 2, 2, true, false);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 730355) {
				if (dialog == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 2375);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}