package com.aionemu.gameserver.questEngine.handlers.models.xmlQuest.events;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.questEngine.handlers.models.Monster;
import com.aionemu.gameserver.questEngine.handlers.models.xmlQuest.operations.QuestOperations;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Mr. Poke, modified Bobobear
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OnKillEvent", propOrder = { "monster", "complite" })
public class OnKillEvent extends QuestEvent {

	@XmlElement(name = "monster")
	protected List<Monster> monster;
	protected QuestOperations complite;

	public List<Monster> getMonsters() {
		if (monster == null) {
			monster = new ArrayList<Monster>();
		}
		return this.monster;
	}

	public boolean operate(QuestEnv env) {
		if (monster == null || !(env.getVisibleObject() instanceof Npc))
			return false;

		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(env.getQuestId());
		if (qs == null)
			return false;

		Npc npc = (Npc) env.getVisibleObject();
		for (Monster m : monster) {
			if (m.getNpcIds().contains(npc.getNpcId())) {
				int var = qs.getQuestVarById(m.getVar());
				if (var >= (m.getStartVar() == null ? 0 : m.getStartVar()) && var < m.getEndVar()) {
					qs.setQuestVarById(m.getVar(), var + 1);
					PacketSendUtility.sendPacket(env.getPlayer(), new SM_QUEST_ACTION(env.getQuestId(), qs.getStatus(), qs
						.getQuestVars().getQuestVars()));
				}
			}
		}

		if (complite != null) {
			for (Monster m : monster) {
				if (qs.getQuestVarById(m.getVar()) != qs.getQuestVarById(m.getVar()))
					return false;
			}
			complite.operate(env);
		}
		return false;
	}
}