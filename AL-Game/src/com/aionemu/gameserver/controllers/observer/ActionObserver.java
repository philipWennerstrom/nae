package com.aionemu.gameserver.controllers.observer;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.Skill;

/**
 * @author ATracer
 */
public class ActionObserver {

	private AtomicBoolean used;

	private ObserverType observerType;

	public ActionObserver(ObserverType observerType) {
		this.observerType = observerType;
	}

	/**
	 * Make this observer usable exactly one time
	 */
	public void makeOneTimeUse() {
		used = new AtomicBoolean(false);
	}

	/**
	 * Try to use this observer. Will return true only once.
	 * 
	 * @return
	 */
	public boolean tryUse() {
		return used.compareAndSet(false, true);
	}

	/**
	 * @return the observerType
	 */
	public ObserverType getObserverType() {
		return observerType;
	}

	public void moved() {
	};

	/**
	 * @param creature
	 */
	public void attacked(Creature creature) {
	};

	/**
	 * @param creature
	 */
	public void attack(Creature creature) {
	};

	/**
	 * @param item
	 * @param owner
	 */
	public void equip(Item item, Player owner) {
	};

	/**
	 * @param item
	 * @param owner
	 */
	public void unequip(Item item, Player owner) {
	};

	/**
	 * @param skill
	 */
	public void skilluse(Skill skill) {
	};
	
	/**
	 * @param creature
	 */
	public void died(Creature creature) {
	};

	/**
	 * @param creature
	 * @param dotEffect 
	 */
	public void dotattacked(Creature creature, Effect dotEffect) {
	};
	
	/**
	 * 
	 * @param item
	 */
	public void itemused(Item item) {
	};
	
	/**
	 * 
	 * @param npc
	 */
	public void npcdialogrequested(Npc npc) {
	};
	
	/**
	 * 
	 * @param state
	 */
	public void abnormalsetted(AbnormalState state) {
	};
	
	/**
	 * 
	 * @param 
	 */
	public void summonrelease() {
	};
}