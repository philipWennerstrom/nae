package com.aionemu.gameserver.configs.schedule;

import com.aionemu.commons.utils.xml.JAXBUtil;
import java.io.File;
import java.util.List;
import javax.xml.bind.annotation.*;
import org.apache.commons.io.FileUtils;

/**
 * @author SoulKeeper, Source
 */
@XmlRootElement(name = "siege_schedule")
@XmlAccessorType(XmlAccessType.FIELD)
public class SiegeSchedule {

	@XmlElement(name = "fortress", required = true)
	private List<Fortress> fortressesList;
	@XmlElement(name = "source", required = true)
	private List<Source> sourcesList;

	public List<Fortress> getFortressesList() {
		return fortressesList;
	}

	public void setFortressesList(List<Fortress> fortressList) {
		this.fortressesList = fortressList;
	}

	public List<Source> getSourcesList() {
		return sourcesList;
	}

	public void setSourcesList(List<Source> sourceList) {
		this.sourcesList = sourceList;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "fortress")
	public static class Fortress {

		@XmlAttribute(required = true)
		private int id;
		@XmlElement(name = "siegeTime", required = true)
		private List<String> siegeTimes;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public List<String> getSiegeTimes() {
			return siegeTimes;
		}

		public void setSiegeTimes(List<String> siegeTimes) {
			this.siegeTimes = siegeTimes;
		}

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "source")
	public static class Source {

		@XmlAttribute(required = true)
		private int id;
		@XmlElement(name = "siegeTime", required = true)
		private List<String> siegeTime;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public List<String> getSiegeTimes() {
			return siegeTime;
		}

		public void setSiegeTimes(List<String> siegeTime) {
			this.siegeTime = siegeTime;
		}

	}

	public static SiegeSchedule load() {
		SiegeSchedule ss;
		try {
			String xml = FileUtils.readFileToString(new File("./config/schedule/siege_schedule.xml"));
			ss = JAXBUtil.deserialize(xml, SiegeSchedule.class);
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to initialize sieges", e);
		}
		return ss;
	}
}