package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

/**
 * @author Sarynth, xTz, Source
 */
public class SiegeConfig {

	/**
	 * Siege Enabled
	 */
	@Property(key = "gameserver.siege.enable", defaultValue = "true")
	public static boolean SIEGE_ENABLED;
	/**
	 * Siege Reward Rate
	 */
	@Property(key = "gameserver.siege.medal.rate", defaultValue = "1")
	public static int SIEGE_MEDAL_RATE;
	/**
	 * Siege sield Enabled
	 */
	@Property(key = "gameserver.siege.shield.enable", defaultValue = "true")
	public static boolean SIEGE_SHIELD_ENABLED;
	/**
	 * Balaur Assaults Enabled
	 */
	@Property(key = "gameserver.siege.assault.enable", defaultValue = "false")
	public static boolean BALAUR_AUTO_ASSAULT;
	@Property(key = "gameserver.siege.assault.rate", defaultValue = "1")
	public static float BALAUR_ASSAULT_RATE;
	
	/**
	 * Siege Race Protector spawn shedule
	 */
	@Property(key = "gameserver.siege.protector.time", defaultValue = "0 0 21 ? * *")
	public static String RACE_PROTECTOR_SPAWN_SCHEDULE;
	/**
	 * Berserker Sunayaka spawn time
	 */
	@Property(key = "gameserver.sunayaka.time", defaultValue = "0 0 23 ? * *")
	public static String BERSERKER_SUNAYAKA_SPAWN_SCHEDULE;
	/**
	 * Berserker Sunayaka spawn time
	 */
	@Property(key = "gameserver.moltenus.time", defaultValue = "0 0 22 ? * SUN")
	public static String MOLTENUS_SPAWN_SCHEDULE;
}