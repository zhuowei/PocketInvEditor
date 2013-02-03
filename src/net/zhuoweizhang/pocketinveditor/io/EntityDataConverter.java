package net.zhuoweizhang.pocketinveditor.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.DataOutputStream;

import java.util.List;

import org.spout.nbt.*;
import org.spout.nbt.stream.*;

import net.zhuoweizhang.pocketinveditor.entity.Entity;
import net.zhuoweizhang.pocketinveditor.io.nbt.NBTConverter;
import net.zhuoweizhang.pocketinveditor.tileentity.TileEntity;

public final class EntityDataConverter {
	public static final byte[] header = {0x45, 0x4e, 0x54, 0x00, 0x01, 0x00, 0x00, 0x00};

	public static class EntityData {
		public List<Entity> entities;
		public List<TileEntity> tileEntities;
		public EntityData(List<Entity> entities, List<TileEntity> tileEntities) {
			this.entities = entities;
			this.tileEntities = tileEntities;
		}
	}

	public static EntityData read(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream is = new BufferedInputStream(fis);
		is.skip(12);
		EntityData eDat = NBTConverter.readEntities((CompoundTag) new NBTInputStream(is, false, true).readTag());
		is.close();
		return eDat;
	}

	public static void write(List<Entity> entitiesList, List<TileEntity> tileEntitiesList, File file) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		new NBTOutputStream(bos, false, true).writeTag(NBTConverter.writeEntities(entitiesList, tileEntitiesList));

		FileOutputStream os = new FileOutputStream(file);
		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(os));

		int length = bos.size();
		dos.write(header);
		dos.writeInt(Integer.reverseBytes(length));
		bos.writeTo(dos);
		dos.close();
	}

	public static void main(String[] args) throws Exception {
		EntityData entities = read(new File(args[0]));
		System.out.println(entities);
		write(entities.entities, entities.tileEntities, new File(args[1]));
	}

}
