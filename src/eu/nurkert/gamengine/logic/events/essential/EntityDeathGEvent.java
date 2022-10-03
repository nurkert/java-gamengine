package eu.nurkert.gamengine.logic.events.essential;

import eu.nurkert.gamengine.logic.events.GEvent;
import eu.nurkert.gamengine.logic.entities.GEntity;

public class EntityDeathGEvent extends GEvent {
	
	GEntity entity;

	public EntityDeathGEvent(GEntity entity) {
		super(-1);
		this.entity = entity;
	}

	public GEntity getEntity() {
		return entity;
	}
}
