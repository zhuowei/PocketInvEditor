package net.zhuoweizhang.pocketinveditor;

import net.zhuoweizhang.pocketinveditor.io.EntityDataConverter;

public interface EntityDataLoadListener {
	/** Called on the UI thread after the entities.dat file has been loaded. */
	public void onEntitiesLoaded(EntityDataConverter.EntityData entitiesDat);
}
