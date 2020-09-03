package quest.aturam_sky_fortress;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Luzien
 */
public class _18302FirstPriority extends QuestHandler {

	private final static int questId = 18302;
	private int[] mobIds = new int[] {700981, 700982, 700983, 700984, 700985};
	
	public _18302FirstPriority() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(799530).addOnQuestStart(questId);
		qe.registerQuestNpc(799530).addOnTalkEvent(questId);
		qe.registerQuestNpc(730375).addOnTalkEvent(questId);
		for (int id : mobIds) {
			qe.registerQuestNpc(id).addOnKillEvent(questId);
		}
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 799530) {
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 4762);
				}
				else if (env.getDialog() == QuestDialog.ACCEPT_QUEST){
					 playQuestMovie(env, 468);
					 return sendQuestStartDialog(env);
				}
				else
					return sendQuestStartDialog(env);
			}
		}
		
		if (qs == null)
			return false;
		
		int var = qs.getQuestVarById(0);

		if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 730375) {
				if (var == 5) {
					switch (env.getDialog()) {
						case USE_OBJECT:
							return sendQuestDialog(env, 1352);
						case SET_REWARD:
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						default:
							return sendQuestDialog(env, 2716);
					}
				}
			}
			else if (targetId == 799530) {
				return sendQuestDialog(env, 1004);
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 799530) {
				if (env.getDialog() == QuestDialog.USE_OBJECT) 
					return sendQuestDialog(env, 10002);
				if (env.getDialog() == QuestDialog.SELECT_REWARD) {
					removeQuestItem(env,182212101,1);
					return sendQuestDialog(env, 5);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		}

		int targetId = 0;
		int var = qs.getQuestVarById(0);

		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		if (var > 4)
			return false;
		
		for (int id : mobIds) {
			if (targetId == id) {
				qs.setQuestVarById(0, var+1);
                updateQuestStatus(env);
                return true;
            }
		}
		return false;
	}
}