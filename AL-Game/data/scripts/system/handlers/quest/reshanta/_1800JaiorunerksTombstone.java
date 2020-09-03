package quest.reshanta;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Gigi
 * @reworked vlog
 */
public class _1800JaiorunerksTombstone extends QuestHandler {

	private final static int questId = 1800;

	public _1800JaiorunerksTombstone() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(279016).addOnQuestStart(questId);
		qe.registerQuestNpc(279016).addOnTalkEvent(questId);
		qe.registerQuestNpc(730141).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 279016) { // Vindachinerk
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env, 182202163, 1);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			switch (targetId) {
				case 730141: { // Jaiorunerk's Tomb
					switch (dialog) {
						case USE_OBJECT: {
							if (var == 0) {
								if (player.getInventory().getItemCountByItemId(182202163) > 0) {
									return sendQuestDialog(env, 1352);
								}
							}
						}
						case STEP_TO_1: {
							removeQuestItem(env, 182202163, 1);
							changeQuestStep(env, 0, 1, false);
							return closeDialogWindow(env);
						}
					}
					break;
				}
				case 279016: { // Vindachinerk
					switch (dialog) {
						case START_DIALOG: {
							if (var == 1) {
								return sendQuestDialog(env, 2375);
							}
						}
						case SELECT_REWARD: {
							changeQuestStep(env, 1, 1, true); // reward
							return sendQuestDialog(env, 5);
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 279016) { // Vindachinerk
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}