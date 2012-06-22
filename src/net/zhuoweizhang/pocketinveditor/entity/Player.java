package net.zhuoweizhang.pocketinveditor.entity;

import java.util.List;

import net.zhuoweizhang.pocketinveditor.InventorySlot;

public class Player extends LivingEntity {
	private List<InventorySlot> inventory;
	private int score;
	private int dimension;
	public List<InventorySlot> getInventory() {
		return inventory;
	}
	public void setInventory(List<InventorySlot> inventory) {
		this.inventory = inventory;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getDimension() {
		return dimension;
	}

	public void setDimension(int dimension) {
		this.dimension = dimension;
	}
}
