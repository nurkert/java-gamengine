package eu.nurkert.gamengine.logic.events.essential;

import java.util.ArrayList;

import eu.nurkert.gamengine.logic.events.GEvent;
import eu.nurkert.gamengine.logic.GObject;

public class HandleObjectsGEvent extends GEvent {

	ArrayList<GObject> objects;
	
	public HandleObjectsGEvent(double diff, ArrayList<GObject> objects) {
		super(diff);
		this.objects = objects;
	}

	public ArrayList<GObject> getObjects() {
		return objects;
	}
}
