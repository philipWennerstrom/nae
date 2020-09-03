package com.aionemu.gameserver.dataholders;

import gnu.trove.map.hash.TIntObjectHashMap;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.items.RandomBonusResult;
import com.aionemu.gameserver.model.templates.item.bonuses.RandomBonus;
import com.aionemu.gameserver.model.templates.stats.ModifiersTemplate;

/**
 * @author Rolandas
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "randomBonuses" })
@XmlRootElement(name = "random_bonuses")
public class ItemRandomBonusData {

	@XmlElement(name = "random_bonus", required = true)
	protected List<RandomBonus> randomBonuses;

	@XmlTransient
	private TIntObjectHashMap<RandomBonus> randomBonusData = new TIntObjectHashMap<RandomBonus>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (RandomBonus bonus : randomBonuses) {
			randomBonusData.put(bonus.getId(), bonus);
		}
		randomBonuses.clear();
		randomBonuses = null;
	}

	/**
	 * Gets a randomly chosen modifiers from bonus list.
	 * 
	 * @param item
	 *          rnd_bonus from the item template
	 * @return null if not a chance
	 */
	public RandomBonusResult getRandomModifiers(int rndOptionSet) {
		RandomBonus bonus = randomBonusData.get(rndOptionSet);
		if (bonus == null)
			return null;

		List<ModifiersTemplate> modifiersGroup = bonus.getModifiers();

		int chance = Rnd.get(10000);
		int current = 0;
		ModifiersTemplate template = null;
		int number = 0;

		for (int i = 0; i < modifiersGroup.size(); i++) {
			ModifiersTemplate modifiers = modifiersGroup.get(i);

			current += modifiers.getChance() * 100;
			if (current >= chance) {
				template = modifiers;
				number = i + 1;
				break;
			}
		}
		return template == null ? null : new RandomBonusResult(template, number);
	}

	public ModifiersTemplate getTemplate(int rndOptionSet, int number) {
		RandomBonus bonus = randomBonusData.get(rndOptionSet);
		if (bonus == null)
			return null;
		return bonus.getModifiers().get(number - 1);
	}

	public int size() {
		return randomBonusData.size();
	}
}