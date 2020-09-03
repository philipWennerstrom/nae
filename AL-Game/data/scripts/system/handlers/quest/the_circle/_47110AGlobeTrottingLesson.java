package quest.the_circle;

import com.aionemu.gameserver.configs.main.GroupConfig;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Cheatkiller
 *
 */
public class _47110AGlobeTrottingLesson extends QuestHandler {

	private final static int questId = 47110;

	public _47110AGlobeTrottingLesson() {
		super(questId);
	}

	public void register() {
		qe.registerQuestNpc(700971).addOnTalkEvent(questId);
		qe.registerQuestNpc(799921).addOnTalkEvent(questId);
		qe.registerQuestNpc(217174).addOnKillEvent(questId);
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env) {
		return defaultOnKillEvent(env, 217174, 0, 5);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		
		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 0) { 
				if (dialog == QuestDialog.ACCEPT_QUEST) {
					QuestService.startQuest(env);
					return closeDialogWindow(env);
				}
			}
		}

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (targetId == 700971) {
				if (player.isInGroup2()){
					PlayerGroup group = player.getPlayerGroup2();
					for (Player member : group.getMembers()) {
						if (player.isMentor() && MathUtil.getDistance(player, member) < GroupConfig.GROUP_MAX_DISTANCE) {
							Npc npc = (Npc) env.getVisibleObject();
							npc.getController().scheduleRespawn();
							npc.getController().onDelete();
							QuestService.addNewSpawn(npc.getWorldId(), npc.getInstanceId(), 217174, npc.getX(), npc.getY(), npc.getZ(), (byte) 0);
							return true;
						}
						else
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DailyQuest_Ask_Mentee);
					}
				}
		    }
			if (targetId == 799921) {
				if (dialog == QuestDialog.START_DIALOG) {
					if(qs.getQuestVarById(0) == 5) {
						return sendQuestDialog(env, 1352);
					}
				}
				else if (dialog == QuestDialog.SELECT_REWARD) {
					return defaultCloseDialog(env, 5, 5, true, true);
			    }
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 799921) { 
				if (dialog == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 5);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}