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
 * @author Rolandas
 */
public class _3969SexiestManAlive extends QuestHandler {

	private final static int questId = 3969;

	public _3969SexiestManAlive() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(798390).addOnQuestStart(questId);// Palentine
		qe.registerQuestNpc(798391).addOnTalkEvent(questId);// Andu
		qe.registerQuestNpc(798390).addOnTalkEvent(questId);// Palentine
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;

		QuestState qs2 = player.getQuestStateList().getQuestState(3968);
		if (qs2 == null || qs2.getStatus() != QuestStatus.COMPLETE)
			return false;

		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (targetId == 798390)// Palentine
		{
			if (qs == null || qs.getStatus() == QuestStatus.NONE) {
				if (env.getDialog() == QuestDialog.USE_OBJECT)
					return sendQuestDialog(env, 1011);
				else if (env.getDialogId() == 1002) {
					if (giveQuestItem(env, 182206126, 1)) {
						return sendQuestStartDialog(env);
					}
					return true;
				}
				else
					return sendQuestStartDialog(env);
			}
		}

		if (qs == null)
			return false;

		int var = qs.getQuestVarById(0);

		if (targetId == 798391)// Andu
		{
			if (qs.getStatus() == QuestStatus.START && var == 0) {
				if (env.getDialog() == QuestDialog.USE_OBJECT)
					return sendQuestDialog(env, 1352);
				else if (env.getDialog() == QuestDialog.STEP_TO_1) {
					if (player.getInventory().getItemCountByItemId(182206126) > 0) {
						removeQuestItem(env, 182206126, 1);
						qs.setQuestVar(++var);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					}
					return true;
				}
				else
					return sendQuestStartDialog(env);
			}
		}
		else if (targetId == 798390)// Palentine
		{
			if (qs.getStatus() == QuestStatus.REWARD) {
				if (env.getDialog() == QuestDialog.USE_OBJECT)
					return sendQuestDialog(env, 2375);
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}