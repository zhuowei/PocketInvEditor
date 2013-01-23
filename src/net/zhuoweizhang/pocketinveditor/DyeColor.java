package net.zhuoweizhang.pocketinveditor;

public enum DyeColor {

	WHITE(221, 221, 221),
	ORANGE(219, 125, 62),
	MAGENTA(179, 80, 188),
	LIGHT_BLUE(107, 138, 201),
	YELLOW(177, 166, 39),
	LIME(65, 174, 56),
	PINK(208, 132, 153),
	GRAY(64, 64, 64),
	LIGHT_GREY(154, 161, 161),
	CYAN(46, 110, 137),
	PURPLE(126, 61, 181),
	BLUE(46, 56, 141),
	BROWN(79, 50, 31),
	GREEN(53, 70, 27),
	RED(150, 52, 48),
	BLACK(25, 22, 22);

	public final int color;

	DyeColor(int color) {
		this.color = color;
	}

	DyeColor(int red, int green, int blue) {
		this((red << 24) | (green << 16) | blue);
	}

	public byte getWoolData() {
		return (byte) this.ordinal();
	}

	public byte getDyeData() {
		return (byte) (15 - this.ordinal());
	}

}
