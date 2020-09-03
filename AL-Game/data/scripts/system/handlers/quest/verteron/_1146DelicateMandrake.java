package quest.verteron;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author Mr.Poke, Dune11
 * @modified xTz, Undertrey
 * @reworked vlog
 */
public class _1146DelicateMandrake extends QuestHandler {

	private final static int questId = 1146;

	public _1146DelicateMandrake() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203123).addOnQuestStart(questId);
		qe.registerQuestNpc(203123).addOnTalkEvent(questId);
		qe.registerQuestNpc(203139).addOnTalkEvent(questId);
		qe.registerOnQuestTimerEnd(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203123) { // Gano
				switch (dialog) {
					case START_DIALOG: {
						return sendQuestDialog(env, 1011);
					}
					case ASK_ACCEPTION: {
						return sendQuestDialog(env, 4);
					}
					case ACCEPT_QUEST: {
						if (giveQuestItem(env, 182200519, 1)) {
							if (QuestService.startQuest(env)) {
								QuestService.questTimerStart(env, 120);
								return sendQuestDialog(env, 1003);
							}
						}
					}
					case REFUSE_QUEST: {
						return sendQuestDialog(env, 1004);
					}
					case FINISH_DIALOG: {
						return sendQuestSelectionDialog(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 203139) { // Krodis
				switch (dialog) {
					case USE_OBJECT: {
						if (player.getInventory().getItemCountByItemId(182200519) > 0) {
							return sendQuestDialog(env, 2375);
						}
					}
					case SELECT_REWARD: {
						removeQuestItem(env, 182200519, 1);
						changeQuestStep(env, 0, 0, true);
						QuestService.questTimerEnd(env);
						return sendQuestDialog(env, 5);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203139) { // Krodis
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onQuestTimerEndEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			removeQuestItem(env, 182200519, 1);
			QuestService.abandonQuest(player, questId);
			player.getController().updateNearbyQuests();
			return true;
		}
		return false;
	}
}