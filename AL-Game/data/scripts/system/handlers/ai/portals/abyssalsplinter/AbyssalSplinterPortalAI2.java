package ai.portals.abyssalsplinter;

import ai.ActionItemNpcAI2;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.teleport.TeleportService2;

/**
 * @author Ritsu
 *
 */
@AIName("teleportation_device")
public class AbyssalSplinterPortalAI2 extends ActionItemNpcAI2
{
	@Override
	protected void handleUseItemFinish(Player player)
	{
		Npc npc =getOwner();
		if(npc.getX() == 302.201f)
			TeleportService2.teleportTo(player, 300220000, 294.632f, 732.189f, 215.854f);
		else if(npc.getX() == 334.001f)
			TeleportService2.teleportTo(player, 300220000, 338.475f, 701.417f, 215.916f);
		else if(npc.getX() == 362.192f)
			TeleportService2.teleportTo(player, 300220000, 373.611f, 739.125f, 215.903f);
	}
}