package ai.instance.aturamSkyFortress;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;


/**
 * @author cheatkiller
 *
 */
@AIName("hariken_supply_chest")
public class HarikenSupplyChestAI2 extends NpcAI2 {

	@Override
	protected void handleDialogStart(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		if (dialogId == 10000) {
			addItems(player);
		}
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
		return true;
	}
	
	private void addItems(Player player){
		Item BottomlessBucket = player.getInventory().getFirstItemByItemId(164000202);
		Item TalonSummoningDevice = player.getInventory().getFirstItemByItemId(164000163);
	  if (TalonSummoningDevice == null && BottomlessBucket == null) {
	  	ItemService.addItem(player, 164000163, 1);
			ItemService.addItem(player, 164000202, 1);
	    }
	}
}