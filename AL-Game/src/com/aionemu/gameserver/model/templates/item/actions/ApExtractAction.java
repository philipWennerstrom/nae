package com.aionemu.gameserver.model.templates.item.actions;

import javax.xml.bind.annotation.XmlAttribute;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.templates.item.Acquisition;
import com.aionemu.gameserver.model.templates.item.ArmorType;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.utils.audit.AuditLogger;

/**
 * @author Rolandas, Luzien
 */
public class ApExtractAction extends AbstractItemAction {

	@XmlAttribute
	protected ApExtractTarget target;
	@XmlAttribute
	protected float rate;

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		if (targetItem == null || !targetItem.canApExtract())
			return false;
		if (parentItem.getItemTemplate().getLevel() < targetItem.getItemTemplate().getLevel())
			return false;
		if (parentItem.getItemTemplate().getItemQuality() != targetItem.getItemTemplate().getItemQuality())
			return false;

		ApExtractTarget type = null;
		switch (targetItem.getItemTemplate().getCategory()) {
			case SWORD:
			case DAGGER:
			case MACE:
			case ORB:
			case SPELLBOOK:
			case BOW:
			case GREATSWORD:
			case POLEARM:
			case STAFF:
			case SHIELD:
				type = ApExtractTarget.WEAPON;
				break;
			case JACKET:
			case PANTS:
			case SHOES:
			case GLOVES:
			case SHOULDERS:
				type = ApExtractTarget.ARMOR;
				break;
			case NECKLACE:
			case EARRINGS:
			case RINGS:
			case HELMET:
			case BELT:
				type = ApExtractTarget.ACCESSORY;
				break;
			case NONE:
				if (targetItem.getItemTemplate().getArmorType() == ArmorType.WING) {
					type = ApExtractTarget.WING;
					break;
				}
				return false;
			default:
				return false;
		}
		return (target == ApExtractTarget.EQUIPMENT || target == type);
	}

	@Override
	public void act(Player player, Item parentItem, Item targetItem) {
		Acquisition acquisition = targetItem.getItemTemplate().getAcquisition();
		if (acquisition == null || acquisition.getRequiredAp() == 0)
			return;
		int ap = (int) (acquisition.getRequiredAp() * rate);
		Storage inventory = player.getInventory();

		if (inventory.delete(targetItem) != null) {
			if (inventory.decreaseByObjectId(parentItem.getObjectId(), 1))
				AbyssPointsService.addAp(player, ap);
		}
		else
			AuditLogger.info(player, "Possible extract item hack, do not remove item.");
	}

	public ApExtractTarget getTarget() {
		return target;
	}

	public float getRate() {
		return rate;
	}
}