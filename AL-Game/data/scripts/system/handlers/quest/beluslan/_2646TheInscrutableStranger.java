package quest.beluslan;

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
 * @author Ritsu
 * 
 */
public class _2646TheInscrutableStranger extends QuestHandler
{
	private final static int	questId	= 2646;

	public _2646TheInscrutableStranger()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.registerQuestNpc(204817).addOnQuestStart(questId);
		qe.registerQuestNpc(204817).addOnTalkEvent(questId);
		qe.registerQuestNpc(204777).addOnTalkEvent(questId);
		qe.registerQuestNpc(204700).addOnTalkEvent(questId);
		qe.registerQuestNpc(204702).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		
		if(qs == null || qs.getStatus() == QuestStatus.NONE){
			if (targetId == 204817){
				if (dialog == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 1011);
				else 
					return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START){
			int var = qs.getQuestVarById(0);
			if (targetId == 204817){
				switch (dialog){
					case START_DIALOG:
						if (var == 3){
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return sendQuestDialog(env, 2375);
						}
					case SELECT_REWARD:
						if (var == 3)
							return sendQuestDialog(env, 5);
				}
			}
			if (targetId == 204777){
				switch (dialog){
					case START_DIALOG:
						if (var == 0)
							return sendQuestDialog(env, 1352);
					case STEP_TO_1:
						if (var == 0){
							if (!giveQuestItem(env, 182204515, 1))
								return true;
							if (!giveQuestItem(env, 182204516, 1))
								return true;
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
							PacketSendUtility
								.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
				}
			}
			if (targetId == 204700){
				switch (dialog){
					case START_DIALOG:
						if (var == 1)
							return sendQuestDialog(env, 1693);
					case STEP_TO_2:
						if (var == 1){
							removeQuestItem(env, 182204515, 1);
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
							PacketSendUtility
								.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
				}
			}
			if (targetId == 204702){
				switch (dialog){
					case START_DIALOG:
						if (var == 2)
							return sendQuestDialog(env, 2034);
					case STEP_TO_3:
						if (var == 2){
							removeQuestItem(env, 182204516, 1);
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
							PacketSendUtility
								.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD){
			if (targetId == 204817){
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}