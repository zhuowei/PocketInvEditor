package net.zhuoweizhang.pocketinveditor.material;

public class MaterialCount {

	public MaterialKey key;

	public int count;

	public MaterialCount(MaterialKey key, int count) {

		this.key = key;
		this.count = count;

	}

	public String toString() {
		return "[" + key.typeId + ":" + key.damage + "]: " + count;
	}
}
