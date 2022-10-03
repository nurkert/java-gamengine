package eu.nurkert.gamengine.logic.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;

import eu.nurkert.gamengine.logic.GContent;
import eu.nurkert.gamengine.logic.GLocation;
import eu.nurkert.gamengine.logic.GObject;
import eu.nurkert.gamengine.logic.GVelocity;
import eu.nurkert.gamengine.logic.events.GEventHandler;
import eu.nurkert.gamengine.visual.GParticle;

public abstract class GEntity extends GObject implements GEventHandler.GEventListener, GObject.Collidable {

	GVelocity velocity;
	protected double speed;
	private boolean isDead;
	double health, maxHealth, hitRadius;

	/**
	 * @param location The position of the entity
	 * @param layor    On which layer the entity is to be drawed
	 * @param speed    The maximum speed of the entity
	 */
	public GEntity(GLocation location, GLayor layor, double speed, double hitRadius) {
		super(location, layor, true, false);

		this.speed = speed;
		isDead = false;
		velocity = new GVelocity();
		health = 100D;
		maxHealth = 100D;
		this.hitRadius = hitRadius;
		GEventHandler.register(this);
	}

	public double getHitRadius() {
		return hitRadius;
	}

	public void setHitRadius(double hitRadius) {
		this.hitRadius = hitRadius;
	}

	@Override
	public void handle(double diff, GContent world) {
		velocity.setPower(velocity.getPower() - velocity.getPower() * diff * 2.5);
		double addX = velocity.getX() * diff * 10;
		double addY = velocity.getY() * diff * 10;
		getLocation().addX(addX);
		getLocation().addY(addY);
	}

	@Override
	public Image getTexture() {
		return rotate(super.texture.getBufImg(), getLocation().getAngle());
	}

	@Override
	public boolean wouldCollide(GObject object) {
		return getLocation().distance(object.getLocation()) < (getHitRadius() + ((Collidable) object).getHitRadius());
	}

	@Override
	public void collide(GObject object) {
//		if(!((object instanceof Item) && (this instanceof Item)))
		getVelocity().add((getLocation().getX() - object.getLocation().getX()) / 2,
				(getLocation().getY() - object.getLocation().getY()) / 2);
	}

	protected static BufferedImage rotate(BufferedImage bimg, double angle) {
		if (bimg == null)
			return null;

		int w = bimg.getWidth();
		int h = bimg.getHeight();

		BufferedImage rotated = new BufferedImage(w, h, bimg.getType());
		Graphics2D graphic = rotated.createGraphics();
		graphic.rotate(angle, w / 2, h / 2);
		graphic.drawImage(bimg, null, 0, 0);
		graphic.dispose();
		return rotated;
	}

	public GVelocity getVelocity() {
		return velocity;
	}

	public void addVelocity(double x, double y) {
		velocity.add(x, y);
	}

	public void addVelocity(float angle, double power) {
		velocity.add(angle, power);

	}

	public void setVelocity(GVelocity velocity) {
		this.velocity = velocity;
	}

	public boolean isDead() {
		return isDead;
	}

	public void kill() {
		if (isDead)
			return;

		this.isDead = true;

		setUseless(true);

		//getLocation().getWorld().takeAmount(getType());

		for (int i = 0; i < Math.pow(getHitRadius(), 2) / 2; i++) {
			GLocation loc = getLocation().copy();
			double radius = new Random().nextDouble() * getHitRadius();
			double addX = new GVelocity(loc.getAngle(), radius).getX();
			double addY = new GVelocity(loc.getAngle(), radius).getY();
			loc.addX(addX);
			loc.addY(addY);
			loc.setAngle((float) (new Random().nextFloat() * 2 * Math.PI));
//				getLocation().getWorld()
//						.place(new GParticle(
//								new Color(45 + new Random().nextInt(10) - 5, 190 + new Random().nextInt(10) - 5,
//										9 + new Random().nextInt(10) - 5),
//								loc, new Random().nextInt(5) + 2, new Random().nextInt(400) + 100));
			//getLocation().getWorld().place(new GParticle(getColorOfTexture(), loc, new Random().nextInt(5) + 2,
					//new Random().nextInt(600) + 100));

		}

	}

	protected Color getColorOfTexture() {
		Random random = new Random();
		BufferedImage image = texture.getBufImg();
		Color color = printPixelARGB(image.getRGB(random.nextInt(image.getWidth()), random.nextInt(image.getHeight())));
		if (color.getAlpha() < 255)
			return getColorOfTexture();
		else
			return color;
	}

	private Color printPixelARGB(int pixel) {
		int alpha = (pixel >> 24) & 0xff;
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel) & 0xff;
		return new Color(red, green, blue, alpha);
	}

	public void setAlive() {
		this.isDead = false;
	}

	@Override
	public boolean isMoveing() {
		return getVelocity().getPower() > 0;
	}

	public double getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(double maxHealth) {
		this.maxHealth = maxHealth;
	}

	public double getHealth() {
		return health;
	}

	long lastDamage = System.currentTimeMillis();

	public void damage(double damage) {
		if (System.currentTimeMillis() - lastDamage > 500) {
			setHealth(getHealth() - damage);
			lastDamage = System.currentTimeMillis();
		}

	}

	public void setHealth(double health) {
		this.health = health;
		if (health  > maxHealth)
			health = maxHealth;
		else if (health < 0)
			kill();
	}

}
