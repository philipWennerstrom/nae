package com.aionemu.gameserver.network.aion.iteminfo;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatFunction;
import com.aionemu.gameserver.model.templates.item.ArmorType;
import com.aionemu.gameserver.model.templates.item.EquipType;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.stats.ModifiersTemplate;
import com.aionemu.gameserver.network.PacketWriteHelper;

/**
 * Entry item info packet data (contains blob entries with detailed info).
 * 
 * @author -Nemesiss-
 * @modified Rolandas - complete rewrite, no trees (blob size must be known in advance!), just sequences
 */
public class ItemInfoBlob extends PacketWriteHelper {

	protected final Player player;
	protected final Item item;

	private List<ItemBlobEntry> itemBlobEntries = new ArrayList<ItemBlobEntry>();

	public ItemInfoBlob(Player player, Item item) {
		this.player = player;
		this.item = item;
	}

	@Override
	public void writeMe(ByteBuffer buf) {
		int totalSize = 0;
		for (ItemBlobEntry ent : itemBlobEntries)
			totalSize += ent.getSize() + 1; // 1 C for blob id

		writeH(buf, totalSize);
		for (ItemBlobEntry ent : itemBlobEntries)
			ent.writeMe(buf);
	}

	public void addBlobEntry(ItemBlobType type) {
		ItemBlobEntry ent = type.newBlobEntry();
		ent.setOwner(player, item, null);
		itemBlobEntries.add(ent);
	}
	
	public void addBonusBlobEntry(IStatFunction modifier) {
		ItemBlobEntry ent = ItemBlobType.STAT_BONUSES.newBlobEntry();
		ent.setOwner(player, item, modifier);
		itemBlobEntries.add(ent);
	}

	public static ItemBlobEntry newBlobEntry(ItemBlobType type, Player player, Item item) {
		if (type == ItemBlobType.STAT_BONUSES)
			throw new UnsupportedOperationException();
		ItemBlobEntry ent = type.newBlobEntry();
		ent.setOwner(player, item, null);
		return ent;
	}

	public static ItemInfoBlob getFullBlob(Player player, Item item) {
		ItemInfoBlob blob = new ItemInfoBlob(player, item);

		ItemTemplate itemTemplate = item.getItemTemplate();

		if (itemTemplate.getWeaponType() != null)
			blob.addBlobEntry(ItemBlobType.COMPOSITE_ITEM);

		if (item.getEquipmentType() != EquipType.NONE) {
			// EQUIPPED SLOT
			blob.addBlobEntry(ItemBlobType.EQUIPPED_SLOT);

			// SLOT INFO
			if (itemTemplate.getArmorType() != null && itemTemplate.getArmorType() != ArmorType.NO_ARMOR) {
				switch (itemTemplate.getArmorType()) {
					case WING:
						blob.addBlobEntry(ItemBlobType.SLOTS_WING);
						break;
					case SHIELD:
						blob.addBlobEntry(ItemBlobType.SLOTS_SHIELD);
						break;
					/*
					case ARROW:
						blob.addBlobEntry(ItemBlobType.SLOTS_ARROW);
						break;
					*/
					default:
						blob.addBlobEntry(ItemBlobType.SLOTS_ARMOR);
						break;
				}
			}
			else if (itemTemplate.isWeapon())
				blob.addBlobEntry(ItemBlobType.SLOTS_WEAPON);
			else if (item.getEquipmentType() == EquipType.ARMOR)
				blob.addBlobEntry(ItemBlobType.SLOTS_ACCESSORY); // power shards, helmets, earrings, rings, belts

			// MANA STONES
			blob.addBlobEntry(ItemBlobType.MANA_SOCKETS);
			
			if (item.getConditioningInfo() != null)
				blob.addBlobEntry(ItemBlobType.CONDITIONING_INFO);

			ModifiersTemplate rndBonuses = item.getRandomStatsTemplate();
			List<StatFunction> allModifiers = itemTemplate.getModifiers();
			if (rndBonuses != null) {
				blob.addBlobEntry(ItemBlobType.PREMIUM_OPTION);
				allModifiers = StatFunction.mergeRandomBonuses(itemTemplate.getModifiers(), 
					rndBonuses.getModifiers());
			}

			if (allModifiers != null) {
				for (IStatFunction modifier : allModifiers) {
					if (modifier.isBonus() && !modifier.hasConditions()) {
						blob.addBonusBlobEntry(modifier);
					}
				}
			}
		} else if (itemTemplate.getTemplateId() == 141000001) {
			blob.addBlobEntry(ItemBlobType.STIGMA_SHARD);
		}

		// GENERAL INFO
		blob.addBlobEntry(ItemBlobType.GENERAL_INFO);

		return blob;
	}

	public enum ItemBlobType {
		GENERAL_INFO(0x00) { @Override ItemBlobEntry newBlobEntry() { return new GeneralInfoBlobEntry(); } },
		SLOTS_WEAPON(0x01) { @Override ItemBlobEntry newBlobEntry() { return new WeaponInfoBlobEntry(); } },
		SLOTS_ARMOR(0x02) { @Override ItemBlobEntry newBlobEntry() { return new ArmorInfoBlobEntry(); } },
		SLOTS_SHIELD(0x03) { @Override ItemBlobEntry newBlobEntry() { return new ShieldInfoBlobEntry(); } },
		SLOTS_ACCESSORY(0x04) { @Override ItemBlobEntry newBlobEntry() { return new AccessoryInfoBlobEntry(); } },
		SLOTS_ARROW(0x05) { @Override ItemBlobEntry newBlobEntry() { return new ArrowInfoBlobEntry(); } },
		EQUIPPED_SLOT(0x06) { @Override ItemBlobEntry newBlobEntry() { return new EquippedSlotBlobEntry(); } },
		// Removed from 3.5
		STIGMA_INFO(0x07) { @Override ItemBlobEntry newBlobEntry() { return new StigmaInfoBlobEntry(); } },
		STIGMA_SHARD(0x08) { @Override ItemBlobEntry newBlobEntry() { return new StigmaShardInfoBlobEntry(); } },
		// missing(0x09), //15? [Not handled before]
		PREMIUM_OPTION(0x10) { @Override ItemBlobEntry newBlobEntry() { return new PremiumOptionInfoBlobEntry(); } },
		STAT_BONUSES(0x0A) { @Override ItemBlobEntry newBlobEntry() { return new BonusInfoBlobEntry(); } },
		// [Not handled before] retail send it xx times (smth dynamically changed)
		MANA_SOCKETS(0x0B) { @Override ItemBlobEntry newBlobEntry() { return new ManaStoneInfoBlobEntry(); } },
		// 0x0C - not used?
		SLOTS_WING(0x0D) { @Override ItemBlobEntry newBlobEntry() { return new WingInfoBlobEntry(); } },
		COMPOSITE_ITEM(0x0E) { @Override ItemBlobEntry newBlobEntry() { return new CompositeItemBlobEntry(); } },
		CONDITIONING_INFO(0x0F) { @Override ItemBlobEntry newBlobEntry() { return new ConditioningInfoBlobEntry(); } };

		private int entryId;

		private ItemBlobType(int entryId) {
			this.entryId = entryId;
		}

		public int getEntryId() {
			return entryId;
		}

		abstract ItemBlobEntry newBlobEntry();
	}
}