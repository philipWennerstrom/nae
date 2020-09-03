package quest.crafting;

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
 * @author Thuatan
 */
public class _29020MasterTailorsPotential extends QuestHandler {

	private final static int questId = 29020;

	public _29020MasterTailorsPotential() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(204110).addOnQuestStart(questId);
		qe.registerQuestNpc(204110).addOnTalkEvent(questId);
		qe.registerQuestNpc(204111).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204110) {
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 4762);
				else
					return sendQuestStartDialog(env);
			}
		}

		if (qs == null)
			return false;

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 204111:
					switch (env.getDialog()) {
						case START_DIALOG:
							return sendQuestDialog(env, 1011);
						case STEP_TO_10:
							if (!giveQuestItem(env, 152206964, 1))
								return true;
							qs.setQuestVarById(0, 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						case STEP_TO_20:
							if (!giveQuestItem(env, 152206965, 1))
								return true;
							qs.setQuestVarById(0, 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
					}
				case 204110:
					switch (env.getDialog()) {
						case START_DIALOG:
							long itemCount1 = player.getInventory().getItemCountByItemId(182207900);
							if (itemCount1 > 0) {
								removeQuestItem(env, 182207900, 1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return sendQuestDialog(env, 1352);
							}
							else
								return sendQuestDialog(env, 10001);
					}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204110) {
				if (env.getDialogId() == 34)
					return sendQuestDialog(env, 5);
				else
					return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}