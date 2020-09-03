package com.aionemu.gameserver.model.templates.mail;

import javax.xml.bind.annotation.*;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Body")
@XmlSeeAlso({ com.aionemu.gameserver.model.templates.mail.MailPart.class })
public class Body extends MailPart {

	@XmlAttribute(name = "type")
	protected MailPartType type;

	@Override
	public MailPartType getType() {
		if (type == null)
			return MailPartType.BODY;
		return type;
	}

	@Override
	public String getParamValue(String name) {
		return "";
	}
}