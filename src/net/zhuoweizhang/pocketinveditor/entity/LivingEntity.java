package net.zhuoweizhang.pocketinveditor.entity;

public class LivingEntity extends Entity {

	private short attackTime, deathTime, hurtTime;

	private short health;

	public LivingEntity() {
		super();
		this.health = (short) this.getMaxHealth();
	}

	public short getAttackTime() {
		return attackTime;
	}

	public void setAttackTime(short attackTime) {
		this.attackTime = attackTime;
	}

	public short getDeathTime() {
		return deathTime;
	}

	public void setDeathTime(short deathTime) {
		this.deathTime = deathTime;
	}

	public short getHurtTime() {
		return hurtTime;
	}

	public void setHurtTime(short hurtTime) {
		this.hurtTime = hurtTime;
	}

	public short getHealth() {
		return health;
	}

	public void setHealth(short health) {
		this.health = health;
	}

	public int getMaxHealth() {
		return 20;
	}
}

