package quest.gelkmaros_armor;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author zhkchi
 * @reworked vlog
 */
public class _21051TroubleinStone extends QuestHandler {

	private final static int questId = 21051;

	public _21051TroubleinStone() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(799291).addOnQuestStart(questId);
		qe.registerQuestNpc(799291).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 799291) { // Aquila
				switch (dialog) {
					case START_DIALOG: {
						return sendQuestDialog(env, 1011);
					}
					default: {
						return sendQuestStartDialog(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 799291) { // Aquila
				switch (dialog) {
					case START_DIALOG: {
						return sendQuestDialog(env, 2375);
					}
					case CHECK_COLLECTED_ITEMS: {
						return checkQuestItems(env, 0, 0, true, 5, 2716); // reward
					}
					case FINISH_DIALOG: {
						return sendQuestSelectionDialog(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 799291) { // Aquila
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}