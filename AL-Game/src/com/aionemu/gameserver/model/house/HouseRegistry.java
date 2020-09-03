package com.aionemu.gameserver.model.house;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerRegisteredItemsDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javolution.util.FastList;
import javolution.util.FastMap;

import com.aionemu.gameserver.model.gameobjects.HouseDecoration;
import com.aionemu.gameserver.model.gameobjects.HouseObject;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.templates.housing.PartType;

/**
 * @author Rolandas
 */
public class HouseRegistry {

	private static final Logger log = LoggerFactory.getLogger(HouseRegistry.class);
	private House owner;
	private FastMap<Integer, HouseObject<?>> objects;
	private FastMap<Integer, HouseDecoration> customParts;
	private HouseDecoration[] defaultParts = new HouseDecoration[28];
	private PersistentState persistentState = PersistentState.UPDATED;

	public HouseRegistry(House owner) {
		this.owner = owner;
		this.objects = FastMap.newInstance();
		this.customParts = FastMap.newInstance();
	}

	public House getOwner() {
		return owner;
	}

	public void setOwner(House newModel) {
		owner = newModel;
	}

	/**
	 * Get all objects including deleted
	 *
	 * @return
	 */
	public FastList<HouseObject<?>> getObjects() {
		FastList<HouseObject<?>> temp = FastList.newInstance();
		for (HouseObject<?> obj : objects.values()) {
			temp.add(obj);
		}
		return temp;
	}

	public FastList<HouseObject<?>> getSpawnedObjects() {
		FastList<HouseObject<?>> temp = FastList.newInstance();
		for (HouseObject<?> obj : objects.values()) {
			if (obj.isSpawnedByPlayer() && obj.getPersistentState() != PersistentState.DELETED)
				temp.add(obj);
		}
		return temp;
	}

	public FastList<HouseObject<?>> getNotSpawnedObjects() {
		FastList<HouseObject<?>> temp = FastList.newInstance();
		for (HouseObject<?> obj : objects.values()) {
			if (!obj.isSpawnedByPlayer() && obj.getPersistentState() != PersistentState.DELETED)
				temp.add(obj);
		}
		return temp;
	}

	public HouseObject<?> getObjectByObjId(int itemObjId) {
		return objects.get(itemObjId);
	}

	public boolean putObject(HouseObject<?> houseObject) {
		if (objects.containsKey(houseObject.getObjectId()))
			return false;

		if (houseObject.getPersistentState() != PersistentState.NEW) {
			log.error("Inserting not new HouseObject: " + houseObject.getObjectId());
			return false;
		}

		objects.put(houseObject.getObjectId(), houseObject);
		setPersistentState(PersistentState.UPDATE_REQUIRED);
		return true;
	}

	public HouseObject<?> removeObject(int itemObjId) {
		if (!objects.containsKey(itemObjId))
			return null;

		HouseObject<?> oldObject = objects.get(itemObjId);
		if (oldObject.getPersistentState() == PersistentState.NEW)
			discardObject(itemObjId);
		else
			oldObject.setPersistentState(PersistentState.DELETED);
		setPersistentState(PersistentState.UPDATE_REQUIRED);

		return oldObject;
	}

	/**
	 * In packets used custom parts are not included (kinda semi-deleted)
	 */
	public FastList<HouseDecoration> getCustomParts() {
		FastList<HouseDecoration> temp = FastList.newInstance();
		for (HouseDecoration decor : customParts.values()) {
			if (decor.getPersistentState() != PersistentState.DELETED && !decor.isUsed())
				temp.add(decor);
		}
		return temp;
	}

	public HouseDecoration getCustomPartByType(PartType partType) {
		for (HouseDecoration deco : customParts.values()) {
			if (deco.getPersistentState() != PersistentState.DELETED && deco.getTemplate().getType() == partType) {
				return deco;
			}
		}
		return null;
	}

	public HouseDecoration getCustomPartByObjId(int itemObjId) {
		return customParts.get(itemObjId);
	}

	public HouseDecoration getCustomPartByPartId(int partId) {
		for (HouseDecoration deco : customParts.values()) {
			if (deco.getPersistentState() != PersistentState.DELETED && deco.getTemplate().getId() == partId) {
				return deco;
			}
		}
		return null;
	}

	public int getCustomPartCountByPartId(int partId) {
		int counter = 0;
		for (HouseDecoration deco : customParts.values()) {
			if (deco.getPersistentState() != PersistentState.DELETED && deco.getTemplate().getId() == partId) {
				counter++;
			}
		}
		return counter;
	}

