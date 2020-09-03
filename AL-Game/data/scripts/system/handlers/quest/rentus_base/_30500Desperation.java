package quest.rentus_base;

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
public class _30500Desperation extends QuestHandler{

	private static final int questId = 30500;

	public _30500Desperation() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(205842).addOnQuestStart(questId);
		qe.registerQuestNpc(799549).addOnTalkEvent(questId);
		qe.registerQuestNpc(799544).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 205842) {
				switch (dialog) {
					case START_DIALOG:{
						return sendQuestDialog(env, 1011);
					}
					default:
						return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (targetId == 799549) {
				switch (dialog) {
					case START_DIALOG:{
						return sendQuestDialog(env, 1352);
					}
					case STEP_TO_1:{
						changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
				}
			}
			else if(targetId == 799544){
				switch (dialog) {
					case START_DIALOG:{
						return sendQuestDialog(env, 2375);
					}
					case SELECT_REWARD:{
						changeQuestStep(env, 1, 1, true);
						return closeDialogWindow(env);
					}
				}
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			if(targetId == 799544){
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}