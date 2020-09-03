package quest.inggison_armor;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author vlog
 * @modified Gigi
 */
public class _11053TheseShoesAreMadeForStalking extends QuestHandler {

	private final static int questId = 11053;

	public _11053TheseShoesAreMadeForStalking() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(799015).addOnQuestStart(questId);
		qe.registerQuestNpc(799015).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null || qs.canRepeat()) {
			if (targetId == 799015) {
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 799015) {
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 2375);
				}
				else if (dialog == QuestDialog.CHECK_COLLECTED_ITEMS) {
					long itemCount = player.getInventory().getItemCountByItemId(182206838);
					if (player.getInventory().tryDecreaseKinah(50000) && itemCount > 29) {
						player.getInventory().decreaseByItemId(182206838, 30);
						changeQuestStep(env, 0, 0, true);
						return sendQuestDialog(env, 5);
					}
					else
						return sendQuestDialog(env, 2716);
				}
				else if (dialog == QuestDialog.FINISH_DIALOG) {
					return defaultCloseDialog(env, 0, 0);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 799015)
				return sendQuestEndDialog(env);
		}
		return false;
	}
}