package com.aionemu.gameserver.questEngine.handlers.models.xmlQuest.operations;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.questEngine.model.QuestEnv;

/**
 * @author Mr. Poke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TakeItemOperation")
public class TakeItemOperation extends QuestOperation {

	@XmlAttribute(name = "item_id", required = true)
	protected int itemId;
	@XmlAttribute(required = true)
	protected int count;

	/*
	 * (non-Javadoc)
	 * @see
	 * com.aionemu.gameserver.questEngine.handlers.models.xmlQuest.operations.QuestOperation#doOperate(com.aionemu.gameserver
	 * .questEngine.model.QuestEnv)
	 */
	@Override
	public void doOperate(QuestEnv env) {
		env.getPlayer().getInventory().decreaseByItemId(itemId, count);
	}
}