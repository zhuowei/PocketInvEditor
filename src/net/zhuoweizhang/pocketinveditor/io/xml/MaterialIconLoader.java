package net.zhuoweizhang.pocketinveditor.io.xml;

import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import net.zhuoweizhang.pocketinveditor.R;

import net.zhuoweizhang.pocketinveditor.material.MaterialKey;
import net.zhuoweizhang.pocketinveditor.material.icon.MaterialIcon;

public final class MaterialIconLoader implements Runnable {

	public XmlResourceParser parser;

	public Bitmap guiBlocksBitmap;

	public Bitmap guiBlocks2Bitmap;

	public Bitmap guiBlocks3Bitmap;

	public Bitmap guiBlocks4Bitmap;

	public Bitmap terrainBitmap;

	public Bitmap itemsBitmap;

	public MaterialIconLoader(Context context) {
		AssetManager asMgr = context.getAssets();
		this.parser = context.getResources().getXml(R.xml.item_icon);
		try {
			BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
			bmpOptions.inDither = false;
			bmpOptions.inScaled = false;
			this.guiBlocksBitmap = BitmapFactory.decodeStream(asMgr.open("gui/gui_blocks.png"));
			this.guiBlocks2Bitmap = BitmapFactory.decodeStream(asMgr.open("gui/gui_blocks_2.png"));
			this.guiBlocks3Bitmap = BitmapFactory.decodeStream(asMgr.open("gui/gui_blocks_3.png"));
			this.guiBlocks4Bitmap = BitmapFactory.decodeStream(asMgr.open("gui/gui_blocks_4.png"));
			this.terrainBitmap = BitmapFactory.decodeStream(asMgr.open("terrain_3x.png"));
			this.itemsBitmap = BitmapFactory.decodeStream(asMgr.open("gui/items_3x.png"), null, bmpOptions);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			loadMaterials(parser, guiBlocksBitmap, guiBlocks2Bitmap, guiBlocks3Bitmap, guiBlocks4Bitmap, terrainBitmap, itemsBitmap);
		} finally {
			parser.close();
		}
	}

	public static void loadMaterials(XmlPullParser parser, Bitmap guiBlocksBitmap, Bitmap guiBlocks2Bitmap, Bitmap guiBlocks3Bitmap, Bitmap guiBlocks4Bitmap,
			Bitmap terrainBitmap, Bitmap itemsBitmap) {
		Map<MaterialKey, MaterialIcon> retval = new HashMap<MaterialKey, MaterialIcon>();
		try {
			while (parser.next() != XmlPullParser.END_DOCUMENT) {
				String tagName = parser.getName();
				short itemId = 0;
				short itemDamage = 0;
				boolean itemHasSubtypes = false;
				String itemIconString = null;
				if (tagName != null && tagName.equals("item")) {
					int size = parser.getAttributeCount();
					for (int i = 0; i < size; i++) { 
						String attrName = parser.getAttributeName(i);
						String attrValue = parser.getAttributeValue(i);
						if (attrName == null) continue;
						if (attrName.equals("typeId")) {
							itemId = Short.parseShort(attrValue);
						} else if (attrName.equals("icon")) {
							itemIconString = attrValue;
						} else if (attrName.equals("damage")) {
							itemDamage = Short.parseShort(attrValue);
						}
					}
					if (itemIconString != null) {
						MaterialKey key = new MaterialKey(itemId, itemDamage);
						String[] iconParams = itemIconString.split(",");
						String iconSource = iconParams[0];
						Bitmap sourceBitmap = null;
						int cellWidthX = 48;
						int cellWidthY = 48;
						if (iconSource.equals("guiblocks")) {
							sourceBitmap = guiBlocksBitmap;
						} else if (iconSource.equals("guiblocks2")) {
							sourceBitmap = guiBlocks2Bitmap;
						} else if (iconSource.equals("guiblocks3")) {
							sourceBitmap = guiBlocks3Bitmap;
						} else if (iconSource.equals("guiblocks4")) {
							sourceBitmap = guiBlocks4Bitmap;
						} else if (iconSource.equals("terrain")) {
							sourceBitmap = terrainBitmap;
						} else if (iconSource.equals("items")) {
							sourceBitmap = itemsBitmap;
						} else {
							System.err.println("iconSource - invalid icon source: " + iconParams);
							continue;
						}
						int sourceRow = Integer.parseInt(iconParams[1]);
						int sourceColumn = Integer.parseInt(iconParams[2]);
						Bitmap itemIconBitmap = Bitmap.createBitmap(sourceBitmap, sourceColumn * cellWidthX, 
							sourceRow * cellWidthY, cellWidthX, cellWidthY, null, false);
						MaterialIcon materialIcon = new MaterialIcon(itemId, itemDamage, itemIconBitmap);
						retval.put(key, materialIcon);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		MaterialIcon.icons = retval;

	}
}
