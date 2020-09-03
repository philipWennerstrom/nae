package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

/**
 * @author lord_rex
 */
public class ShutdownConfig {

	/**
	 * Shutdown Hook Mode.
	 */
	@Property(key = "gameserver.shutdown.mode", defaultValue = "1")
	public static int HOOK_MODE;

	/**
	 * Shutdown Hook delay.
	 */
	@Property(key = "gameserver.shutdown.delay", defaultValue = "60")
	public static int HOOK_DELAY;

	/**
	 * Shutdown announce interval.
	 */
	@Property(key = "gameserver.shutdown.interval", defaultValue = "1")
	public static int ANNOUNCE_INTERVAL;

	/**
	 * Safe reboot mode.
	 */
	@Property(key = "gameserver.shutdown.safereboot", defaultValue = "true")
	public static boolean SAFE_REBOOT;
}