package net.zhuoweizhang.pocketinveditor.entity;

public class Arrow extends Projectile {

	private boolean player = false;

	private byte inData = (byte) 0;

	public byte getInData() {
		return inData;
	}

	public void setInData(byte inData) {
		this.inData = inData;
	}

	public boolean isShotByPlayer() {
		return player;
	}

	public void setShotByPlayer(boolean player) {
		this.player = player;
	}

}
