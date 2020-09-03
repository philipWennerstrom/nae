package com.aionemu.gameserver.model.templates.materials;

import javax.xml.bind.annotation.*;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MeshMaterial")
public class MeshMaterial {

	@XmlAttribute(name = "material_id", required = true)
	protected int materialId;

	@XmlAttribute(name = "path", required = true)
	protected String path;

	@XmlAttribute(name = "zone")
	private String zoneName;

	public String getZoneName() {
		return zoneName;
	}
}