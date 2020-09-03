package com.aionemu.gameserver.controllers.effect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javolution.util.FastMap;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ABNORMAL_EFFECT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.skillengine.effect.EffectTemplate;
import com.aionemu.gameserver.skillengine.effect.EffectType;
import com.aionemu.gameserver.skillengine.model.DispelCategoryType;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.EffectResult;
import com.aionemu.gameserver.skillengine.model.SkillSubType;
import com.aionemu.gameserver.skillengine.model.SkillTargetSlot;
import com.aionemu.gameserver.skillengine.model.TransformType;
import com.aionemu.gameserver.taskmanager.tasks.PacketBroadcaster.BroadcastMode;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * @author ATracer modified by Wakizashi, Sippolo, Cheatkiller
 */
public class EffectController {

	private Creature owner;

	protected Map<String, Effect> passiveEffectMap = new FastMap<String, Effect>().shared();
	protected Map<String, Effect> noshowEffects = new FastMap<String, Effect>().shared();
	protected Map<String, Effect> abnormalEffectMap = new FastMap<String, Effect>().shared();

	private final Lock lock = new ReentrantLock();

	protected int abnormals;
	
	private boolean isUnderShield = false;
	
	public EffectController(Creature owner) {
		this.owner = owner;
	}

	/**
	 * @return the owner
	 */
	public Creature getOwner() {
		return owner;
	}
	
	/**
	 * @return the isUnderShield
	 */
	public boolean isUnderShield() {
		return isUnderShield;
	}

	
	/**
	 * @param isUnderShield the isUnderShield to set
	 */
	public void setUnderShield(boolean isUnderShield) {
		this.isUnderShield = isUnderShield;
	}
	
	/**
	 * @param effect
	 */
	public void addEffect(Effect effect) {
		Map<String, Effect> mapToUpdate = getMapForEffect(effect);

		lock.lock();
		try {
			
			if (effect.isPassive()) {
				boolean useEffectId = true;
				Effect existingEffect = mapToUpdate.get(effect.getStack());
				if (existingEffect != null && existingEffect.isPassive()) {
					// check stack level
					if (existingEffect.getSkillStackLvl() > effect.getSkillStackLvl()){
						return;
					}
					
					// check skill level (when stack level same)
					if (existingEffect.getSkillStackLvl() == effect.getSkillStackLvl()
						&& existingEffect.getSkillLevel() > effect.getSkillLevel()){
						return;
					}
					existingEffect.endEffect();
					useEffectId = false;
				}
				
				if (useEffectId) {
					/**
					 * idea here is that effects with same effectId shouldnt stack
					 * effect with higher basiclvl takes priority
					 */ 
					Iterator<Effect> iter2 = mapToUpdate.values().iterator();
					while (iter2.hasNext())	{
						Effect ef = iter2.next();
						if (ef.getTargetSlot() == effect.getTargetSlot())	{
							for (EffectTemplate et : ef.getEffectTemplates())	{
								if (et.getEffectid() == 0)
									continue;
								for (EffectTemplate et2 : effect.getEffectTemplates())	{
									if (et2.getEffectid() == 0)
										continue;
									if (et.getEffectid() == et2.getEffectid()) {
										if (et.getBasicLvl() > et2.getBasicLvl())
											return;
										else
											ef.endEffect();
									}
								}
							}
						}
					}
				}
			}
			
			Effect conflictedEffect = findConflictedEffect(mapToUpdate, effect);
			if (conflictedEffect != null) {
				conflictedEffect.endEffect();
			}
			
			//max 3 aura effects
			if (effect.isToggle()) {
				int mts = effect.getSkillSubType() == SkillSubType.CHANT ? 3 : 1;
        if (mapToUpdate.size() >= mts) {
				 Iterator<Effect> iter = mapToUpdate.values().iterator();
				 Effect nextEffect = iter.next();
				 nextEffect.endEffect();
				 iter.remove();
        }
			}
			
			//max 4 chants
			if (effect.isChant()) {
				Collection<Effect> chants = this.getChantEffects();
				if (chants.size() >= 4) {
					Iterator<Effect> chantIter = chants.iterator();
					chantIter.next().endEffect();
				}
			}
			
			if (!effect.isPassive()) {
			  if (searchConflict(effect))
				  return;
		   checkEffectCooldownId(effect);
			}
			
			mapToUpdate.put(effect.getStack(), effect);
			
		}
		finally {
			lock.unlock();
		}

		// ? move into lock area
		effect.startEffect(false);

		if (!effect.isPassive()) {
			broadCastEffects();
		}
	}

