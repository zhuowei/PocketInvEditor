package net.zhuoweizhang.pocketinveditor.entity;

import net.zhuoweizhang.pocketinveditor.util.Vector;

public class Entity {

	private Vector location = new Vector(0, 0, 0);

	private float pitch;

	private float yaw;

	private Vector motion = new Vector(0, 0, 0);

	private float fallDistance;

	private short fire;

	private short air = 300;

	private boolean onGround = false;

	private int typeId = 0;

	public Vector getLocation() {
		return location;
	}

	public void setLocation(Vector location) {
		this.location = location;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public Vector getVelocity() {
		return motion;
	}

	public void setVelocity(Vector velocity) {
		this.motion = velocity;
	}

	public float getFallDistance() {
		return fallDistance;
	}

	public void setFallDistance(float fallDistance) {
		this.fallDistance = fallDistance;
	}

	public short getFireTicks() {
		return fire;
	}

	public void setFireTicks(short fire) {
		this.fire = fire;
	}

	public short getAirTicks() {
		return air;
	}

	public void setAirTicks(short air) {
		this.air = air;
	}

	public boolean isOnGround() {
		return onGround;
	}

	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}

	public int getEntityTypeId() {
		return typeId;
	}

	public void setEntityTypeId(int typeId) {
		this.typeId = typeId;
	}

	public EntityType getEntityType() {
		EntityType type = EntityType.getById(typeId);
		if (type == null) type = EntityType.UNKNOWN;
		return type;
	}
}
