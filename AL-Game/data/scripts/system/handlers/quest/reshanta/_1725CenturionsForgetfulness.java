package quest.reshanta;

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
 * @author Hilgert
 */
public class _1725CenturionsForgetfulness extends QuestHandler {

	private final static int questId = 1725;

	public _1725CenturionsForgetfulness() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(278520).addOnQuestStart(questId);
		qe.registerQuestNpc(278520).addOnTalkEvent(questId);
		qe.registerQuestNpc(278514).addOnTalkEvent(questId);
		qe.registerQuestNpc(278590).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 278520) {
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
				}
				else if (env.getDialogId() == 1002) {
					if (QuestService.startQuest(env)) {
						if (giveQuestItem(env, 182202153, 1))
							return sendQuestDialog(env, 1003);
						else
							return true;
					}
				}
				else
					return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 278514) {
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 1352);
				else if (env.getDialog() == QuestDialog.STEP_TO_1) {
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
			}
			else if (targetId == 278590) {
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 2375);
				else if (env.getDialogId() == 1009) {
					removeQuestItem(env, 182202153, 1);
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return sendQuestEndDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD && targetId == 278590) {
			return sendQuestEndDialog(env);
		}
		return false;
	}
}