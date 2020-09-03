package com.aionemu.gameserver.dao;

import com.aionemu.commons.database.dao.DAO;

/**
 * @author synchro2
 */
public abstract class OldNamesDAO implements DAO {

	public abstract boolean isOldName(String name);

	public abstract void insertNames(int id, String oldname, String newname);

	@Override
	public final String getClassName() {
		return OldNamesDAO.class.getName();
	}
}