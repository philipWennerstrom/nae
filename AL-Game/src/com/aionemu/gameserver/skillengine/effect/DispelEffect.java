package com.aionemu.gameserver.skillengine.effect;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.aionemu.gameserver.skillengine.model.DispelType;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.SkillTargetSlot;

/**
 * @author ATracer
 */
public class DispelEffect extends EffectTemplate {

	@XmlElement(type = Integer.class)
	protected List<Integer> effectids;
	@XmlElement
	protected List<String> effecttype;
	@XmlElement
	protected List<String> slottype;
	@XmlAttribute
	protected DispelType dispeltype;
	@XmlAttribute
	protected Integer value;

	@Override
	public void applyEffect(Effect effect) {
		if (effect.getEffected() == null || effect.getEffected().getEffectController() == null)
			return;

		if (dispeltype == null)
			return;

		if ((dispeltype == DispelType.EFFECTID || dispeltype == DispelType.EFFECTIDRANGE) && 
			effectids == null)
			return;

		if (dispeltype == DispelType.EFFECTTYPE && effecttype == null)
			return;
		
		if (dispeltype == DispelType.SLOTTYPE && slottype == null)
			return;

		switch (dispeltype) {
			case EFFECTID:
				for (Integer effectId : effectids) {
					effect.getEffected().getEffectController().removeEffectByEffectId(effectId);
				}
				break;
			case EFFECTIDRANGE:
				for (int i = effectids.get(0); i <= effectids.get(1); i++) {
					effect.getEffected().getEffectController().removeEffectByEffectId(i);
				}
				break;
			case EFFECTTYPE:
				for (String type : effecttype) {
					EffectType temp = null;
					try {
						temp = EffectType.valueOf(type);
					} catch (Exception e) {
						log.error("wrong effecttype in dispeleffect "+type);
					}
					if (temp != null)
						effect.getEffected().getEffectController().removeEffectByEffectType(temp);
				}
				break;
			case SLOTTYPE:
				for (String type : slottype) {
					effect.getEffected().getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.valueOf(type));
				}
				break;
		}
	}
}