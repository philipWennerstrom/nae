package quest.brusthonin;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Nephis
 */
public class _4033ABloominBrusthonin extends QuestHandler {

	private final static int questId = 4033;

	public _4033ABloominBrusthonin() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(205155).addOnQuestStart(questId); // Heintz
		qe.registerQuestNpc(205155).addOnTalkEvent(questId);
		qe.registerQuestNpc(700379).addOnTalkEvent(questId); // Portaro's Tomb
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (targetId == 205155) // Heintz
		{
			if (qs == null || qs.getStatus() == QuestStatus.NONE) {
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 1011);
				else
					return sendQuestStartDialog(env);
			}

			else if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) {
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 1352);
				else if (env.getDialogId() == 34) {
					if (QuestService.collectItemCheck(env, true)) {
						qs.setQuestVarById(0, qs.getQuestVarById(0) + 2);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					else
						return sendQuestDialog(env, 1438);
				}
				else
					return sendQuestStartDialog(env);
			}

			else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 2034);
				else if (env.getDialogId() == 1009) {
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return sendQuestEndDialog(env);
				}
				else
					return sendQuestEndDialog(env);
			}
		}

		else if (qs != null && qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 700379: { // Portaro's Tomb
					if (qs.getQuestVarById(0) == 2 && env.getDialog() == QuestDialog.USE_OBJECT) {
						return useQuestObject(env, 2, 2, true, false); // reward
					}
				}
			}
		}
		return false;
	}
}