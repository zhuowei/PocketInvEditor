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

public final class EntityDataConverter {
	public static final byte[] header = {0x45, 0x4e, 0x54, 0x00, 0x01, 0x00, 0x00, 0x00};

	public static List<Entity> read(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream is = new BufferedInputStream(fis);
		is.skip(12);
		List<Entity> entitiesList = NBTConverter.readEntities((CompoundTag) new NBTInputStream(is, false, true).readTag());
		is.close();
		return entitiesList;
	}

	public static void write(List<Entity> entitiesList, File file) throws IOException {
		FileOutputStream os = new FileOutputStream(file);
		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(os));
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		new NBTOutputStream(bos, false, true).writeTag(NBTConverter.writeEntities(entitiesList));
		int length = bos.size();
		dos.write(header);
		dos.writeInt(Integer.reverseBytes(length));
		bos.writeTo(dos);
		dos.close();
	}

	public static void main(String[] args) throws Exception {
		List<Entity> entities = read(new File(args[0]));
		System.out.println(entities);
		write(entities, new File(args[1]));
	}

}
