package ai.instance.crucibleChallenge;

import ai.ActionItemNpcAI2;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 *
 * @author xTz
 */
@AIName("cruciblerift")
public class CrucibleRiftAI2 extends ActionItemNpcAI2 {

	@Override
	protected void handleUseItemFinish(Player player) {
		switch (getNpcId()) {
			case 730459:
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
				break;
			case 730460:
				TeleportService2.teleportTo(player, 300320000, getPosition().getInstanceId(), 1759.5004f, 1273.5414f, 389.11743f, (byte) 10);
				spawn(205679, 1765.522f, 1282.1051f, 389.11743f, (byte) 0);
				AI2Actions.deleteOwner(this);
				break;
		}
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		if (getNpcId() == 730459) {
			announceRift();
		}
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		if (dialogId == 10000 && getNpcId() == 730459) {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
			TeleportService2.teleportTo(player, 300320000, getPosition().getInstanceId(), 1759.5946f, 1768.6449f, 389.11758f, (byte) 16);
			spawn(218190, 1760.8701f, 1774.7711f, 389.11743f, (byte) 110);
			spawn(218185, 1762.6906f, 1773.863f, 389.11743f, (byte) 80);
			spawn(218191, 1763.9441f, 1775.2466f, 389.1175f, (byte) 80);
			AI2Actions.deleteOwner(this);
		}
		return true;
	}

	private void announceRift() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(false, 1111482, player.getObjectId(), 2));
				}
			}
		});
	}
}