package quest.altgard;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author Mr. Poke
 */
public class _2017TrespassersattheObservatory extends QuestHandler {

	private final static int questId = 2017;

	public _2017TrespassersattheObservatory() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(203654).addOnTalkEvent(questId);
		qe.registerQuestNpc(210528).addOnKillEvent(questId);
		qe.registerQuestNpc(210721).addOnKillEvent(questId);
		qe.registerQuestNpc(203558).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
			return false;

		final int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 203654:
					switch (env.getDialog()) {
						case START_DIALOG:
							if (var == 0)
								return sendQuestDialog(env, 1011);
							else if (var == 6)
								return sendQuestDialog(env, 1352);
							else if (var == 7)
								return sendQuestDialog(env, 1693);
							break;
						case STEP_TO_1:
						case STEP_TO_2:
							if (var == 0 || var == 6) {
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								return sendQuestSelectionDialog(env);
							}
							break;
						case CHECK_COLLECTED_ITEMS:
							if (var == 7) {
								if (QuestService.collectItemCheck(env, true)) {
									qs.setStatus(QuestStatus.REWARD);
									updateQuestStatus(env);
									return sendQuestDialog(env, 1694);
								}
								else
									return sendQuestDialog(env, 1779);
							}
					}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203558) {
				if (env.getDialog() == QuestDialog.USE_OBJECT)
					return sendQuestDialog(env, 2034);
				else
					return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START)
			return false;

		int targetId = 0;
		int var = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		switch (targetId) {
			case 210528:
			case 210721:
				var = qs.getQuestVarById(0);
				if (var < 6) {
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
				}
				break;
		}
		return false;
	}

	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 2200, true);
	}
}