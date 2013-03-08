package net.zhuoweizhang.pocketinveditor.material;

import java.util.*;

import net.zhuoweizhang.pocketinveditor.ItemStack;

/** A list of all materials in Minecraft PE that uses their Damage values to calculate... damage. */
public final class RepairableMaterials {

	public static final Set<Integer> ids = new HashSet<Integer>();

	private static void add(int id) {
		ids.add(id);
	}

	private static void add(int begin, int end) {
		for (int i = begin; i <= end; i++) {
			ids.add(i);
		}
	}

	/** returns whether this stack can be repaired by resetting its damage value; */
	public static boolean isRepairable(ItemStack stack) {
		return ids.contains(new Integer(stack.getTypeId()));
	}

	static {
		add(256, 259);
		add(261);
		add(267, 279);
		add(283, 286);
		add(298, 317);
		add(359);
	}

}
