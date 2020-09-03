package com.aionemu.gameserver.model.templates.spawns;

import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author xTz
 * @modified Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SpawnSpotTemplate")
public class SpawnSpotTemplate {

	@XmlAttribute(name = "state")
	private Integer state = 0;
	
	@XmlAttribute(name = "anchor")
	private String anchor;
	
	@XmlAttribute(name = "fly")
	private Integer fly = 0;

	@XmlAttribute(name = "walker_index")
	private Integer walkerIdx;
	
	@XmlAttribute(name = "walker_id")
	private String walkerId;
	
	@XmlAttribute(name = "random_walk")
	private Integer randomWalk = 0;
	
	@XmlAttribute(name = "static_id")
	private Integer staticId = 0;
	
	@XmlAttribute(name = "h", required = true)
	private byte h;
	
	@XmlAttribute(name = "z", required = true)
	private float z;

	@XmlAttribute(name = "y", required = true)
	private float y;
	
	@XmlAttribute(name = "x", required = true)
	private float x;

	@XmlElement(name = "temporary_spawn")
	private TemporarySpawn temporaySpawn;

	@XmlElement(name = "model")
	private SpawnModel model;

	public SpawnSpotTemplate() {
	}
	
	private static final Integer ZERO = new Integer(0);
	
	void beforeMarshal(Marshaller marshaller) {
		if (ZERO.equals(staticId))
			staticId = null;
		if (ZERO.equals(fly))
			fly = null;
		if (ZERO.equals(randomWalk))
			randomWalk = null;
		if (ZERO.equals(state))
			state = null;
		if (ZERO.equals(walkerIdx))
			walkerIdx = null;
	}
	
	void afterMarshal(Marshaller marshaller) {
		if (staticId == null)
			staticId = 0;
		if (fly == null)
			fly = 0;
		if (randomWalk == null)
			randomWalk = 0;
		if (state == null)
			state = 0;
		if (walkerIdx == null)
			walkerIdx = 0;
	}

	public SpawnSpotTemplate(float x, float y, float z, byte h, int randomWalk, String walkerId, Integer walkerIndex) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.h = h;
		if (randomWalk > 0)
			this.randomWalk = randomWalk;
		this.walkerId = walkerId;
		this.walkerIdx = walkerIndex;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public byte getHeading() {
		return h;
	}

	public int getStaticId() {
		return staticId;
	}

	public void setStaticId(int staticId) {
		this.staticId = staticId;
	}

	public String getWalkerId() {
		return walkerId;
	}
	
	public void setWalkerId(String walkerId) {
		this.walkerId = walkerId;
	}
	
	public int getWalkerIndex() {
		if (walkerIdx == null)
			return 0;
		return walkerIdx;
	}

	public int getRandomWalk() {
		return randomWalk;
	}

	public int getFly() {
		return fly;
	}

	public String getAnchor() {
		return anchor;
	}

	public SpawnModel getModel() {
		return model;
	}
	
	public int getState() {
		if (state == null)
			return 0;
		return state;
	}

	public TemporarySpawn getTemporarySpawn() {
		return temporaySpawn;
	}
}