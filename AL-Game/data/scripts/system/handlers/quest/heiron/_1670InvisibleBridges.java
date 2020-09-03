package quest.heiron;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author Cheatkiller
 *
 */
public class _1670InvisibleBridges extends QuestHandler {

	private final static int questId = 1670;

	public _1670InvisibleBridges() {
		super(questId);
	}

	public void register() {
		qe.registerQuestItem(182201770, questId);
		qe.registerQuestNpc(203837).addOnTalkEvent(questId);
		qe.registerQuestNpc(212281).addOnAtDistanceEvent(questId);
		qe.registerQuestNpc(212282).addOnAtDistanceEvent(questId);
		qe.registerQuestNpc(212283).addOnAtDistanceEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 0) { 
				if (dialog == QuestDialog.ACCEPT_QUEST) {
					QuestService.startQuest(env);
					return closeDialogWindow(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 203837) {
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 2375);
				}
				else if (dialog == QuestDialog.SELECT_REWARD) {
						return defaultCloseDialog(env, 0, 1, true, true);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203837) {
				if (dialog == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 1352);
				}
				removeQuestItem(env, 182201770, 1);
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onAtDistanceEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			 targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
  		int var = qs.getQuestVarById(0);
  		if(targetId == 212281 && var == 0) {
  			changeQuestStep(env, 0, 16, false);
  			return true;
  		}
  		else if(targetId == 212282 && var == 16) {
  			changeQuestStep(env, 16, 48, false);
  			return true;
    	}
  		else if(targetId == 212283 && var == 48) {
  			changeQuestStep(env, 48, 48, true);
  			return true;
    	}
  	}
		return false;
	}

	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
				return HandlerResult.fromBoolean(sendQuestDialog(env, 4));
		}
		return HandlerResult.FAILED;
	}
}