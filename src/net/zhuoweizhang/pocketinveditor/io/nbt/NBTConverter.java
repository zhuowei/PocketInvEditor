package net.zhuoweizhang.pocketinveditor.io.nbt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.spout.nbt.*;

import net.zhuoweizhang.pocketinveditor.InventorySlot;
import net.zhuoweizhang.pocketinveditor.ItemStack;
import net.zhuoweizhang.pocketinveditor.Level;

import net.zhuoweizhang.pocketinveditor.entity.Player;

import net.zhuoweizhang.pocketinveditor.util.Vector;

public final class NBTConverter {

	public static InventorySlot readInventorySlot(CompoundTag compoundTag) {
		List<Tag> tags = compoundTag.getValue();
		byte slot = 0;
		short id = 0;
		short damage = 0;
		byte count = 0;
		for (Tag tag : tags) {
			if (tag.getName().equals("Slot")) {
				slot = ((ByteTag) tag).getValue();
			} else if (tag.getName().equals("id")) {
				id = ((ShortTag) tag).getValue();
			} else if (tag.getName().equals("Damage")) {
				damage = ((ShortTag) tag).getValue();
			} else if (tag.getName().equals("Count")) {
				count = ((ByteTag) tag).getValue();
			}
		}
		return new InventorySlot(slot, new ItemStack(id, damage, count));
	}

	public static CompoundTag writeInventorySlot(InventorySlot slot) {
		List<Tag> values = new ArrayList<Tag>(4);
		ItemStack stack = slot.getContents();
		values.add(new ByteTag("Count", (byte) stack.getAmount()));
		values.add(new ShortTag("Damage", stack.getDurability()));
		values.add(new ByteTag("Slot", slot.getSlot()));
		values.add(new ShortTag("id", stack.getTypeId()));
		return new CompoundTag("", values);
	}

	public static ListTag<CompoundTag> writeInventory(List<InventorySlot> slots, String name) {
		List<CompoundTag> values = new ArrayList<CompoundTag>(slots.size());
		for (InventorySlot slot: slots) {
			values.add(writeInventorySlot(slot));
		}
		return new ListTag<CompoundTag>(name, CompoundTag.class, values);
	}

	public static List<InventorySlot> readInventory(ListTag<CompoundTag> listTag) {
		List<InventorySlot> slots = new ArrayList<InventorySlot>();
		for (CompoundTag tag: listTag.getValue()) {
			slots.add(readInventorySlot(tag));
		}
		return slots;
	}

	@SuppressWarnings("unchecked")
	public static Player readPlayer(CompoundTag compoundTag) {
		/* todo: separate this out to another class like Glowstone's loading system */
		List<Tag> tags = compoundTag.getValue();
		Player player = new Player();
		for (Tag tag: tags) {
			if (tag.getName().equals("Pos")) {
				player.setLocation(readVector((ListTag<FloatTag>) tag));
			} else if (tag.getName().equals("Motion")) {
				player.setVelocity(readVector((ListTag<FloatTag>) tag));
			} else if (tag.getName().equals("Air")) {
				player.setAirTicks(((ShortTag) tag).getValue());
			} else if (tag.getName().equals("Fire")) {
				player.setFireTicks(((ShortTag) tag).getValue());
			} else if (tag.getName().equals("FallDistance")) {
				player.setFallDistance(((FloatTag) tag).getValue());
			} else if (tag.getName().equals("Rotation")) {
				List<FloatTag> rotationTags = ((ListTag<FloatTag>) tag).getValue();
				player.setYaw(rotationTags.get(0).getValue());
				player.setPitch(rotationTags.get(1).getValue());
			} else if (tag.getName().equals("OnGround")) {
				player.setOnGround(((ByteTag) tag).getValue() > 0 ? true : false);
			} else if (tag.getName().equals("AttackTime")) {
				player.setAttackTime(((ShortTag) tag).getValue());
			} else if (tag.getName().equals("DeathTime")) {
				player.setDeathTime(((ShortTag) tag).getValue());
			} else if (tag.getName().equals("Health")) {
				player.setHealth(((ShortTag) tag).getValue());
			} else if (tag.getName().equals("HurtTime")) {
				player.setHurtTime(((ShortTag) tag).getValue());
			} else if (tag.getName().equals("Dimension")) {
				player.setDimension(((IntTag) tag).getValue());
			} else if (tag.getName().equals("Inventory")) {
				player.setInventory(readInventory((ListTag<CompoundTag>) tag));
			} else if (tag.getName().equals("Score")) {
				player.setScore(((IntTag) tag).getValue());
			}
		}
		return player;
	}

