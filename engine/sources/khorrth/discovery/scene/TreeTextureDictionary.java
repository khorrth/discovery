package khorrth.discovery.scene;

import android.content.Context;

import khorrth.discovery.R;

public class TreeTextureDictionary extends TextureDictionary
{
	public TreeTextureDictionary(Context context)
	{
		super(context);
		textureHandles.add(loadTexture(R.drawable.broken_tree));
		textureHandles.add(loadTexture(R.drawable.tree_stump));
	}
}
