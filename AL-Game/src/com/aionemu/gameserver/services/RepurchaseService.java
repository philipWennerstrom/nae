package com.aionemu.gameserver.services;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.trade.RepurchaseList;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.audit.AuditLogger;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author xTz
 */
public class RepurchaseService {

	private Multimap<Integer, Item> repurchaseItems = ArrayListMultimap.create();

	/**
	 * Save items for repurchase for this player
	 */
	public void addRepurchaseItems(Player player, List<Item> items) {
		repurchaseItems.putAll(player.getObjectId(), items);
	}

	/**
	 * Delete all repurchase items for this player
	 */
	public void removeRepurchaseItems(Player player) {
		repurchaseItems.removeAll(player.getObjectId());
	}

	public void removeRepurchaseItem(Player player, Item item) {
		repurchaseItems.get(player.getObjectId()).remove(item);
	}

	public Collection<Item> getRepurchaseItems(int playerObjectId) {
		Collection<Item> items = repurchaseItems.get(playerObjectId);
		return items != null ? items : Collections.<Item> emptyList();
	}

	public Item getRepurchaseItem(Player player, int itemObjectId) {
		Collection<Item> items = getRepurchaseItems(player.getObjectId());
		for (Item item : items) {
			if (item.getObjectId() == itemObjectId) {
				return item;
			}
		}
		return null;
	}

	/**
	 * @param player
	 * @param repurchaseList
	 */
	public void repurchaseFromShop(Player player, RepurchaseList repurchaseList) {
		Storage inventory = player.getInventory();
		for (Item repurchaseItem : repurchaseList.getRepurchaseItems()) {
			Collection<Item> items = repurchaseItems.get(player.getObjectId());
			if (items.contains(repurchaseItem)) {
				if (inventory.tryDecreaseKinah(repurchaseItem.getRepurchasePrice())) {
					ItemService.addItem(player, repurchaseItem);
					removeRepurchaseItem(player, repurchaseItem);
				}
				else {
					AuditLogger.info(player, "Player try repurchase item: " + repurchaseItem.getItemId() + " count: " + repurchaseItem.getItemCount() + " whithout kinah");
				}
			}
			else {
				AuditLogger.info(player, "Player might be abusing CM_BUY_ITEM try dupe item: " + repurchaseItem.getItemId() + " count: " + repurchaseItem.getItemCount());
			}
		}
	}

	public static RepurchaseService getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {

		protected static final RepurchaseService INSTANCE = new RepurchaseService();
	}
}