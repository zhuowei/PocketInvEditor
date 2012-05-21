package net.zhuoweizhang.pocketinveditor.material.icon;

import java.util.Map;

import android.graphics.Bitmap;

public class MaterialIcon {

	public static Map<MaterialKey, MaterialIcon> icons;

	public int typeId;

	public short damage;

	public Bitmap bitmap;

	public MaterialIcon(int typeId, short damage, Bitmap bmp) {
		this.typeId = typeId;
		this.damage = damage;
		this.bitmap = bmp;
	}
}
