package com.aionemu.gameserver.model.stats.calc;

/**
 * @author ATracer
 */
public class StatCapUtil {

	public static void calculateBaseValue(Stat2 stat, byte isPlayer) {

		int lowerCap = Short.MIN_VALUE; // -32767
		int upperCap = Short.MAX_VALUE; // 32767

		// TODO more stats????
		switch (stat.getStat()) {
			case MAIN_HAND_POWER:
			case MAIN_HAND_ACCURACY:
			case MAIN_HAND_CRITICAL:
			case OFF_HAND_POWER:
			case OFF_HAND_ACCURACY:
			case OFF_HAND_CRITICAL:
			case MAGICAL_RESIST:
			case PHYSICAL_CRITICAL_RESIST:
			case EVASION:
			case PHYSICAL_DEFENSE:
			case HEAL_SKILL_BOOST:
				lowerCap = 0;
				break;
			case ATTACK_SPEED:
				int base = stat.getBase() / 2;
				if (stat.getBonus() > 0 && base < stat.getBonus())
					stat.setBonus(base);
				else if (stat.getBonus() < 0 && base < -stat.getBonus())
					stat.setBonus(-base);
				break;
			case SPEED:
				lowerCap = 0;
				upperCap = isPlayer == 2 ? Integer.MAX_VALUE : 12000;
				break;
			case FLY_SPEED:
				lowerCap = 0;
				upperCap = isPlayer == 2 ? Integer.MAX_VALUE : 16000;
				break;
			case MAXHP:
			case MAXMP:
				lowerCap = 0;
				upperCap = Integer.MAX_VALUE;
				break;
			case PVP_DEFEND_RATIO:
				upperCap = 900;
				break;
			case BOOST_DURATION_BUFF:
				upperCap = Integer.MAX_VALUE;
				break;
		}
		calculate(stat, lowerCap, upperCap);
	}

	private static void calculate(Stat2 stat2, int lowerCap, int upperCap) {
		if (stat2.getCurrent() > upperCap) {
			stat2.setBonus(upperCap - stat2.getBase());
		}
		else if (stat2.getCurrent() < lowerCap) {
			stat2.setBonus(lowerCap - stat2.getBase());
		}
	}
}