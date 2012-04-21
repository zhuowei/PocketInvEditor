package net.zhuoweizhang.pocketinveditor.material;

import java.util.List;

public class Material {

	public static List<Material> materials;

	private int id;

	private String name;

	private short damage;

	private boolean hasSubtypes;

	public Material(int id, String name) {
		this(id, name, (short) 0, false);
	}

	public Material(int id, String name, short damage) {
		this(id, name, damage, true);
	}

	public Material(int id, String name, short damage, boolean hasSubtypes) {
		this.id = id;
		this.name = name;
		this.damage = damage;
		this.hasSubtypes = hasSubtypes;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public short getDamage() {
		return damage;
	}

	public boolean hasSubtypes() {
		return hasSubtypes;
	}

	public String toString() {
		return getName() + " : " + getId();
	}
}
