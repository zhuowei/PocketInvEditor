package net.zhuoweizhang.pocketinveditor.tileentity;

import net.zhuoweizhang.pocketinveditor.util.Vector3f;

public class TileEntity {

	private String id = null;

	private int x = 0;
	private int y = 0;
	private int z = 0;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public double distanceSquaredTo(Vector3f other) {
		return Math.pow(other.x - this.x, 2) + Math.pow(other.y - this.y, 2) + Math.pow(other.z - this.z, 2);
	}

	@Override
	public String toString() {
		return this.id + ": X: " + x + " Y: " + y + " Z: " + z;
	}
}
