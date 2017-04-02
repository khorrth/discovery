package khorrth.discovery.scene;

import android.content.Context;

import khorrth.discovery.R;

public class BrownGrassTextureDictionary extends TextureDictionary
{
	public BrownGrassTextureDictionary(Context context)
	{
		super(context);
		textureHandles.add(loadTexture(R.drawable.brown_grass));
	}
}
