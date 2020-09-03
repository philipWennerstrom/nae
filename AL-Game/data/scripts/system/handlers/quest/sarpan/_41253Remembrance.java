package quest.sarpan;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author Cheatkiller
 *
 */
public class _41253Remembrance extends QuestHandler {

	private final static int questId = 41253;

	public _41253Remembrance() {
		super(questId);
	}

	public void register() {
		qe.registerQuestNpc(205787).addOnQuestStart(questId);
		qe.registerQuestNpc(205787).addOnTalkEvent(questId);
		qe.registerQuestNpc(205572).addOnTalkEvent(questId);
		qe.registerQuestNpc(730487).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 205787) { 
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 205572) {
				if (dialog == QuestDialog.START_DIALOG) {
					if(qs.getQuestVarById(0) == 0) {
						return sendQuestDialog(env, 1011);
					}
					else if(qs.getQuestVarById(0) == 1) {
						return sendQuestDialog(env, 1352);
					}
				}
				else if (dialog == QuestDialog.CHECK_COLLECTED_ITEMS_SIMPLE) {
					return checkQuestItems(env, 1, 2, false, 10000, 10001, 182213107, 1); 
				}
				else if (dialog == QuestDialog.STEP_TO_1) {
					return defaultCloseDialog(env, 0, 1);
				}
			}
			else if (targetId == 730487) {
				if (dialog == QuestDialog.USE_OBJECT) {
					if(qs.getQuestVarById(0) == 2) {
						return sendQuestDialog(env, 1693);
					}
				}
				else if (dialog == QuestDialog.SET_REWARD) {
					removeQuestItem(env, 182213107, 1);
					QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 701417, player.getX(), player.getY(), player.getZ(), (byte) 0);
					return defaultCloseDialog(env, 2, 2, true, false);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 205787) {
				if (dialog == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}