	public boolean putCustomPart(HouseDecoration houseDeco) {
		if (customParts.containsKey(houseDeco.getObjectId()))
			return false;
		if (houseDeco.getPersistentState() != PersistentState.NEW) {
			log.error("Inserting not new HouseDecoration: " + houseDeco.getObjectId());
			return false;
		}

		customParts.put(houseDeco.getObjectId(), houseDeco);
		setPersistentState(PersistentState.UPDATE_REQUIRED);
		return true;
	}

	public HouseDecoration removeCustomPart(int itemObjId) {
		HouseDecoration obj = null;

		if (customParts.containsKey(itemObjId)) {
			obj = customParts.get(itemObjId);
			obj.setPersistentState(PersistentState.DELETED);
			setPersistentState(PersistentState.UPDATE_REQUIRED);
		}
		return obj;
	}

	public FastList<HouseDecoration> getDefaultParts() {
		FastList<HouseDecoration> temp = FastList.newInstance();
		for (HouseDecoration deco : defaultParts) {
			if (deco != null)
				temp.add(deco);
		}
		return temp;
	}

	public HouseDecoration getDefaultPartByType(PartType partType) {
		return defaultParts[partType.getStartLineNr()];
	}

	public void putDefaultPart(HouseDecoration houseDeco) {
		defaultParts[houseDeco.getTemplate().getType().getStartLineNr()] = houseDeco;
		houseDeco.setPersistentState(PersistentState.NOACTION);
	}

	/**
	 * Get all decoration parts including deleted
	 *
	 * @return
	 */
	public FastList<HouseDecoration> getAllParts() {
		FastList<HouseDecoration> temp = FastList.newInstance();
		for (HouseDecoration deco : defaultParts) {
			if (deco != null)
				temp.add(deco);
		}
		for (HouseDecoration decor : customParts.values()) {
			temp.add(decor);
		}
		return temp;
	}

	public HouseDecoration getRenderPart(PartType partType) {
		for (HouseDecoration decor : customParts.values()) {
			if (decor.getTemplate().getType() == partType && decor.isUsed()) {
				return decor;
			}
		}
		return getDefaultPartByType(partType);
	}

	public void setPartInUse(HouseDecoration decorationUse) {
		HouseDecoration defaultDecor = defaultParts[decorationUse.getTemplate().getType().getStartLineNr()];
		if (defaultDecor.getTemplate().getId() == decorationUse.getTemplate().getId()) {
			defaultDecor.setUsed(true);
			for (HouseDecoration decor : customParts.values()) {
				if (decor.getTemplate().getType() != decorationUse.getTemplate().getType())
					continue;
				if (decor.getPersistentState() != PersistentState.DELETED) {
					if (decor.isUsed()) {
						decor.setUsed(false);
						if (decor.getPersistentState() == PersistentState.NEW)
							discardPart(decor);
						else
							decor.setPersistentState(PersistentState.DELETED);
					}
				}
			}
			return;
		}
		for (HouseDecoration decor : customParts.values()) {
			if (decor.getTemplate().getType() != decorationUse.getTemplate().getType())
				continue;
			if (decor.getPersistentState() != PersistentState.DELETED) {
				if (decorationUse.equals(decor)) {
					decor.setUsed(true);
					defaultDecor.setUsed(false);
				}
				else {
					if (decor.isUsed() && !decorationUse.equals(decor)) {
						decor.setUsed(false);
						if (decor.getPersistentState() == PersistentState.NEW)
							discardPart(decor);
						else
							decor.setPersistentState(PersistentState.DELETED);
					}
				}
			}
		}
	}

	public void discardObject(Integer objectId) {
		objects.remove(objectId);
	}

	public void discardPart(HouseDecoration decor) {
		customParts.remove(decor.getObjectId());
	}

	public void save() {
		if (persistentState == PersistentState.UPDATE_REQUIRED) {
			DAOManager.getDAO(PlayerRegisteredItemsDAO.class).store(this, getOwner().getOwnerId());
		}
	}

	public final PersistentState getPersistentState() {
		return persistentState;
	}

	public final void setPersistentState(PersistentState persistentState) {
		this.persistentState = persistentState;
	}

	public int size() {
		return objects.size() + customParts.size();
	}
	
	public void despawnObjects() {
	   if(getSpawnedObjects().isEmpty())
		  DAOManager.getDAO(PlayerRegisteredItemsDAO.class).resetRegistry(owner.getOwnerId());
	   else
		  despawnObjects(true);
	}

	/**
	 * Use before switching house
	 */
	public void despawnObjects(boolean remove) {
		for (HouseObject<?> obj : getSpawnedObjects()) {
			if (obj.isInWorld()) {
				obj.getController().onDelete();
				obj.clearKnownlist();
			}
			if (remove)
				obj.removeFromHouse();
		}
		if (remove) {
			setPersistentState(PersistentState.UPDATE_REQUIRED);
			save();
		}
	}
}