	public static CompoundTag writePlayer(Player player, String name) {
		List<Tag> tags = new ArrayList<Tag>();
		/* shared properties with all entities */
		tags.add(new ShortTag("Air", player.getAirTicks()));
		tags.add(new FloatTag("FallDistance", player.getFallDistance()));
		tags.add(new ShortTag("Fire", player.getFireTicks()));
		tags.add(writeVector(player.getVelocity(), "Motion"));
		tags.add(writeVector(player.getLocation(), "Pos"));
		List<FloatTag> rotationTags = new ArrayList<FloatTag>(2);
		rotationTags.add(new FloatTag("", player.getYaw()));
		rotationTags.add(new FloatTag("", player.getPitch()));
		tags.add(new ListTag<FloatTag>("Rotation", FloatTag.class, rotationTags));
		tags.add(new ByteTag("OnGround", player.isOnGround() ? (byte) 1: (byte) 0));

		/* mobs' tags */
		tags.add(new ShortTag("AttackTime", player.getAttackTime()));
		tags.add(new ShortTag("DeathTime", player.getDeathTime()));
		tags.add(new ShortTag("Health", player.getHealth()));
		tags.add(new ShortTag("HurtTime", player.getHurtTime()));

		/* Human specific tags */

		tags.add(new IntTag("Dimension", player.getDimension()));
		tags.add(writeInventory(player.getInventory(), "Inventory"));
		tags.add(new IntTag("Score", player.getScore()));

		/* all level.dat tags are sorted for some reason */

		Collections.sort(tags, new Comparator<Tag>() {
			public int compare(Tag a, Tag b) {
				return a.getName().compareTo(b.getName());
			}
			public boolean equals(Tag a, Tag b) {
				return a.getName().equals(b.getName());
			}
		});

		return new CompoundTag(name, tags);
	}

	public static Vector readVector(ListTag<FloatTag> tag) {
		List<FloatTag> tags = tag.getValue();
		return new Vector(tags.get(0).getValue(), tags.get(1).getValue(), tags.get(2).getValue());
	}

	public static ListTag<FloatTag> writeVector(Vector vector, String tagName) {
		List<FloatTag> tags = new ArrayList<FloatTag>(3);
		tags.add(new FloatTag("", vector.getX()));
		tags.add(new FloatTag("", vector.getY()));
		tags.add(new FloatTag("", vector.getZ()));
		return new ListTag<FloatTag>(tagName, FloatTag.class, tags);
	}

	@SuppressWarnings("unchecked")
	public static Level readLevel(CompoundTag compoundTag) {
		Level level = new Level();
		List<Tag> tags = compoundTag.getValue();
		for (Tag tag: tags) {
			String name = tag.getName();
			if (name.equals("GameType")) {
				level.setGameType(((IntTag) tag).getValue());
			} else if (name.equals("LastPlayed")) {
				level.setLastPlayed(((LongTag) tag).getValue());
			} else if (name.equals("LevelName")) {
				level.setLevelName(((StringTag) tag).getValue());
			} else if (name.equals("Platform")) {
				level.setPlatform(((IntTag) tag).getValue());
			} else if (name.equals("Player")) {
				level.setPlayer(readPlayer((CompoundTag) tag));
			} else if (name.equals("RandomSeed")) {
				level.setRandomSeed(((LongTag) tag).getValue());
			} else if (name.equals("SizeOnDisk")) {
				level.setSizeOnDisk(((LongTag) tag).getValue());
			} else if (name.equals("SpawnX")) {
				level.setSpawnX(((IntTag) tag).getValue());
			} else if (name.equals("SpawnY")) {
				level.setSpawnY(((IntTag) tag).getValue());
			} else if (name.equals("SpawnZ")) {
				level.setSpawnZ(((IntTag) tag).getValue());
			} else if (name.equals("StorageVersion")) {
				level.setStorageVersion(((IntTag) tag).getValue());
			} else if (name.equals("Time")) {
				level.setTime(((LongTag) tag).getValue());
			}
		}
		return level;
	}

	public static CompoundTag writeLevel(Level level) {
		List<Tag> tags = new ArrayList<Tag>(11);
		/* tags should be sorted alphabetically */
		tags.add(new IntTag("GameType", level.getGameType()));
		tags.add(new LongTag("LastPlayed", level.getLastPlayed()));
		tags.add(new StringTag("LevelName", level.getLevelName()));
		tags.add(new IntTag("Platform", level.getPlatform()));
		tags.add(writePlayer(level.getPlayer(), "Player"));
		tags.add(new LongTag("RandomSeed", level.getRandomSeed()));
		tags.add(new LongTag("SizeOnDisk", level.getSizeOnDisk()));
		tags.add(new IntTag("SpawnX", level.getSpawnX()));
		tags.add(new IntTag("SpawnY", level.getSpawnY()));
		tags.add(new IntTag("SpawnZ", level.getSpawnZ()));
		tags.add(new IntTag("StorageVersion", level.getStorageVersion()));
		tags.add(new LongTag("Time", level.getTime()));
		return new CompoundTag("", tags);
	}

}
