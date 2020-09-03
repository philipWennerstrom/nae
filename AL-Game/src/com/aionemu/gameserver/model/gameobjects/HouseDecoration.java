package com.aionemu.gameserver.model.gameobjects;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.templates.housing.HousePart;

/**
 * @author Rolandas
 */
public class HouseDecoration extends AionObject {

	private int templateId;
	private boolean isUsed;
	private PersistentState persistentState;

	public HouseDecoration(int objectId, int templateId) {
		super(objectId);
		this.templateId = templateId;
		this.persistentState = PersistentState.NEW;
	}

	public HousePart getTemplate() {
		return DataManager.HOUSE_PARTS_DATA.getPartById(templateId);
	}

	public PersistentState getPersistentState() {
		return persistentState;
	}

	public void setPersistentState(PersistentState persistentState) {
		this.persistentState = persistentState;
	}

	@Override
	public String getName() {
		return getTemplate().getName();
	}

	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		if (this.isUsed != isUsed && persistentState != PersistentState.DELETED) {
			this.isUsed = isUsed;
			if (persistentState != PersistentState.NEW && persistentState != PersistentState.NOACTION)
				persistentState = PersistentState.UPDATE_REQUIRED;
		}
	}
	
	@Override
	public boolean equals(Object object) {
	    if(!(object instanceof HouseDecoration))
	        return false;
	    else
	        return ((HouseDecoration)object).getObjectId().equals(this.getObjectId());
	    }
	 
	@Override
	public int hashCode() {
	    return this.getObjectId();
	}
}