package quest.heiron;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Nephis and quest helper team
 */
public class _1537FishOnTheLine extends QuestHandler {

	private final static int questId = 1537;

	public _1537FishOnTheLine() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(204588).addOnQuestStart(questId);
		qe.registerQuestNpc(204588).addOnTalkEvent(questId);
		qe.registerQuestNpc(730189).addOnTalkEvent(questId);
		qe.registerQuestNpc(730190).addOnTalkEvent(questId);
		qe.registerQuestNpc(730191).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204588) {
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 1011);
				else
					return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 730189: {
					if (env.getDialog() == QuestDialog.USE_OBJECT) {
						return useQuestObject(env, 0, 1, false, 0); // 1
					}
					break;
				}
				case 730190: {
					if (env.getDialog() == QuestDialog.USE_OBJECT) {
						return useQuestObject(env, 1, 2, false, 0); // 2
					}
					break;
				}
				case 730191: {
					if (qs.getQuestVarById(0) == 2 && env.getDialog() == QuestDialog.USE_OBJECT) {
						qs.setQuestVarById(0, 3);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return true;
					}
				}
			}
		}

		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204588)
				return sendQuestEndDialog(env);
		}
		return false;
	}
}