package net.zhuoweizhang.pocketinveditor;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import net.zhuoweizhang.pocketinveditor.material.MaterialKey;
import net.zhuoweizhang.pocketinveditor.material.icon.MaterialIcon;

/** An ArrayAdapter that displays item icons based on the InventorySlot. */
public class MaterialIconArrayAdapter<T extends InventorySlot> extends ArrayAdapter<T> {

	public MaterialIconArrayAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
		super(context, resource, textViewResourceId, objects);
	}


	public View getView(int position, View convertView, ViewGroup parent) {
		View retval = super.getView(position, convertView, parent);
		ImageView iconView = (ImageView) retval.findViewById(R.id.slot_list_icon);
		InventorySlot slot = this.getItem(position);
		ItemStack stack = slot.getContents();
		MaterialIcon icon = MaterialIcon.icons.get(new MaterialKey(stack.getTypeId(), stack.getDurability()));
		if (icon == null) {
			icon = MaterialIcon.icons.get(new MaterialKey(stack.getTypeId(), (short) 0));
		}
		if (icon != null) {
			BitmapDrawable myDrawable = new BitmapDrawable(icon.bitmap);
			myDrawable.setDither(false);
			myDrawable.setAntiAlias(false);
			myDrawable.setFilterBitmap(false);
			iconView.setImageDrawable(myDrawable);
			iconView.setVisibility(View.VISIBLE);
		} else {
			iconView.setVisibility(View.INVISIBLE);
		}
		return retval;
	}
}
