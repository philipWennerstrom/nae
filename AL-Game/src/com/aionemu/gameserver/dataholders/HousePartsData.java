package com.aionemu.gameserver.dataholders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;

import com.aionemu.gameserver.model.templates.housing.Building;
import com.aionemu.gameserver.model.templates.housing.HousePart;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "houseParts" })
@XmlRootElement(name = "house_parts")
public class HousePartsData {

	@XmlElement(name = "house_part")
	protected List<HousePart> houseParts;

	@XmlTransient
	Map<String, List<HousePart>> partsByTags = new HashMap<String, List<HousePart>>(5);

	@XmlTransient
	Map<Integer, HousePart> partsById = new HashMap<Integer, HousePart>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		if (houseParts == null)
			return;

		for (HousePart part : houseParts) {
			partsById.put(part.getId(), part);
			Iterator<String> iterator = part.getTags().iterator();
			while (iterator.hasNext()) {
				String tag = iterator.next();
				List<HousePart> parts = partsByTags.get(tag);
				if (parts == null) {
					parts = new ArrayList<HousePart>();
					partsByTags.put(tag, parts);
				}
				parts.add(part);
			}
		}

		houseParts.clear();
		houseParts = null;
	}

	public HousePart getPartById(int partId) {
		return partsById.get(partId);
	}

	public List<HousePart> getPartsForBuilding(Building building) {
		return partsByTags.get(building.getPartsMatchTag());
	}

	public int size() {
		return partsById.size();
	}
}