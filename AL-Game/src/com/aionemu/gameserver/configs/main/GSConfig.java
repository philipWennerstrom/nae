package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;
import java.util.Calendar;

public class GSConfig {

	/**
	* Gameserver
	*/

	/* Server Country Code */
	@Property(key = "gameserver.country.code", defaultValue = "1")
	public static int SERVER_COUNTRY_CODE;
	
	/* Server Credits Name */
	@Property(key = "gameserver.name", defaultValue = "Aion Lightning")
	public static String SERVER_NAME;
	
	/* Players Max Level */
	@Property(key = "gameserver.players.max.level", defaultValue = "60")
	public static int PLAYER_MAX_LEVEL;
	
	/* Time Zone name (used for events & timed spawns) */	
	@Property(key = "gameserver.timezone", defaultValue = "")
	public static String TIME_ZONE_ID = Calendar.getInstance().getTimeZone().getID();
	
	/* Enable connection with CS (ChatServer) */
	@Property(key = "gameserver.chatserver.enable", defaultValue = "false")
	public static boolean ENABLE_CHAT_SERVER;

	/* Server MOTD Display revision */
	@Property(key = "gameserver.revisiondisplay.enable", defaultValue = "false")
	public static boolean SERVER_MOTD_DISPLAYREV;
	
	/**
	* Character creation
	*/
	@Property(key = "gameserver.character.creation.mode", defaultValue = "0")
	public static int CHARACTER_CREATION_MODE;
	
	@Property(key = "gameserver.character.limit.count", defaultValue = "8")
	public static int CHARACTER_LIMIT_COUNT;
	
	@Property(key = "gameserver.character.faction.limitation.mode", defaultValue = "0")
	public static int CHARACTER_FACTION_LIMITATION_MODE;

	@Property(key = "gameserver.ratio.limitation.enable", defaultValue = "false")
	public static boolean ENABLE_RATIO_LIMITATION;

	@Property(key = "gameserver.ratio.min.value", defaultValue = "60")
	public static int RATIO_MIN_VALUE;

	@Property(key = "gameserver.ratio.min.required.level", defaultValue = "10")
	public static int RATIO_MIN_REQUIRED_LEVEL;

	@Property(key = "gameserver.ratio.min.characters_count", defaultValue = "50")
	public static int RATIO_MIN_CHARACTERS_COUNT;

	@Property(key = "gameserver.ratio.high_player_count.disabling", defaultValue = "500")
	public static int RATIO_HIGH_PLAYER_COUNT_DISABLING;
	
	/**
	* Misc
	*/
	@Property(key = "gameserver.character.reentry.time", defaultValue = "20")
	public static int CHARACTER_REENTRY_TIME;
}