	/**
	 * @param mapToUpdate
	 * @param effect
	 * @return
	 */
	private final Effect findConflictedEffect(Map<String, Effect> mapToUpdate, Effect newEffect) {
		int conflictId = newEffect.getSkillTemplate().getConflictId();
		if(conflictId == 0){
			return null;
		}
		for(Effect effect : mapToUpdate.values()){
			if(effect.getSkillTemplate().getConflictId() == conflictId){
				return effect;
			}
		}
		return null;
	}

	/**
	 * @param effect
	 * @return
	 */
	private Map<String, Effect> getMapForEffect(Effect effect) {
		if (effect.isPassive())
			return passiveEffectMap;

		if (effect.isToggle())
			return noshowEffects;

		return abnormalEffectMap;
	}

	/**
	 * @param stack
	 * @return abnormalEffectMap
	 */
	public Effect getAnormalEffect(String stack) {
		return abnormalEffectMap.get(stack);
	}

	/**
	 * @param skillId
	 * @return
	 */
	public boolean hasAbnormalEffect(int skillId) {
		Iterator<Effect> localIterator = this.abnormalEffectMap.values().iterator();
		while (localIterator.hasNext()) {
			Effect localEffect = localIterator.next();
			if (localEffect.getSkillId() == skillId)
				return true;
		}
		return false;
	}

	public void broadCastEffects() {
		owner.addPacketBroadcastMask(BroadcastMode.BROAD_CAST_EFFECTS);
	}

	/**
	 * Broadcasts current effects to all visible objects
	 */
	public void broadCastEffectsImp() {
		List<Effect> effects = getAbnormalEffects();
		PacketSendUtility.broadcastPacket(getOwner(), new SM_ABNORMAL_EFFECT(getOwner(), abnormals, effects));
	}

	/**
	 * Used when player see new player
	 * 
	 * @param player
	 */
	public void sendEffectIconsTo(Player player) {
		List<Effect> effects = getAbnormalEffects();
		PacketSendUtility.sendPacket(player, new SM_ABNORMAL_EFFECT(getOwner(), abnormals, effects));
	}

	/**
	 * @param effect
	 */
	public void clearEffect(Effect effect) {
		Map<String, Effect> mapForEffect = getMapForEffect(effect);
		mapForEffect.remove(effect.getStack());
		broadCastEffects();
	}

	/**
	 * Removes the effect by skillid.
	 * 
	 * @param skillid
	 */
	public void removeEffect(int skillid) {
		for (Effect effect : abnormalEffectMap.values()) {
			if (effect.getSkillId() == skillid) {
				effect.endEffect();
			}
		}

		for (Effect effect : passiveEffectMap.values()) {
			if (effect.getSkillId() == skillid) {
				effect.endEffect();
			}
		}

		for (Effect effect : noshowEffects.values()) {
			if (effect.getSkillId() == skillid) {
				effect.endEffect();
			}
		}
	}
	
	public void removeHideEffects(){
		for(Effect effect : abnormalEffectMap.values()){
			if(effect.isHideEffect() && owner.getVisualState() < 10){
				effect.endEffect();
				abnormalEffectMap.remove(effect.getStack());
			}
		}
	}

	/**
	 * Removes Paralyze effects from owner.
	 * 
	 */
	public void removeParalyzeEffects(){
		for(Effect effect : abnormalEffectMap.values()){
			if(effect.isParalyzeEffect()){
				effect.endEffect();
				abnormalEffectMap.remove(effect.getStack());
			}
		}
	}

	/**
	 * @param effectId
	 */
	public void removeEffectByEffectId(int effectId) {
		for (Effect effect : abnormalEffectMap.values()) {
			if (effect.containsEffectId(effectId)) {
				effect.endEffect();
			}
		}
	}

