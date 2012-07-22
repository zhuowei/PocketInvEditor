package net.zhuoweizhang.pocketinveditor.tileentity;

import java.util.List;

import net.zhuoweizhang.pocketinveditor.InventorySlot;

public class ContainerTileEntity extends TileEntity {

	private List<InventorySlot> items;

	public List<InventorySlot> getItems() {
		return items;
	}

	public void setItems(List<InventorySlot> items) {
		this.items = items;
	}
}
