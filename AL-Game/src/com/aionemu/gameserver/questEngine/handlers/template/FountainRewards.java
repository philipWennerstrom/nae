package com.aionemu.gameserver.questEngine.handlers.template;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.*;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Wakizashi, vlog, Bobobear
 * @reworked Luzien
 */
public class FountainRewards extends QuestHandler {

	private final int questId;
	private final Set<Integer> startNpcs = new HashSet<Integer>();

	public FountainRewards(int questId, List<Integer> startNpcIds) {
		super(questId);
		this.questId = questId;
		this.startNpcs.addAll(startNpcIds);
		this.startNpcs.remove(0);
	}

	@Override
	public void register() {
		Iterator<Integer> iterator = startNpcs.iterator();
		while (iterator.hasNext()) {
			int startNpc = iterator.next();
			qe.registerQuestNpc(startNpc).addOnQuestStart(getQuestId());
			qe.registerQuestNpc(startNpc).addOnTalkEvent(getQuestId());
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (startNpcs.contains(targetId)) {  // Coin Fountain
				switch (dialog) {
					case USE_OBJECT: {
						if (!QuestService.inventoryItemCheck(env, true)) {
							return true;
						} else {
							if (targetId == 730241 || targetId == 730242) {
									return sendQuestDialog(env, 1011);
							} else {
								return sendQuestSelectionDialog(env);
							}
						}
					}
					case STEP_TO_1: {
						if (QuestService.collectItemCheck(env, false)) {
							if (!player.getInventory().isFullSpecialCube()) {
								if (QuestService.startQuest(env)) {
									changeQuestStep(env, 0, 0, true);
									return sendQuestDialog(env, 5);
								}
							}
							else {
								PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_FULL_INVENTORY);
								return sendQuestSelectionDialog(env);
							}
						}
						else {
							return sendQuestSelectionDialog(env);
						}
					}
				}
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			// Coin Fountain
			if (startNpcs.contains(targetId)) {  // Coin Fountain
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
		if (startNpcs.contains(env.getTargetId())) {  // Coin Fountain
			return true;
		}
		return false;
	}
}