	/**
	 * Method used to calculate number of effects of given dispelcategory, targetslot and dispelLevel
	 * used only in DispelBuffCounterAtk, therefore rest of cases are skipped
	 * @param dispelCat
	 * @param targetSlot
	 * @param dispelLevel
	 * @return
	 */
	public int calculateNumberOfEffects(int dispelLevel) {
		int number = 0;
		
		for (Effect effect : abnormalEffectMap.values()) {
			DispelCategoryType dispelCat = effect.getDispelCategory();
			SkillTargetSlot tragetSlot = effect.getSkillTemplate().getTargetSlot();
			//effects with duration 86400000 cant be dispelled
			//TODO recheck
			if (effect.getDuration() >= 86400000 && !removebleEffect(effect))
				continue;
			
			if (effect.isSanctuaryEffect())
				continue;
			
			//check for targetslot, effects with target slot higher or equal to 2 cant be removed (ex. skillId: 11885)
			if (tragetSlot != SkillTargetSlot.BUFF && (tragetSlot != SkillTargetSlot.DEBUFF && dispelCat != DispelCategoryType.ALL) || effect.getTargetSlotLevel() >= 2)
				continue;

			switch (dispelCat) {
				case ALL:
				case BUFF://DispelBuffCounterAtkEffect
					if (effect.getReqDispelLevel() <= dispelLevel)
						number++;
					break;
			}
		}
					
		return number;
	}
	
	public void removeEffectByDispelCat(DispelCategoryType dispelCat, SkillTargetSlot targetSlot, int count, int dispelLevel, int power, boolean itemTriggered) {
		for (Effect effect : abnormalEffectMap.values()) {
			if (count == 0)
				break;
			//effects with duration 86400000 cant be dispelled
			//TODO recheck
			if (effect.getDuration() >= 86400000 && !removebleEffect(effect)) {
				continue;
			}
			
			if (effect.isSanctuaryEffect())
				continue;

			// If dispel is triggered by an item (ex. Healing Potion)
			// and debuff is unpottable, do not dispel
			if ((effect.getSkillTemplate().isUndispellableByPotions()) && itemTriggered)
					continue;
			
			//check for targetslot, effects with target slot level higher or equal to 2 cant be removed (ex. skillId: 11885)
			if (effect.getTargetSlot() != targetSlot.ordinal() || effect.getTargetSlotLevel() >= 2)
				continue;

			boolean remove = false;
			switch (dispelCat) {
				case ALL://DispelDebuffEffect
					if ((effect.getDispelCategory() == DispelCategoryType.ALL
						|| effect.getDispelCategory() == DispelCategoryType.DEBUFF_MENTAL
						|| effect.getDispelCategory() == DispelCategoryType.DEBUFF_PHYSICAL)
						&& effect.getReqDispelLevel() <= dispelLevel)
						remove = true;
					break;
				case DEBUFF_MENTAL://DispelDebuffMentalEffect
					if ((effect.getDispelCategory() == DispelCategoryType.ALL
						|| effect.getDispelCategory() == DispelCategoryType.DEBUFF_MENTAL)
						&& effect.getReqDispelLevel() <= dispelLevel)
						remove = true;
					break;
				case DEBUFF_PHYSICAL://DispelDebuffPhysicalEffect
					if ((effect.getDispelCategory() == DispelCategoryType.ALL
						|| effect.getDispelCategory() == DispelCategoryType.DEBUFF_PHYSICAL)
						&& effect.getReqDispelLevel() <= dispelLevel)
						remove = true;
					break;
				case BUFF://DispelBuffEffect or DispelBuffCounterAtkEffect
					if (effect.getDispelCategory() == DispelCategoryType.BUFF
						&& effect.getReqDispelLevel() <= dispelLevel)
						remove = true;
					break;
				case STUN:
					if (effect.getDispelCategory() == DispelCategoryType.STUN)
						remove = true;
					break;
				case NPC_BUFF://DispelNpcBuff
					if (effect.getDispelCategory() == DispelCategoryType.NPC_BUFF)
						remove = true;
					break;
				case NPC_DEBUFF_PHYSICAL://DispelNpcDebuff
					if (effect.getDispelCategory() == DispelCategoryType.NPC_DEBUFF_PHYSICAL)
						remove = true;
					break;
			}

			if (remove) {
				if (removePower(effect, power)) {
					effect.endEffect();
					abnormalEffectMap.remove(effect.getStack());
				}
				else if (owner instanceof Player)
					PacketSendUtility.sendPacket((Player)owner, SM_SYSTEM_MESSAGE.STR_MSG_NOT_ENOUGH_DISPELCOUNT);
				count--;
			}
			else if (owner instanceof Player)
				PacketSendUtility.sendPacket((Player)owner, SM_SYSTEM_MESSAGE.STR_MSG_NOT_ENOUGH_DISPELLEVEL);
		}
	}
	
	
	public void dispelBuffCounterAtkEffect(int count, int dispelLevel, int power) {
		for (Effect effect : abnormalEffectMap.values()) {
			DispelCategoryType dispelCat = effect.getDispelCategory();
			SkillTargetSlot tragetSlot = effect.getSkillTemplate().getTargetSlot();
			if (count == 0)
				break;
			
			if (effect.getDuration() >= 86400000 && !removebleEffect(effect)) {
				continue;
			}
			
			if (effect.isSanctuaryEffect())
				continue;

			if (tragetSlot != SkillTargetSlot.BUFF && (tragetSlot != SkillTargetSlot.DEBUFF && dispelCat != DispelCategoryType.ALL) || effect.getTargetSlotLevel() >= 2)
				continue;

			boolean remove = false;
			switch (dispelCat) {
				case ALL:
				case BUFF:
					if (effect.getReqDispelLevel() <= dispelLevel)
						remove = true;
					break;
			}

			if (remove) {
				if (removePower(effect, power)) {
					effect.endEffect();
					abnormalEffectMap.remove(effect.getStack());
				}
				else if (owner instanceof Player)
					PacketSendUtility.sendPacket((Player)owner, SM_SYSTEM_MESSAGE.STR_MSG_NOT_ENOUGH_DISPELCOUNT);
				count--;
			}
			else if (owner instanceof Player)
				PacketSendUtility.sendPacket((Player)owner, SM_SYSTEM_MESSAGE.STR_MSG_NOT_ENOUGH_DISPELLEVEL);
		}
	}
	
