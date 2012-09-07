package net.zhuoweizhang.pocketinveditor.io.nbt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.spout.nbt.*;

import net.zhuoweizhang.pocketinveditor.InventorySlot;
import net.zhuoweizhang.pocketinveditor.ItemStack;
import net.zhuoweizhang.pocketinveditor.Level;

import net.zhuoweizhang.pocketinveditor.entity.*;

import static net.zhuoweizhang.pocketinveditor.io.EntityDataConverter.EntityData;

import net.zhuoweizhang.pocketinveditor.io.nbt.entity.EntityStore;
import net.zhuoweizhang.pocketinveditor.io.nbt.entity.EntityStoreLookupService;

import net.zhuoweizhang.pocketinveditor.io.nbt.tileentity.TileEntityStore;
import net.zhuoweizhang.pocketinveditor.io.nbt.tileentity.TileEntityStoreLookupService;

import net.zhuoweizhang.pocketinveditor.tileentity.*;

import net.zhuoweizhang.pocketinveditor.util.Vector;

public final class NBTConverter {

	public static InventorySlot readInventorySlot(CompoundTag compoundTag) {
		List<Tag> tags = compoundTag.getValue();
		byte slot = 0;
		short id = 0;
		short damage = 0;
		int count = 0;
		for (Tag tag : tags) {
			if (tag.getName().equals("Slot")) {
				slot = ((ByteTag) tag).getValue();
			} else if (tag.getName().equals("id")) {
				id = ((ShortTag) tag).getValue();
			} else if (tag.getName().equals("Damage")) {
				damage = ((ShortTag) tag).getValue();
			} else if (tag.getName().equals("Count")) {
				count = ((ByteTag) tag).getValue();
				if (count < 0) {
					count = 256 + count;
				}
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
			String name = tag.getName();
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
			} else if (name.equals("BedPositionX")) {
				player.setBedPositionX(((IntTag) tag).getValue());
			} else if (name.equals("BedPositionY")) {
				player.setBedPositionY(((IntTag) tag).getValue());
			} else if (name.equals("BedPositionZ")) {
				player.setBedPositionZ(((IntTag) tag).getValue());
			} else if (tag.getName().equals("Dimension")) {
				player.setDimension(((IntTag) tag).getValue());
			} else if (tag.getName().equals("Inventory")) {
				player.setInventory(readInventory((ListTag<CompoundTag>) tag));
			} else if (tag.getName().equals("Score")) {
				player.setScore(((IntTag) tag).getValue());
			} else if (tag.getName().equals("Sleeping")) {
				player.setSleeping(((ByteTag) tag).getValue() != 0);
			} else if (name.equals("SleepTimer")) {
				player.setSleepTimer(((ShortTag) tag).getValue());
			} else if (name.equals("SpawnX")) {
				player.setSpawnX(((IntTag) tag).getValue());
			} else if (name.equals("SpawnY")) {
				player.setSpawnY(((IntTag) tag).getValue());
			} else if (name.equals("SpawnZ")) {
				player.setSpawnZ(((IntTag) tag).getValue());
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

		tags.add(new IntTag("BedPositionX", player.getBedPositionX()));
		tags.add(new IntTag("BedPositionY", player.getBedPositionY()));
		tags.add(new IntTag("BedPositionZ", player.getBedPositionZ()));
		tags.add(new IntTag("Dimension", player.getDimension()));
		tags.add(writeInventory(player.getInventory(), "Inventory"));
		tags.add(new IntTag("Score", player.getScore()));
		tags.add(new ByteTag("Sleeping", player.isSleeping() ? (byte) 1: (byte) 0));
		tags.add(new ShortTag("SleepTimer", player.getSleepTimer()));
		tags.add(new IntTag("SpawnX", player.getSpawnX()));
		tags.add(new IntTag("SpawnY", player.getSpawnY()));
		tags.add(new IntTag("SpawnZ", player.getSpawnZ()));

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

	@SuppressWarnings("unchecked")
	public static EntityData readEntities(CompoundTag tag) {
		List<Entity> entities = null;
		List<TileEntity> tileEntities = null;
		for (Tag t: tag.getValue()) {
			if (t.getName().equals("Entities")) {
				entities = readEntitiesPart(((ListTag<CompoundTag>) t).getValue());
			} else if (t.getName().equals("TileEntities")) {
				tileEntities = readTileEntitiesPart(((ListTag<CompoundTag>) t).getValue());
			}
		}
		if (tileEntities == null) {
			tileEntities = new ArrayList<TileEntity>();
		}
		return new EntityData(entities, tileEntities);
	}

	public static List<Entity> readEntitiesPart(List<CompoundTag> tags) {
		List<Entity> entities = new ArrayList<Entity>(tags.size());
		for (CompoundTag entityTag: tags) {
			for (Tag t: entityTag.getValue()) {
				String name = t.getName();
				if (name.equals("id")) {
					Entity entity = readEntity(((IntTag) t).getValue(), entityTag);
					entities.add(entity);
					break;
				}
			}
		}
		return entities;
	}

	public static Entity readEntity(int id, CompoundTag tag) {
		Entity entity = createEntityById(id);
		entity.setEntityTypeId(id);
		EntityStore store = EntityStoreLookupService.idMap.get(id);
		if (store == null) {
			System.err.println("Warning: unknown entity id " + id + "; using default entity store.");
			store = EntityStoreLookupService.defaultStore;
		}
		store.load(entity, tag);
		return entity;
	}

	public static Entity createEntityById(int id) {
		switch (id) {
			case 10:
				return new Chicken();
			case 11:
				return new Cow();
			case 12:
				return new Pig();
			case 13:
				return new Sheep();
			case 32:
				return new Zombie();
			case 64:
				return new Item();
			default:
				EntityType type = EntityType.getById(id);
				if (type != null) {
					try {
						return type.getEntityClass().newInstance();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.err.println("Unable to find entity class for entity type " + id);
				return new Entity();
		}
	}

	public static CompoundTag writeEntities(List<Entity> entities, List<TileEntity> tileEntities) {
		List<CompoundTag> entitiesTags = new ArrayList<CompoundTag>(entities.size());
		for (Entity entity : entities) {
			entitiesTags.add(writeEntity(entity));
		}
		ListTag<CompoundTag> entitiesListTag = new ListTag<CompoundTag>("Entities", CompoundTag.class, entitiesTags);

		List<CompoundTag> tileEntitiesTags = new ArrayList<CompoundTag>(tileEntities.size());
		for (TileEntity entity : tileEntities) {
			tileEntitiesTags.add(writeTileEntity(entity));
		}
		ListTag<CompoundTag> tileEntitiesListTag = new ListTag<CompoundTag>("TileEntities", CompoundTag.class, tileEntitiesTags);

		List<Tag> compoundTagList = new ArrayList<Tag>(2);
		compoundTagList.add(entitiesListTag);
		compoundTagList.add(tileEntitiesListTag);
		CompoundTag compoundTag = new CompoundTag("", compoundTagList);
		return compoundTag;
	}

	public static CompoundTag writeEntity(Entity entity) {
		int typeId = entity.getEntityTypeId();
		EntityStore store = EntityStoreLookupService.idMap.get(typeId);
		if (store == null) {
			System.err.println("Warning: unknown entity id " + typeId + "; using default entity store.");
			store = EntityStoreLookupService.defaultStore;
		}
		List<Tag> tags = store.save(entity);
		
		Collections.sort(tags, new Comparator<Tag>() {
			public int compare(Tag a, Tag b) {
				return a.getName().compareTo(b.getName());
			}
			public boolean equals(Tag a, Tag b) {
				return a.getName().equals(b.getName());
			}
		});
		return new CompoundTag("", tags);
	}

	public static ItemStack readItemStack(CompoundTag compoundTag) {
		List<Tag> tags = compoundTag.getValue();
		short id = 0;
		short damage = 0;
		int count = 0;
		for (Tag tag : tags) {
			if (tag.getName().equals("id")) {
				id = ((ShortTag) tag).getValue();
			} else if (tag.getName().equals("Damage")) {
				damage = ((ShortTag) tag).getValue();
			} else if (tag.getName().equals("Count")) {
				count = ((ByteTag) tag).getValue();
				if (count < 0) {
					count = 256 + count;
				}
			}
		}
		return new ItemStack(id, damage, count);
	}

	public static CompoundTag writeItemStack(ItemStack stack, String name) {
		List<Tag> values = new ArrayList<Tag>(3);
		values.add(new ByteTag("Count", (byte) stack.getAmount()));
		values.add(new ShortTag("Damage", stack.getDurability()));
		values.add(new ShortTag("id", stack.getTypeId()));
		return new CompoundTag(name, values);
	}

	public static List<TileEntity> readTileEntitiesPart(List<CompoundTag> tags) {
		List<TileEntity> entities = new ArrayList<TileEntity>(tags.size());
		for (CompoundTag entityTag: tags) {
			for (Tag t: entityTag.getValue()) {
				String name = t.getName();
				if (name.equals("id")) {
					TileEntity entity = readTileEntity(((StringTag) t).getValue(), entityTag);
					entities.add(entity);
					break;
				}
			}
		}
		return entities;
	}

	public static TileEntity readTileEntity(String id, CompoundTag tag) {
		TileEntity entity = createTileEntityById(id);
		entity.setId(id);
		TileEntityStore store = TileEntityStoreLookupService.idMap.get(id);
		if (store == null) {
			System.err.println("Warning: unknown tile entity id " + id + "; using default tileentity store.");
			store = TileEntityStoreLookupService.defaultStore;
		}
		store.load(entity, tag);
		return entity;
	}

	public static TileEntity createTileEntityById(String id) {
		if (id.equals("Furnace")) {
			return new FurnaceTileEntity();
		} else if (id.equals("Chest")) {
			return new ContainerTileEntity();
		} else {
			return new TileEntity();
		}
	}

	public static CompoundTag writeTileEntity(TileEntity entity) {
		String typeId = entity.getId();
		TileEntityStore store = TileEntityStoreLookupService.idMap.get(typeId);
		if (store == null) {
			System.err.println("Warning: unknown tileentity id " + typeId + "; using default tileentity store.");
			store = TileEntityStoreLookupService.defaultStore;
		}
		List<Tag> tags = store.save(entity);
		
		Collections.sort(tags, new Comparator<Tag>() {
			public int compare(Tag a, Tag b) {
				return a.getName().compareTo(b.getName());
			}
			public boolean equals(Tag a, Tag b) {
				return a.getName().equals(b.getName());
			}
		});
		return new CompoundTag("", tags);
	}

}
