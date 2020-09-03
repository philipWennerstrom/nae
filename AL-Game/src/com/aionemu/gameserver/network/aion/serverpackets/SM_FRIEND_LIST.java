package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.gameobjects.player.Friend;
import com.aionemu.gameserver.model.gameobjects.player.FriendList;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.services.HousingService;

/**
 * Sends a friend list to the client
 * 
 * @author Ben
 */
public class SM_FRIEND_LIST extends AionServerPacket {

	@Override
	protected void writeImpl(AionConnection con) {
		FriendList list = con.getActivePlayer().getFriendList();

		writeH((0 - list.getSize()));
		writeC(0);// unk

		for (Friend friend : list) {
			writeD(friend.getOid());
			writeS(friend.getName());
			writeD(friend.getLevel());
			writeD(friend.getPlayerClass().getClassId());
			writeC(friend.isOnline() ? 1 : 0);
			writeD(friend.getMapId());
			writeD(friend.getLastOnlineTime()); // Date friend was last online as a Unix timestamp.
			writeS(friend.getNote()); // Friend note
			writeC(friend.getStatus().getId());

			int address = HousingService.getInstance().getPlayerAddress(friend.getOid());
			if (address > 0) {
				House house = HousingService.getInstance().getPlayerStudio(friend.getOid());
				if (house == null) {
					house = HousingService.getInstance().getHouseByAddress(address);
					writeD(house.getAddress().getId());
				}
				else {
					writeD(address);
				}
				writeC(house.getSettingFlags() >> 8);
			}
			else {
				writeD(0);
				writeC(0);
			}
		}
	}
}