	private boolean removebleEffect(Effect effect) {
		int skillId = effect.getSkillId();
		switch (skillId) {
			case 20941:
			case 20942:
			case 19370:
			case 19371:
			case 19372:
			case 20530:
			case 20531:
			case 19345:
			case 19346:
				//TODO
				return true;
			default:
				return false;
		}
	}
	
	public void removeEffectByEffectType(EffectType effectType) {
		for (Effect effect : abnormalEffectMap.values()) {
			for (EffectTemplate et : effect.getSuccessEffect()) {
				if (effectType == et.getEffectType())
					effect.endEffect();
			}
		}
	}
	
	private boolean removePower(Effect effect, int power) {
		int effectPower = effect.removePower(power);
		
		if (effectPower <= 0)
			return true;
		else
			return false;
	}

	/**
	 * Removes the effect by skillid.
	 * 
	 * @param skillid
	 */
	public void removePassiveEffect(int skillid) {
		for (Effect effect : passiveEffectMap.values()) {
			if (effect.getSkillId() == skillid) {
				effect.endEffect();
			}
		}
	}

	/**
	 * @param skillid
	 */
	public void removeNoshowEffect(int skillid) {
		for (Effect effect : noshowEffects.values()) {
			if (effect.getSkillId() == skillid) {
				effect.endEffect();
			}
		}
	}

	/**
	 * @see TargetSlot
	 * @param targetSlot
	 */
	public void removeAbnormalEffectsByTargetSlot(SkillTargetSlot targetSlot) {
		for (Effect effect : abnormalEffectMap.values()) {
			if (effect.getTargetSlot() == targetSlot.ordinal()) {
				effect.endEffect();
			}
		}
	}

