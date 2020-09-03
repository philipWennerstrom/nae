package quest.sanctum;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Gigi
 */
public class _1937ALepharistMonstrosity extends QuestHandler {

	private final static int questId = 1937;

	public _1937ALepharistMonstrosity() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203833).addOnQuestStart(questId);
		qe.registerQuestNpc(203833).addOnTalkEvent(questId);
		qe.registerQuestNpc(203837).addOnTalkEvent(questId);
		qe.registerQuestNpc(203761).addOnTalkEvent(questId);
		qe.registerQuestNpc(204573).addOnTalkEvent(questId);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 1936);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			if (targetId == 203833) {
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 1011);
				else
					return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 203837: {
					if (qs.getQuestVarById(0) == 0) {
						if (env.getDialog() == QuestDialog.START_DIALOG)
							return sendQuestDialog(env, 1352);
						else if (env.getDialog() == QuestDialog.STEP_TO_1) {
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					}
				}
					break;
				case 203761: {
					if (qs.getQuestVarById(0) == 1) {
						if (env.getDialog() == QuestDialog.START_DIALOG)
							return sendQuestDialog(env, 1693);
						else if (env.getDialog() == QuestDialog.STEP_TO_2) {
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					}
				}
					break;
				case 203833: {
					if (qs.getQuestVarById(0) == 2) {
						if (env.getDialog() == QuestDialog.START_DIALOG)
							return sendQuestDialog(env, 2034);
						else if (env.getDialog() == QuestDialog.STEP_TO_3) {
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					}
				}
					break;
				case 204573: {
					if (qs.getQuestVarById(0) == 3) {
						if (env.getDialog() == QuestDialog.START_DIALOG)
							return sendQuestDialog(env, 2375);
						else if (env.getDialogId() == 1009) {
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return sendQuestEndDialog(env);
						}
						else
							return sendQuestEndDialog(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204573)
				return sendQuestEndDialog(env);
		}
		return false;
	}
}