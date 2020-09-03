package quest.udas_temple;

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
public class _30111CenterOfTheWeb extends QuestHandler {

	private final static int questId = 30111;

	public _30111CenterOfTheWeb() {
		super(questId);
	}

	public void register() {
		qe.registerQuestNpc(799335).addOnQuestStart(questId);
		qe.registerQuestNpc(799335).addOnTalkEvent(questId);
		qe.registerQuestNpc(206105).addOnAtDistanceEvent(questId);
		qe.registerQuestNpc(215792).addOnKillEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 799335) { 
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
				}
				else
					return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 799335) {
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1352);
				}
				else if (dialog == QuestDialog.SELECT_REWARD) {
					return defaultCloseDialog(env, 2, 2, true, true);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 799335) {
				if (dialog == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 5);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env) {
		return defaultOnKillEvent(env, 215792, 1, 2);
	}
	
	@Override
	public boolean onAtDistanceEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
  		int var = qs.getQuestVarById(0);
  		if(var == 0) {
  			changeQuestStep(env, 0, 1, false);
  			return true;
  		}
		}
		return false;
	}
}