	/**
	 * Removes all effects from controllers and ends them appropriately Passive effect will not be removed
	 */
	public void removeAllEffects() {
		this.removeAllEffects(false);
	}

	public void removeAllEffects(boolean logout) {
		if (!logout) {
			Iterator<Map.Entry<String, Effect>> it = abnormalEffectMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Effect> entry = it.next();
				//TODO recheck - kecimis
				if (!entry.getValue().getSkillTemplate().isNoRemoveAtDie() && !entry.getValue().isXpBoost()) {
					entry.getValue().endEffect();
					it.remove();
				}
			}

			for (Effect effect : noshowEffects.values()) {
				effect.endEffect();
			}
			noshowEffects.clear();
		}	else {
			//remove all effects on logout
			for (Effect effect : abnormalEffectMap.values()) {
				effect.endEffect();
			}
			abnormalEffectMap.clear();
			for (Effect effect : noshowEffects.values()) {
				effect.endEffect();
			}
			noshowEffects.clear();
			for (Effect effect : passiveEffectMap.values()) {
				effect.endEffect();
			}
			passiveEffectMap.clear();
		}
	}

	/**
	 * Return true if skillId is present among creature's abnormals
	 */
	public boolean isAbnormalPresentBySkillId(int skillId) {
		for (Effect effect : abnormalEffectMap.values()) {
			if (effect.getSkillId() == skillId)
				return true;
		}
		return false;
	}

	public boolean isNoshowPresentBySkillId(int skillId) {
		for (Effect effect : noshowEffects.values()) {
			if (effect.getSkillId() == skillId)
				return true;
		}
		return false;
	}

	public boolean isPassivePresentBySkillId(int skillId) {
		for (Effect effect : passiveEffectMap.values()) {
			if (effect.getSkillId() == skillId)
				return true;
		}
		return false;
	}

	/**
	 * return true if creature is under Fear effect
	 */
	public boolean isUnderFear() {
		return isAbnormalSet(AbnormalState.FEAR);
	}

	public void updatePlayerEffectIcons() {
	}

	public void updatePlayerEffectIconsImpl() {
	}

	/**
	 * @return copy of anbornals list
	 */
	public List<Effect> getAbnormalEffects() {
		List<Effect> effects = new ArrayList<Effect>();
		Iterator<Effect> iterator = iterator();
		while (iterator.hasNext()) {
			Effect effect = iterator.next();
			if (effect != null)
				effects.add(effect);
		}
		return effects;
	}

	/**
	 * @return list of effects to display as top icons
	 */
	public Collection<Effect> getAbnormalEffectsToShow() {
		return Collections2.filter(abnormalEffectMap.values(), new Predicate<Effect>() {

			@Override
			public boolean apply(Effect effect) {
				return effect.getSkillTemplate().getTargetSlot() != SkillTargetSlot.NOSHOW;
			}
		});
	}
	
	public Collection<Effect> getAbnormalEffectsWithoutPassive() {
		return Collections2.filter(abnormalEffectMap.values(), new Predicate<Effect>() {

			@Override
			public boolean apply(Effect effect) {
				return !effect.isPassive();
			}
		});
	}
	
	public Collection<Effect> getChantEffects() {
		return Collections2.filter(abnormalEffectMap.values(), new Predicate<Effect>() {

			@Override
			public boolean apply(Effect effect) {
				return effect.isChant();
			}
		});
	}

	/**
	 * ABNORMAL EFFECTS
	 */

	public void setAbnormal(int mask) {
		owner.getObserveController().notifyAbnormalSettedObservers(AbnormalState.getStateById(mask));
		abnormals |= mask;
	}

	public void unsetAbnormal(int mask) {
		int count = 0;
		for (Effect effect : abnormalEffectMap.values()) {
			if ((effect.getAbnormals() & mask) == mask)
				count++;
		}
		if (count <= 1)
			abnormals &= ~mask;
	}

	/**
	 * Used for checking unique abnormal states
	 * 
	 * @param id
	 * @return
	 */
	public boolean isAbnormalSet(AbnormalState id) {
		return (abnormals & id.getId()) == id.getId();
	}

	/**
	 * Used for compound abnormal state checks
	 * 
	 * @param id
	 * @return
	 */
	public boolean isAbnormalState(AbnormalState id) {
		int state = abnormals & id.getId();
		return state > 0 && state <= id.getId();
	}

	public int getAbnormals() {
		return abnormals;
	}

	/**
	 * @return
	 */
	public Iterator<Effect> iterator() {
		return abnormalEffectMap.values().iterator();
	}

	public TransformType getTransformType() {
		for (Effect eff : getAbnormalEffects()) {
			if (eff.isDeityAvatar())
				return TransformType.AVATAR;
			else
				return eff.getTransformType();
		}
		return TransformType.NONE;		
	}
	
	public boolean isEmpty(){
		return abnormalEffectMap.isEmpty();
	}
	
	public void checkEffectCooldownId(Effect effect) {
		Collection<Effect> effects = this.getAbnormalEffectsWithoutPassive();
		int delayId = effect.getSkillTemplate().getCooldownId();
		int rDelay = 0;
		int size = 0;
		if (delayId == 1)
			return;
		switch (delayId) {
			case 2005:
				size = 2;
				break;
				//TODO
		}
		rDelay = delayId;
		
		if(delayId == rDelay && effects.size() >= size){
			int i = 0;
			Effect toRemove = null;
			Iterator<Effect> iter2 = effects.iterator();
			while(iter2.hasNext()){
				Effect nextEffect = iter2.next();
				if (nextEffect.getSkillTemplate().getCooldownId() == rDelay
					&& nextEffect.getTargetSlot() == effect.getTargetSlot()){
					i++;
					if(toRemove == null)
						toRemove = nextEffect;
				}
			}
			if(i >= size && toRemove != null) {
				toRemove.endEffect();
			}
		}
	}
	
	private boolean checkExtraEffect(Effect effect) {
	Effect existingEffect = getMapForEffect(effect).get(effect.getStack());
	  if (existingEffect != null) {
		  if (existingEffect.getDispelCategory() == DispelCategoryType.EXTRA && effect.getDispelCategory() == DispelCategoryType.EXTRA) {
		       existingEffect.endEffect();
		       return true;
		    }
	    }
		return false;
	}
	
	private boolean searchConflict(Effect effect) {
		Collection<Effect> effects = this.getAbnormalEffectsWithoutPassive();
		Iterator<Effect> iter2 = effects.iterator();
		if (priorityStigmaEffect(effect)) {
			return false;
		}
		if (checkExtraEffect(effect)) {
			return false;
		}
		while (iter2.hasNext())	{
			Effect ef = iter2.next();
			if (ef.getSkillSubType() == effect.getSkillSubType()) {
				for (EffectTemplate et : ef.getEffectTemplates())	{
					if (et.getEffectid() == 0)
						continue;
					for (EffectTemplate et2 : effect.getEffectTemplates())	{
						if (et2.getEffectid() == 0)
							continue;
						if (et.getEffectid() == et2.getEffectid()) {
							if (et.getBasicLvl() > et2.getBasicLvl()) {
								effect.setEffectResult(EffectResult.CONFLICT);
								return true;
							}
							else 
								ef.endEffect();
						}
					}
				}
		    }
		}
		return false;
	}
	
	private boolean priorityStigmaEffect(Effect effect) {
		Collection<Effect> effects = this.getAbnormalEffectsWithoutPassive();
		Iterator<Effect> iter2 = effects.iterator();
		while (iter2.hasNext())	{
			Effect ef = iter2.next();
			if (ef.getSkillTemplate().getStigmaType().getId() < effect.getSkillTemplate().getStigmaType().getId()
				&& ef.getTargetSlot() == effect.getTargetSlot() &&
				ef.getTargetSlotLevel() == effect.getTargetSlotLevel()) {
				for (EffectTemplate et : ef.getEffectTemplates())	{
					if (et.getEffectid() == 0)
						continue;
					for (EffectTemplate et2 : effect.getEffectTemplates())	{
						if (et2.getEffectid() == 0)
							continue;
						if (et.getEffectid() == et2.getEffectid()) {
								ef.endEffect();
							  return true;
						}
					}
				}
		    }
		}
		return false;
	}
}