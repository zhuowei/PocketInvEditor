package net.zhuoweizhang.pocketinveditor.entity;

public class PigZombie extends Monster {

	private short anger = 0;

	@Override
	public int getMaxHealth() {
		return 20;
	}

	public short getAnger() {
		return anger;
	}

	public void setAnger(short anger) {
		this.anger = anger;
	}

}
