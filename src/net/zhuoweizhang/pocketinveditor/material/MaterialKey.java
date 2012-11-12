package net.zhuoweizhang.pocketinveditor.material;

public final class MaterialKey {

	public final short typeId;

	public final short damage;

	public MaterialKey(short typeId, short damage) {
		this.typeId = typeId;
		this.damage = damage;
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) return true;
		if (!(other instanceof MaterialKey)) return false;
		MaterialKey another = (MaterialKey) other;
		return another.typeId == this.typeId && another.damage == this.damage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + typeId;
		result = prime * result + damage;
		return result;
	}

	@Override
	public String toString() {
		return "MaterialKey[typeId=" + typeId + ";damage=" + damage + "]";
	}
}
