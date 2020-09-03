package com.aionemu.gameserver.services.item;

import com.aionemu.gameserver.configs.main.LegionConfig;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.StorageType;
import com.aionemu.gameserver.model.team.legion.LegionPermissionsMask;
import com.aionemu.gameserver.model.templates.item.ItemCategory;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.LegionService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer
 */
public class ItemRestrictionService {

	/**
	 * Check if item can be moved from storage by player
	 */
	public static boolean isItemRestrictedFrom(Player player, Item item, byte storage) {
		StorageType type = StorageType.getStorageTypeById(storage);
		switch (type) {
			case LEGION_WAREHOUSE:
				if (!LegionService.getInstance().getLegionMember(player.getObjectId()).hasRights(LegionPermissionsMask.WH_WITHDRAWAL) 
					|| !LegionConfig.LEGION_WAREHOUSE || !player.isLegionMember()) {
					// You do not have the authority to use the Legion warehouse.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300322));
					return true;
				}
				break;
		}
		return false;
	}

	/**
	 * Check if item can be moved to storage by player
	 */
	public static boolean isItemRestrictedTo(Player player, Item item, byte storage) {
		StorageType type = StorageType.getStorageTypeById(storage);
		switch (type) {
			case REGULAR_WAREHOUSE:
				if (!item.isStorableinWarehouse(player)) {
					// You cannot store this in the warehouse.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300418));
					return true;
				}
				break;
			case ACCOUNT_WAREHOUSE:
				if (!item.isStorableinAccWarehouse(player)) {
					// You cannot store this item in the account warehouse.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400356));
					return true;
				}
				break;
			case LEGION_WAREHOUSE:
				if (!item.isStorableinLegWarehouse(player) || !LegionConfig.LEGION_WAREHOUSE) {
					// You cannot store this item in the Legion warehouse.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400355));
					return true;
				}
				else if (!player.isLegionMember() || !LegionService.getInstance().getLegionMember(player.getObjectId()).hasRights(LegionPermissionsMask.WH_DEPOSIT)) {
					// You do not have the authority to use the Legion warehouse.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300322));
					return true;
				}
				break;
		}

		return false;
	}
	
	/** Check, whether the item can be removed */
	public static boolean canRemoveItem(Player player, Item item) {
		ItemTemplate it = item.getItemTemplate();
		if (it.getCategory() == ItemCategory.QUEST) {
			// TODO: not removable, if quest status start and quest can not be abandoned
			// Waiting for quest data reparse
			return true;
		}
		return true;
	}
}