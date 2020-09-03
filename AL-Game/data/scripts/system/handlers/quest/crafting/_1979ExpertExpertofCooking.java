package quest.crafting;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Gigi
 */
public class _1979ExpertExpertofCooking extends QuestHandler {

	private final static int questId = 1979;

	public _1979ExpertExpertofCooking() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203784).addOnQuestStart(questId);
		qe.registerQuestNpc(203784).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203784) {
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 1011);
				else
					return sendQuestStartDialog(env);
			}
		}

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 203784: {
					switch (env.getDialog()) {
						case START_DIALOG: {
							long itemCount1 = player.getInventory().getItemCountByItemId(182206902);
							long itemCount2 = player.getInventory().getItemCountByItemId(182206903);
							long itemCount3 = player.getInventory().getItemCountByItemId(182206904);
							long itemCount4 = player.getInventory().getItemCountByItemId(182206905);
							if (itemCount1 > 0 && itemCount2 > 0 && itemCount3 > 0 && itemCount4 > 0) {
								removeQuestItem(env, 182206902, 1);
								removeQuestItem(env, 182206903, 1);
								removeQuestItem(env, 182206904, 1);
								removeQuestItem(env, 182206905, 1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return sendQuestDialog(env, 2375);
							}
							else
								return sendQuestDialog(env, 2716);
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203784) {
				if (env.getDialogId() == 34)
					return sendQuestDialog(env, 5);
				else
					return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}