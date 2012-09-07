package net.zhuoweizhang.pocketinveditor.entity;


public class Projectile extends Entity {

	private short xTile = (short) 0;

	private short yTile = (short) 0;

	private short zTile = (short) 0;

	private boolean inGround = false;

	private byte shake = (byte) 0;

	private byte inTile = (byte) 0;

	public short getXTile() {
		return xTile;
	}

	public void setXTile(short xTile) {
		this.xTile = xTile;
	}

	public short getYTile() {
		return yTile;
	}

	public void setYTile(short yTile) {
		this.yTile = yTile;
	}

	public short getZTile() {
		return zTile;
	}

	public void setZTile(short zTile) {
		this.zTile = zTile;
	}

	public boolean isInGround() {
		return inGround;
	}

	public void setInGround(boolean inGround) {
		this.inGround = inGround;
	}

	public byte getShake() {
		return shake;
	}

	public void setShake(byte shake) {
		this.shake = shake;
	}

	public byte getInBlock() {
		return inTile;
	}

	public void setInBlock(byte inTile) {
		this.inTile = inTile;
	}

}
