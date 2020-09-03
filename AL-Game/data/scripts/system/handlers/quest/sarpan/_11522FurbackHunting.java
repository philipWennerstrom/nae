package quest.sarpan;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author zhkchi
 */
public class _11522FurbackHunting extends QuestHandler {

	private static final int questId = 11522;

	public _11522FurbackHunting() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(799532).addOnQuestStart(questId);
		qe.registerQuestNpc(799532).addOnTalkEvent(questId);
		qe.registerOnKillInWorld(600020000, questId);
	}

	@Override
	public boolean onKillInWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		if (env.getVisibleObject() instanceof Player && player != null && player.isInsideZone(ZoneName.get("HEROS_DISCUS_600020000"))) {
			if ((env.getPlayer().getLevel() >= (((Player)env.getVisibleObject()).getLevel() - 5)) && (env.getPlayer().getLevel() <= (((Player)env.getVisibleObject()).getLevel() + 9))) {
				return defaultOnKillRankedEvent(env, 0, 5, true); // reward
			}
		}
		return false;
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 799532) {
				switch (env.getDialog()) {
					case START_DIALOG:
						return sendQuestDialog(env, 4762);
					default:
						return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 799532) {
				switch (env.getDialog()) {
					case USE_OBJECT:
						return sendQuestDialog(env, 10002);
					case SELECT_REWARD:
						return sendQuestDialog(env, 5);
					default:
						return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}