package eu.nurkert.gamengine.logic.entities;

import eu.nurkert.gamengine.logic.GObject;

public interface GMonster {

	public boolean hasTarget();
	public void setTarget(GObject object);
	public GObject getTarget();
}
