package net.zhuoweizhang.pocketinveditor.entity;

public class Sheep extends Animal {

	private boolean sheared = false;

	private byte color = 0;

	public boolean isSheared() {
		return sheared;
	}

	public void setSheared(boolean sheared) {
		this.sheared = sheared;
	}

	public byte getColor() {
		return color;
	}

	public void setColor(byte color) {
		this.color = color;
	}

	@Override
	public int getMaxHealth() {
		return 8;
	}

}
