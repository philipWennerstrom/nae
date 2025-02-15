package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.ingameshop.IGItem;
import com.aionemu.gameserver.model.ingameshop.InGameShopEn;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author xTz, KID
 */
public class SM_IN_GAME_SHOP_ITEM extends AionServerPacket {

	private IGItem item;
	public SM_IN_GAME_SHOP_ITEM(Player player, int objectItem) {
		item = InGameShopEn.getInstance().getIGItem(objectItem);
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(item.getObjectId());
		writeQ(item.getItemPrice());
		writeH(0);
		writeD(item.getItemId());
		writeQ(item.getItemCount());
		writeD(0);
		writeD(item.getGift());
		writeD(item.getItemType());
		writeD(0);
		writeC(0);
		writeH(0);
		writeS(item.getTitleDescription());
		writeS(item.getItemDescription());
	}
}