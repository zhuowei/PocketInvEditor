package net.zhuoweizhang.pocketinveditor.io.xml;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.content.res.XmlResourceParser;

import net.zhuoweizhang.pocketinveditor.material.Material;

public final class MaterialLoader implements Runnable {

	public XmlResourceParser parser;

	public MaterialLoader(XmlResourceParser parser) {
		this.parser = parser;
	}

	public void run() {
		try {
			List<Material> materials = loadMaterials(parser);
			Material.materials = materials;
		} finally {
			parser.close();
		}
	}

	public static List<Material> loadMaterials(XmlPullParser parser) {
		List<Material> materials = new ArrayList<Material>();
		try {
			while (parser.next() != XmlPullParser.END_DOCUMENT) {
				String tagName = parser.getName();
				int itemId = 0;
				String itemName = null;
				short itemDamage = 0;
				boolean itemHasSubtypes = false;
				if (tagName != null && tagName.equals("item")) {
					int size = parser.getAttributeCount();
					for (int i = 0; i < size; i++) { 
						String attrName = parser.getAttributeName(i);
						String attrValue = parser.getAttributeValue(i);
						if (attrName == null) continue;
						if (attrName.equals("dec")) {
							itemId = Integer.parseInt(attrValue);
						} else if (attrName.equals("name")) {
							itemName = attrValue;
						} else if (attrName.equals("id")) {
							itemHasSubtypes = true;
							itemDamage = Short.parseShort(attrValue);
						}
					}
					if (itemName != null) {
						materials.add(new Material(itemId, itemName, itemDamage, itemHasSubtypes));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(materials.toString());
		return materials;
	}
}
