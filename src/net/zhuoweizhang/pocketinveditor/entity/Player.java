package net.zhuoweizhang.pocketinveditor.entity;

import java.util.List;

import net.zhuoweizhang.pocketinveditor.InventorySlot;
import net.zhuoweizhang.pocketinveditor.ItemStack;

public class Player extends LivingEntity {
	private List<InventorySlot> inventory;
	private int score;
	private int dimension;
	private int bedPositionX = 0, bedPositionY = 0, bedPositionZ = 0;
	private int spawnX = 0, spawnY = 64, spawnZ = 0;
	private short sleepTimer = 0;
	private boolean sleeping = false;
	private List<ItemStack> armorSlots;
	private PlayerAbilities abilities = new PlayerAbilities();

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

	public int getBedPositionX() {
		return bedPositionX;
	}

	public void setBedPositionX(int bedPositionX) {
		this.bedPositionX = bedPositionX;
	}

	public int getBedPositionY() {
		return bedPositionY;
	}

	public void setBedPositionY(int bedPositionY) {
		this.bedPositionY = bedPositionY;
	}

	public int getBedPositionZ() {
		return bedPositionZ;
	}

	public void setBedPositionZ(int bedPositionZ) {
		this.bedPositionZ = bedPositionZ;
	}

	public int getSpawnX() {
		return spawnX;
	}

	public void setSpawnX(int spawnX) {
		this.spawnX = spawnX;
	}

	public int getSpawnY() {
		return spawnY;
	}

	public void setSpawnY(int spawnY) {
		this.spawnY = spawnY;
	}

	public int getSpawnZ() {
		return spawnZ;
	}

	public void setSpawnZ(int spawnZ) {
		this.spawnZ = spawnZ;
	}

	public boolean isSleeping() {
		return sleeping;
	}

	public void setSleeping(boolean sleeping) {
		this.sleeping = sleeping;
	}

	public short getSleepTimer() {
		return sleepTimer;
	}

	public void setSleepTimer(short sleepTimer) {
		this.sleepTimer = sleepTimer;
	}

	public List<ItemStack> getArmor() {
		return armorSlots;
	}

	public void setArmor(List<ItemStack> armorSlots) {
		this.armorSlots = armorSlots;
	}

	public PlayerAbilities getAbilities() {
		return this.abilities;
	}

	public void setAbilities(PlayerAbilities abilities) {
		this.abilities = abilities;
	}

	public EntityType getEntityType() {
		return EntityType.PLAYER;
	}

}
