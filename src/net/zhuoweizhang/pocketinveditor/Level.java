package net.zhuoweizhang.pocketinveditor;

import java.util.List;

import net.zhuoweizhang.pocketinveditor.entity.Entity;
import net.zhuoweizhang.pocketinveditor.entity.Player;
import net.zhuoweizhang.pocketinveditor.tileentity.TileEntity;

public class Level {

	private int gameType;

	private long lastPlayed;

	private String levelName;

	private int platform;

	private Player player;

	private long randomSeed;

	private long sizeOnDisk;

	private int spawnX, spawnY, spawnZ;

	private int storageVersion;

	private long time;

	private List<Entity> entities;

	private List<TileEntity> tileEntities;

	public int getGameType() {
		return gameType;
	}

	public void setGameType(int gameType) {
		this.gameType = gameType;
	}

	public long getLastPlayed() {
		return lastPlayed;
	}

	public void setLastPlayed(long lastPlayed) {
		this.lastPlayed = lastPlayed;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public long getRandomSeed() {
		return randomSeed;
	}

	public void setRandomSeed(long randomSeed) {
		this.randomSeed = randomSeed;
	}

	public long getSizeOnDisk() {
		return sizeOnDisk;
	}

	public void setSizeOnDisk(long sizeOnDisk) {
		this.sizeOnDisk = sizeOnDisk;
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

	public int getStorageVersion() {
		return storageVersion;
	}

	public void setStorageVersion(int storageVersion) {
		this.storageVersion = storageVersion;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}

	public List<TileEntity> getTileEntities() {
		return tileEntities;
	}

	public void setTileEntities(List<TileEntity> tileEntities) {
		this.tileEntities = tileEntities;
	}
}
