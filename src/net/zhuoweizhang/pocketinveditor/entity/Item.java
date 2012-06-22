package net.zhuoweizhang.pocketinveditor.entity;

import net.zhuoweizhang.pocketinveditor.ItemStack;

public class Item extends Entity {

	private ItemStack stack;

	private short health = 5;

	private short age = 0;

	public ItemStack getItemStack() {
		return stack;
	}

	public void setItemStack(ItemStack stack) {
		this.stack = stack;
	}

	public short getAge() {
		return age;
	}

	public void setAge(short age) {
		this.age = age;
	}

	public short getHealth() {
		return health;
	}

	public void setHealth(short health) {
		this.health = health;
	}

}
