package quest.reshanta;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author vlog
 */
public class _2759TenaciousGuardian extends QuestHandler {

	private static final int questId = 2759;
	private final List<Integer> killedMobs = new ArrayList<Integer>();

	public _2759TenaciousGuardian() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(264769).addOnQuestStart(questId);
		qe.registerQuestNpc(264769).addOnTalkEvent(questId);
		qe.registerQuestNpc(278588).addOnKillEvent(questId);
		qe.registerQuestNpc(278589).addOnKillEvent(questId);
		qe.registerQuestNpc(278590).addOnKillEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 264769) { // Gudharten
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 264769) { // Gudharten
				if (dialog == QuestDialog.START_DIALOG) {
					if (var == 3) {
						return sendQuestDialog(env, 1352);
					}
				}
				else if (dialog == QuestDialog.SELECT_REWARD) {
					changeQuestStep(env, 3, 3, true); // reward
					killedMobs.clear();
					return sendQuestDialog(env, 5);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 264769) { // Gudharten
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var < 3) {
				switch (targetId) {
					case 278588: {
						if (!killedMobs.contains(278588)) {
							killedMobs.add(278588);
							return defaultOnKillEvent(env, 278588, var, var + 1);
						}
						break;
					}
					case 278589: {
						if (!killedMobs.contains(278589)) {
							killedMobs.add(278589);
							return defaultOnKillEvent(env, 278589, var, var + 1);
						}
						break;
					}
					case 278590: {
						if (!killedMobs.contains(278590)) {
							killedMobs.add(278590);
							return defaultOnKillEvent(env, 278590, var, var + 1);
						}
						break;
					}
				}
			}
		}
		return false;
	}
}