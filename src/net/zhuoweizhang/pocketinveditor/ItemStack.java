package net.zhuoweizhang.pocketinveditor;

import java.io.Serializable;

public class ItemStack {

	private short id;

	private short durability;

	private int amount;

	public ItemStack(short id, short durability, int amount) {
		this.id = id;
		this.durability = durability;
		this.amount = amount;
	}

	public short getTypeId() {
		return id;
	}

	public void setTypeId(short id) {
		this.id = id;
	}

	public short getDurability() {
		return durability;
	}

	public void setDurability(short durability) {
		this.durability = durability;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String toString() {
		return "ItemStack: type=" + getTypeId() + ", durability=" + getDurability() + ", amount=" + getAmount();
	}

}
