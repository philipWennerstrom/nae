package quest.empyrean_crucible;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Cheatkiller
 *
 */
public class _18213TheChillingTruth extends QuestHandler {

	private final static int questId = 18213;
	
	public _18213TheChillingTruth() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestItem(182212220, questId);
		qe.registerQuestNpc(205985).addOnQuestStart(questId);
		qe.registerQuestNpc(205985).addOnTalkEvent(questId);
		qe.registerQuestNpc(205316).addOnTalkEvent(questId);
		qe.registerQuestNpc(798604).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		QuestDialog dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 205985) {
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
				}
				else {
					giveQuestItem(env, 182212219, 1);
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 205316) {
				switch (dialog) {
					case START_DIALOG: {
						return sendQuestDialog(env, 1352);
					}
					case SELECT_ACTION_1353: {
						removeQuestItem(env, 182212219, 1);
						giveQuestItem(env, 182212220, 1);
						return sendQuestDialog(env, 1353);
					}
					case STEP_TO_1: {
						return defaultCloseDialog(env, 0, 1);
					}
				}
			}
			else if (targetId == 798604) {
				switch (dialog) {
					case START_DIALOG: {
						if(var == 1){
						return sendQuestDialog(env, 1693);
						}
						else if(var == 3){
							return sendQuestDialog(env, 2375);
						}
					}
					case STEP_TO_2: {
						return defaultCloseDialog(env, 1, 2);
					}
					case SELECT_REWARD: {
						changeQuestStep(env, 3, 3, true);
						return sendQuestDialog(env, 5);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 798604) { 
				if (dialog == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 2375);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
	
	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 2) {
			removeQuestItem(env, 182212220, 1);
				changeQuestStep(env, 2, 3, false);
				return HandlerResult.SUCCESS;
		}
		return HandlerResult.FAILED;
	}
}