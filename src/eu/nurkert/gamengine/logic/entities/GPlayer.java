package eu.nurkert.gamengine.logic.entities;

import eu.nurkert.gamengine.logic.GContent;
import eu.nurkert.gamengine.logic.GLocation;
import eu.nurkert.gamengine.logic.GObject;
import eu.nurkert.gamengine.logic.events.GEventHandler;

public abstract class GPlayer extends GEntity implements GObject.Collidable, GEventHandler.GEventListener {

	GLocation viewCenter;

	public GPlayer(GLocation location, GLocation viewCenter, double speed, double hitRadius) {
		super(location, GLayor.MAIN4, speed, hitRadius);
		this.viewCenter = viewCenter;

//		GEventHandler.register(this);
	}

	/**
	 * 
	 * Viewcenter/("camera" center) follows player entity smooth
	 */
	@Override
	public void handle(double diff, GContent world) {
		super.handle(diff, world);

		double dX = ((getLocation().getX() - viewCenter.getX()) / 4) * diff * 10;
		double dY = ((getLocation().getY() - viewCenter.getY()) / 4) * diff * 10;
		viewCenter.add(dX, dY);
	}


//	public void collide(GObject object) {
////		damage(2);
//	}

	
}
