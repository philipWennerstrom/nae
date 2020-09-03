package com.aionemu.gameserver.model.templates.item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author antness
 */
@XmlType(name = "ResultedItemsCollection")
public class ResultedItemsCollection {

	@XmlElement(name = "item")
	protected ArrayList<ResultedItem> items;
	@XmlElement(name = "random_item")
	protected ArrayList<RandomItem> randomItems;

	public Collection<ResultedItem> getItems() {
		return items != null ? items : Collections.<ResultedItem> emptyList();
	}

	public List<RandomItem> getRandomItems() {
		if (randomItems != null) {
			return randomItems;
		}
		else {
			return new ArrayList<RandomItem>();
		}
	}
}