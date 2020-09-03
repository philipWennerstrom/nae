package quest.fenris_fang;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Cheatkiller
 */
public class _29064FangOfConstruction extends QuestHandler {

	private final static int questId = 29064;

	public _29064FangOfConstruction() {
		super(questId);
	}

	@Override
	public void register() {
		int[] npcs = { 798452, 204075, 204053 };
		qe.registerQuestNpc(204053).addOnQuestStart(questId);
		for (int npc : npcs)
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204053) {
				if (dialog == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 1011);
				else
					return sendQuestStartDialog(env);
			}
		}

		if (qs == null)
			return false;

		int var = qs.getQuestVarById(0);

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 798452:
					switch (dialog) {
						case START_DIALOG:
							return sendQuestDialog(env, 1352);
						case STEP_TO_1:
							return defaultCloseDialog(env, 0, 1); // 1
					}
					break;
				case 204075:
					switch (dialog) {
						case START_DIALOG: {
							if (var == 1 && player.getInventory().getItemCountByItemId(182213239) >= 1 && player.getInventory().getItemCountByItemId(186000085) >= 1) {
								return sendQuestDialog(env, 2375);
							}
						}
						case CHECK_COLLECTED_ITEMS_SIMPLE: {
							if (player.getInventory().getItemCountByItemId(182213239) >= 1 && player.getInventory().getItemCountByItemId(186000085) >= 1) {
								removeQuestItem(env, 182213239, 1);
								removeQuestItem(env, 186000085, 1);
								return defaultCloseDialog(env, 1, 1, true, false);
							}
						}
				  }
					break;
			 }
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204075) {
				if (dialog == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 5);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}