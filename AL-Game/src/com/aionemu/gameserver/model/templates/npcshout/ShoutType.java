package com.aionemu.gameserver.model.templates.npcshout;

import javax.xml.bind.annotation.*;

/**
 * @author Rolandas
 */

@XmlType(name = "ShoutType")
@XmlEnum
public enum ShoutType {

	BROADCAST,
	SAY,
	HEAR;

	public String value() {
		return name();
	}

	public static ShoutType fromValue(String v) {
		return valueOf(v);
	}
}