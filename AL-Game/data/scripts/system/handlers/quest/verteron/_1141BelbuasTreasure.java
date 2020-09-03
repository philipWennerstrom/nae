package quest.verteron;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Mr. Poke, Dune11
 * @reworked vlog
 */
public class _1141BelbuasTreasure extends QuestHandler {

	private final static int questId = 1141;

	public _1141BelbuasTreasure() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(730001).addOnQuestStart(questId);
		qe.registerQuestNpc(730001).addOnTalkEvent(questId);
		qe.registerQuestNpc(700122).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 730001) { // Nola
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 700122) { // Belbua's Wine Barrel
				if (env.getDialog() == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 2375);
				}
				else if (env.getDialog() == QuestDialog.SELECT_REWARD) {
					changeQuestStep(env, 0, 0, true); // reward
					return sendQuestDialog(env, 5);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 700122) { // Belbua's Wine Barrel
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}