package entity;

import java.awt.Color;
import java.util.Set;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager.SpriteType;

/**
 * Implements a ship, to be controlled by the player.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class Ship extends Entity {

	/** Time between shots. */
	private static final int SHOOTING_INTERVAL = 750;
	/** Speed of the bullets shot by the ship. */
	private static final int BULLET_SPEED = -6;
	/** Distance in px from the ship's center to each barrel of a two-way ship. */
	private static final int BARREL_OFFSET = 8;
	/** Types of player ships. Each type has its own speed and sprite. */
	public enum ShipType {
		/** Standard ship. */
		STANDARD(2, SpriteType.Ship),
		/** Ship with a high movement speed. */
		FAST_MOVE(4, SpriteType.ShipFastMove),
		/** Ship that shoots a bullet from each of its two barrels. */
		TWO_WAY(2, SpriteType.ShipTwoWay);

		/** Movement of the ship for each unit of time. */
		private final int speed;
		/** Sprite of the ship while it is not destroyed. */
		private final SpriteType idleSprite;

		/**
		 * Constructor, establishes the properties of the ship type.
		 * 
		 * @param speed
		 *            Movement of the ship for each unit of time.
		 * @param idleSprite
		 *            Sprite of the ship while it is not destroyed.
		 */
		ShipType(final int speed, final SpriteType idleSprite) {
			this.speed = speed;
			this.idleSprite = idleSprite;
		}
	}

	/** Type of this ship. */
	private final ShipType type;
	
	/** Minimum time between shots. */
	private Cooldown shootingCooldown;
	/** Time spent inactive between hits. */
	private Cooldown destructionCooldown;

	/**
	 * Constructor, establishes the ship's properties.
	 * 
	 * @param positionX
	 *            Initial position of the ship in the X axis.
	 * @param positionY
	 *            Initial position of the ship in the Y axis.
	 */
	public Ship(final int positionX, final int positionY) {
		this(positionX, positionY, ShipType.STANDARD);
	}

	/**
	 * Constructor, establishes the ship's properties.
	 * 
	 * @param positionX
	 *            Initial position of the ship in the X axis.
	 * @param positionY
	 *            Initial position of the ship in the Y axis.
	 * @param type
	 *            Type of the ship, which defines its speed and sprite.
	 */
	public Ship(final int positionX, final int positionY, final ShipType type) {
		super(positionX, positionY, 13 * 2, 8 * 2, Color.GREEN);

		this.type = type;
		this.spriteType = type.idleSprite;
		this.shootingCooldown = Core.getCooldown(SHOOTING_INTERVAL);
		this.destructionCooldown = Core.getCooldown(1000);
	}

	/**
	 * Moves the ship speed uni ts right, or until the right screen border is
	 * reached.
	 */
	public final void moveRight() {
		this.positionX += this.type.speed;
	}

	/**
	 * Moves the ship speed units left, or until the left screen border is
	 * reached.
	 */
	public final void moveLeft() {
		this.positionX -= this.type.speed;
	}

	/**
	 * Shoots a bullet upwards. A two-way ship shoots one bullet from each of
	 * its two barrels instead.
	 * 
	 * @param bullets
	 *            List of bullets on screen, to add the new bullet.
	 * @return Checks if the bullet was shot correctly.
	 */
	public final boolean shoot(final Set<Bullet> bullets) {
		if (this.shootingCooldown.checkFinished()) {
			this.shootingCooldown.reset();
			final int centerX = positionX + this.width / 2;
			if (this.type == ShipType.TWO_WAY) {
				bullets.add(BulletPool.getBullet(centerX - BARREL_OFFSET,
						positionY, BULLET_SPEED));
				bullets.add(BulletPool.getBullet(centerX + BARREL_OFFSET,
						positionY, BULLET_SPEED));
			} else {
				bullets.add(BulletPool.getBullet(centerX, positionY,
						BULLET_SPEED));
			}
			return true;
		}
		return false;
	}

	/**
	 * Updates status of the ship.
	 */
	public final void update() {
		if (!this.destructionCooldown.checkFinished())
			this.spriteType = SpriteType.ShipDestroyed;
		else
			this.spriteType = this.type.idleSprite;
	}

	/**
	 * Switches the ship to its destroyed state.
	 */
	public final void destroy() {
		this.destructionCooldown.reset();
	}

	/**
	 * Checks if the ship is destroyed.
	 * 
	 * @return True if the ship is currently destroyed.
	 */
	public final boolean isDestroyed() {
		return !this.destructionCooldown.checkFinished();
	}

	/**
	 * Getter for the ship's speed.
	 * 
	 * @return Speed of the ship.
	 */
	public final int getSpeed() {
		return this.type.speed; 
	}
	
	/**
	 * Getter for the ship's type.
	 * 
	 * @return Type of the ship.
	 */
	public final ShipType getType() {
		return this.type;
	}
}
