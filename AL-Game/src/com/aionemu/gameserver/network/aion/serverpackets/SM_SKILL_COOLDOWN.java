package com.aionemu.gameserver.network.aion.serverpackets;

import java.util.ArrayList;
import java.util.Map;

import javolution.util.FastMap;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author ATracer, nrg
 */
public class SM_SKILL_COOLDOWN extends AionServerPacket {

	private FastMap<Integer, Long> cooldowns;

	public SM_SKILL_COOLDOWN(FastMap<Integer, Long> cooldowns) {
		this.cooldowns = cooldowns;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
        writeH(calculateSize());
        long currentTime = System.currentTimeMillis();
        for (Map.Entry<Integer, Long> entry : cooldowns.entrySet()) {
            int left = (int) ((entry.getValue() - currentTime) / 1000);
            ArrayList<Integer> skillsWithCooldown = DataManager.SKILL_DATA.getSkillsForCooldownId(entry.getKey());

            for (int index = 0; index < skillsWithCooldown.size(); index++) {
                writeH(skillsWithCooldown.get(index));
                writeD(left > 0 ? left : 0);       
            }
        }
	}
	
    private int calculateSize() {
        int size = 0;
        for(Map.Entry<Integer, Long> entry : cooldowns.entrySet()) {
            size += DataManager.SKILL_DATA.getSkillsForCooldownId(entry.getKey()).size();
        }
        return size;
    }
}