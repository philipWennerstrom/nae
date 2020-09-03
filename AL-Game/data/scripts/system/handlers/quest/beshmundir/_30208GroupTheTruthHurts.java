package quest.beshmundir;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author Gigi
 */

public class _30208GroupTheTruthHurts extends QuestHandler {

	private final static int questId = 30208;

	public _30208GroupTheTruthHurts() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(798941).addOnQuestStart(questId);
		qe.registerQuestNpc(798941).addOnTalkEvent(questId);
		qe.registerQuestNpc(799506).addOnTalkEvent(questId);
		qe.registerQuestItem(182209610, questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();

		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 798941) {
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					if (giveQuestItem(env, 182209610, 1))
						return sendQuestDialog(env, 4762);
				}
				else
					return sendQuestStartDialog(env);

			}
		}

		if (qs == null)
			return false;

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 799506:
					switch (env.getDialog()) {
						case START_DIALOG:
							return sendQuestDialog(env, 1011);
						case SET_REWARD:
							env.getVisibleObject().getController().onDelete();
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return true;
					}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 798941)
				return sendQuestEndDialog(env);
		}
		return false;
	}

	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item) {
		final Player player = env.getPlayer();
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();

		if (id != 182209610)
			return HandlerResult.UNKNOWN;
		
		if (player.getWorldId() != 300170000) // TODO: Use zone instead of map
			return HandlerResult.UNKNOWN;
		
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
			return HandlerResult.UNKNOWN;

		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0,
			0), true);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0,
					1, 0), true);
				player.getInventory().decreaseByObjectId(itemObjId, 1);
				QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 799506, player.getX(), player.getY(),
					player.getZ(), player.getHeading());
			}
		}, 3000);
		return HandlerResult.SUCCESS;
	}
}