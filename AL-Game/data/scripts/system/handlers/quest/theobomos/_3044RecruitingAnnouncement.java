package quest.theobomos;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Balthazar
 */

public class _3044RecruitingAnnouncement extends QuestHandler {

	private final static int questId = 3044;

	public _3044RecruitingAnnouncement() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(730145).addOnQuestStart(questId);
		qe.registerQuestNpc(730145).addOnTalkEvent(questId);
		qe.registerQuestNpc(798206).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);

		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 730145) {
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 4762);
					}
					case STEP_TO_1: {
						QuestService.startQuest(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
						return true;
					}
					default:
						return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (targetId == 798206) {
				if (env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else if (env.getDialogId() == 1009)
					return defaultCloseDialog(env, 0, 1, true, true);
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 798206) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}