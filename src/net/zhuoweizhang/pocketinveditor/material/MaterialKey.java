package net.zhuoweizhang.pocketinveditor.material;

public final class MaterialKey {

	public final short typeId;

	public final short damage;

	/**
	 * @param damage the damage/data value of the material. -1 is a pseudo-value that tells any replacement routines to match all data values.
	 */
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

	public static MaterialKey parse(String str, int radix) {
		String[] parts = str.split(":");
		if (parts.length == 0) throw new IllegalArgumentException("Why is the string blank?!");
		else if (parts.length == 1) {
			return new MaterialKey(Short.parseShort(parts[0], radix), (short) -1);
		} else {
			return new MaterialKey(Short.parseShort(parts[0], radix), Short.parseShort(parts[1], radix));
		}
	}
}
