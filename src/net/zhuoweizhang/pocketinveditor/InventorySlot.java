package net.zhuoweizhang.pocketinveditor;

public class InventorySlot {

	private byte slot;

	private ItemStack contents;

	public InventorySlot(byte slot, ItemStack contents) {
		this.slot = slot;
		this.contents = contents;
	}

	public byte getSlot() {
		return this.slot;
	}

	public void setSlot(byte slot) {
		this.slot = slot;
	}

	public ItemStack getContents() {
		return this.contents;
	}

	public void setContents(ItemStack contents) {
		this.contents = contents;
	}

	public String toString() {
		return "Slot " + getSlot() + ": Type: " + contents.getTypeId() +
			"; Damage: " + contents.getDurability() + "; Amount: "  + contents.getAmount();
	}
}
