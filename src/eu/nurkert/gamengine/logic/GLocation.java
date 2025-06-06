package eu.nurkert.gamengine.logic;

import java.util.Random;

/**
 * This class previously depended on the example game implementation by using
 * {@code eu.nurkert.ImmuneTillDeath.Game.World}. In order to make the engine
 * self contained, the dependency was replaced with the generic
 * {@link eu.nurkert.gamengine.logic.GContent} type so that any game can supply
 * its own world implementation.
 */
import eu.nurkert.gamengine.logic.GContent;

public class GLocation {

	double x, y;
	float angle;
       GContent world;

       public GLocation(double x, double y, float angle, GContent world) {
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.world = world;
	}

       public GLocation(double x, double y, GContent world) {
		this.x = x;
		this.y = y;
		this.angle = 0F;
		this.world = world;
		angle = (float) (new Random().nextFloat() * 2 * Math.PI);
	}

	public double getX() {
		if (x == 0)
			return 0.0001;
		return x;
	}

	public GLocation setX(double x) {
		this.x = x;
		return this;
	}

	public GLocation addX(double x) {
		this.x += x;
		return this;
	}

	public double getY() {
		if (y == 0)
			return 0.0001;
		return y;
	}

	public GLocation setY(double y) {
		this.y = y;
		return this;
	}

	public GLocation addY(double y) {
		this.y += y;
		return this;
	}

	public GLocation add(double x, double y) {
		this.x += x;
		this.y += y;
		return this;
	}

	public void addAnlge(float angle) {
		this.angle += angle;
		fixAngle();
	}

	public float getAngle() {
		return angle;
	}

	public float getInvertAngle() {
		float invert = (float) (angle - Math.PI);
		if (invert < 0)
			invert += Math.PI * 2;
		else
			invert -= Math.PI * 2;
		return invert;
	}

	public GLocation setAngle(float angle) {
		this.angle = angle;
		fixAngle();
		return this;
	}

	private float fixAngle() {
		angle = (float) Math.atan2(Math.sin(angle), Math.cos(angle));
		return angle;
	}
	
       public GContent getWorld() {
		return world;
	}

	public double distance(GLocation loc) {
		double dX = Math.abs(getX() - loc.getX());
		double dY = Math.abs(getY() - loc.getY());
		return Math.sqrt(dX * dX + dY * dY);
	}

	public GLocation copy() {
		return new GLocation(x, y, world);
	}
}
