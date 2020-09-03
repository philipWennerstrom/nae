package quest.morheim;

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
public class _2345OrashunerkSpecialOrder extends QuestHandler {

	private final static int questId = 2345;
	
	int rewardIndex;
	
	public _2345OrashunerkSpecialOrder() {
		super(questId);
	}

	public void register() {
		qe.registerQuestNpc(798084).addOnQuestStart(questId);
		qe.registerQuestNpc(798084).addOnTalkEvent(questId);
		qe.registerQuestNpc(700238).addOnTalkEvent(questId);
		qe.registerQuestNpc(204339).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 798084) { 
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 798084) { 
				if (dialog == QuestDialog.START_DIALOG) {
					if(var == 0)
						return sendQuestDialog(env, 1011);
					else if(var == 1)
						return sendQuestDialog(env, 1352);
				}
				else if(dialog == QuestDialog.CHECK_COLLECTED_ITEMS) {
				  return checkQuestItems(env, 0, 1, false, 10000, 10001);
				}
				else if(dialog == QuestDialog.SELECT_ACTION_1353) {
					return sendQuestDialog(env, 1353);
				}
				else if(dialog == QuestDialog.SELECT_ACTION_1438) {
					return sendQuestDialog(env, 1438);
				}
				else if(dialog == QuestDialog.STEP_TO_10) {
					giveQuestItem(env, 182204137, 1);
					changeQuestStep(env, 1, 10, false);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				}
				else if(dialog == QuestDialog.STEP_TO_20) {
					giveQuestItem(env, 182204138, 1);
					changeQuestStep(env, 1, 20, false);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				}
			}
			else if (targetId == 700238 && player.getInventory().getItemCountByItemId(182204136) < 3) {
				return true; //looting
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204339) {
				if (dialog == QuestDialog.USE_OBJECT) {
					if(qs.getQuestVarById(0) == 10) {
						removeQuestItem(env, 182204137, 1);
						return sendQuestDialog(env, 1693);
					}
					else if(qs.getQuestVarById(0) == 20) {
						rewardIndex = 1;
						removeQuestItem(env, 182204138, 1);
						return sendQuestDialog(env, 2034);
					}
				}
				return sendQuestEndDialog(env, rewardIndex);
			}
		}
	    return false;
	}	
}