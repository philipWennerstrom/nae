package quest.tiamaranta;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.*;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Luzien
 * 
 */
public class _41598MakingBalaurLuck extends QuestHandler {

	private final static int questId = 41598;

	public _41598MakingBalaurLuck() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(730555).addOnQuestStart(questId);
		qe.registerQuestNpc(730555).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 730555) {
				switch (dialog) {
					case USE_OBJECT: {
						if (!QuestService.inventoryItemCheck(env, true)) {
							return true;
						}
						else return sendQuestDialog(env, 1011);
					}
					case ACCEPT_QUEST_SIMPLE: {
						if (!player.getInventory().isFullSpecialCube()) {
							if (QuestService.startQuest(env)) {
								return sendQuestDialog(env, 2375);
							}
						}
						else {
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_FULL_INVENTORY);
							return sendQuestSelectionDialog(env);
						}
					}	
				}
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (targetId == 730555) {
				if (dialog.equals(QuestDialog.CHECK_COLLECTED_ITEMS_SIMPLE)) {
					if (QuestService.collectItemCheck(env, false)) {
						changeQuestStep(env, 0, 0, true);
						return sendQuestDialog(env, 5);
					}
				}
				return sendQuestDialog(env, 2375);
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 730555) {
				if (dialog == QuestDialog.SELECT_NO_REWARD) {
					if (QuestService.collectItemCheck(env, true))
						return sendQuestEndDialog(env);
				}
				else {
					return QuestService.abandonQuest(player, questId);
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onCanAct(QuestEnv env, QuestActionType questEventType, Object... objects) {
		return env.getTargetId() == 730555;
	}
}