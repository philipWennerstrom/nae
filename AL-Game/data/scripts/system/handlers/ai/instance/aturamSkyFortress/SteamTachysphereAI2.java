package ai.instance.aturamSkyFortress;

import ai.ActionItemNpcAI2;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 *
 * @author xTz
 */
@AIName("steam_tachysphere")
public class SteamTachysphereAI2 extends ActionItemNpcAI2 {
	
	@Override
	protected void handleUseItemFinish(Player player) {
		final QuestState qs = player.getQuestStateList().getQuestState(player.getRace().equals(Race.ELYOS) ? 18302 : 28302);
		if (qs == null) {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 27));
		}
		else if (qs != null && qs.getStatus() != QuestStatus.COMPLETE) {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 10));
		}
		else {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
		}
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		if (dialogId == 10000) {
			final QuestState qs = player.getQuestStateList().getQuestState(player.getRace().equals(Race.ELYOS) ? 18302 : 28302);
			if (qs != null && qs.getStatus() == QuestStatus.COMPLETE) {
				TeleportService2.teleportTo(player, 300240000, 175.28925f, 625.1088f, 901.009f, (byte) 33);
				PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 0, 471, 16777216));
				player.getController().stopProtectionActiveTask();
				SkillEngine.getInstance().getSkill(player, 19502, 1, player).useNoAnimationSkill();
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
			}
		}
		return true;
	}
}