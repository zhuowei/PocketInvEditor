package net.zhuoweizhang.pocketinveditor.util;

public class Vector3f {
	
	public float x, y, z;

	public Vector3f() {
		this(0f, 0f, 0f);
	}
	
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float getX() {
		return x;
	}

	public Vector3f setX(float x) {
		this.x = x;
		return this;
	}

	public float getY() {
		return y;
	}

	public Vector3f setY(float y) {
		this.y = y;
		return this;
	}

	public float getZ() {
		return z;
	}

	public Vector3f setZ(float z) {
		this.z = z;
		return this;
	}

	public int getBlockX() {
		return (int) x;
	}

	public int getBlockY() {
		return (int) y;
	}

	public int getBlockZ() {
		return (int) z;
	}

	public double distSquared(Vector3f other) {
		return Math.pow(other.x - (double) this.x, 2) + Math.pow(other.y - (double) this.y, 2) + Math.pow(other.z - (double) this.z, 2);
	}

}
