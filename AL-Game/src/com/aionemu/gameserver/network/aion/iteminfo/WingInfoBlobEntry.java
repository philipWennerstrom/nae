package com.aionemu.gameserver.network.aion.iteminfo;

import java.nio.ByteBuffer;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.network.aion.iteminfo.ItemInfoBlob.ItemBlobType;

/**
 * This blob is sent for clothes. It keeps info about slots that cloth can be equipped to.
 *
 * @author -Nemesiss-
 * @modified Rolandas
 */
public class WingInfoBlobEntry extends ItemBlobEntry{

	WingInfoBlobEntry() {
		super(ItemBlobType.SLOTS_WING);
	}

	@Override
	public
	void writeThisBlob(ByteBuffer buf) {
		Item item = ownerItem;

		writeQ(buf, ItemSlot.getSlotFor(item.getItemTemplate().getItemSlot()).getSlotIdMask());
		writeQ(buf, 0); // no secondary slot
	}

	@Override
	public int getSize() {
		return 16;
	}
}