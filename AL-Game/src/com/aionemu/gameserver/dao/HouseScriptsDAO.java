package com.aionemu.gameserver.dao;

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.gameserver.model.gameobjects.player.PlayerScripts;

/**
 * @author Rolandas
 */
public abstract class HouseScriptsDAO implements DAO {

	@Override
	public final String getClassName() {
		return HouseScriptsDAO.class.getName();
	}

	public abstract PlayerScripts getPlayerScripts(int houseId);
	
	public abstract void addScript(int houseId, int position, String script);

	public abstract void updateScript(int houseId, int position, String script);

	public abstract void deleteScript(int houseId, int position);
}