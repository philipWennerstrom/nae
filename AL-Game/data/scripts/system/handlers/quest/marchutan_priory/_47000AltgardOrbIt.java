package quest.marchutan_priory;

import com.aionemu.gameserver.configs.main.GroupConfig;
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
public class _47000AltgardOrbIt extends QuestHandler {

	private final static int questId = 47000;

	public _47000AltgardOrbIt() {
		super(questId);
	}

	public void register() {
		qe.addHandlerSideQuestDrop(questId, 700970, 182211033, 5, 100, 0);
		qe.registerQuestNpc(700970).addOnTalkEvent(questId);
		qe.registerQuestNpc(799864).addOnTalkEvent(questId);
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
			if (targetId == 700970) {
				if (player.isInGroup2()){
					PlayerGroup group = player.getPlayerGroup2();
					for (Player member : group.getMembers()) {
						if (member.isMentor() && MathUtil.getDistance(player, member) < GroupConfig.GROUP_MAX_DISTANCE)
							return true;
						else
							 PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DailyQuest_Ask_Mentor);
					}
				}
		  }
			if (targetId == 799864) {
				if (dialog == QuestDialog.START_DIALOG) {
					if(qs.getQuestVarById(0) == 0) {
						return sendQuestDialog(env, 2375);
					}
				}
				else if (dialog == QuestDialog.CHECK_COLLECTED_ITEMS) {
					return checkQuestItems(env, 0, 1, true, 5, 2716);
			  }
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 799864) { 
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