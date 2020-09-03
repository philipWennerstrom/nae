package quest.pernon;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.MathUtil;

/**
 * @author Bobobear
 */
public class _51009TheGiftOfCharity extends QuestHandler {

	private final static int questId = 51009;

	public _51009TheGiftOfCharity() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(831033).addOnQuestStart(questId);
		qe.registerQuestNpc(831039).addOnQuestStart(questId);
		qe.registerQuestNpc(831033).addOnTalkEvent(questId);
		qe.registerQuestNpc(831039).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();

		if(qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 831033 || targetId == 831039) {
				switch (dialog) {
					case START_DIALOG: {
						return sendQuestDialog(env, 1011);
					}
					case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE:
						return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 831033 || targetId == 831039) {
				changeQuestStep(env, 0, 0, true); // reward
				return sendQuestDialog(env, 2375);
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 831033 || targetId == 831039)
				return sendQuestEndDialog(env);
		}
		return false;
	}
}