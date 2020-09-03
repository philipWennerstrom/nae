package com.aionemu.gameserver.model;

import javax.xml.bind.annotation.XmlEnum;

import org.apache.commons.lang.NotImplementedException;

/**
 * Basic enum with races.<br>
 * I believe that NPCs will have their own races, so it's quite comfortable to have it in the same place
 * 
 * @author SoulKeeper
 */

@XmlEnum
public enum Race {
	/**
	 * Playable races
	 */
	ELYOS(0, new DescriptionId(480480)),
	ASMODIANS(1, new DescriptionId(480481)),

	/**
	 * Npc races
	 */
	LYCAN(2),
	CONSTRUCT(3),
	CARRIER(4),
	DRAKAN(5),
	LIZARDMAN(6),
	TELEPORTER(7),
	NAGA(8),
	BROWNIE(9),
	KRALL(10),
	SHULACK(11),
	BARRIER(12),
	PC_LIGHT_CASTLE_DOOR(13),
	PC_DARK_CASTLE_DOOR(14),
	DRAGON_CASTLE_DOOR(15),
	GCHIEF_LIGHT(16),
	GCHIEF_DARK(17),
	DRAGON(18),
	OUTSIDER(19),
	RATMAN(20),
	DEMIHUMANOID(21),
	UNDEAD(22),
	BEAST(23),
	MAGICALMONSTER(24),
	ELEMENTAL(25),
	LIVINGWATER(28),

	/**
	 * Special races
	 */
	NONE(26),
	PC_ALL(27),
	DEFORM(28),
	
	//2.6
	NEUT(29),
	//2.7 -- NOT SURE !!!
	GHENCHMAN_LIGHT(30),
	GHENCHMAN_DARK(31),
	//3.0
	EVENT_TOWER_DARK(32),
	EVENT_TOWER_LIGHT(33),
	GOBLIN(34),
	TRICODARK(35),
	NPC(36),
	// 3.5
	LIGHT(37),
	DARK(38),
	WORLD_EVENT_DEFTOWER(39)
	;
	
	private int raceId;
	private DescriptionId descriptionId;

	/**
	 * Constructors
	 */
	private Race(int raceId) {
		this(raceId, null);
	}

	private Race(int raceId, DescriptionId descriptionId){
		this.raceId = raceId;
		this.descriptionId = descriptionId;
	}

	/**
	 * Accessors
	 */
	public int getRaceId() {
		return raceId;
	}

	public DescriptionId getRaceDescriptionId(){
		if(descriptionId == null){
			throw new NotImplementedException("Race name DescriptionId is unknown for race" + this);
		}
		return descriptionId;
	}
	
	public static Race getRaceByString(String fieldName) {
		for (Race r : values()) {
			if (r.toString().equals(fieldName))
				return r;
		}
		return null;
	}
}