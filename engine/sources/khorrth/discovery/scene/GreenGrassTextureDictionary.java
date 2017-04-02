package khorrth.discovery.scene;

import android.content.Context;

import khorrth.discovery.R;

public class GreenGrassTextureDictionary extends TextureDictionary
{
	public GreenGrassTextureDictionary(Context context)
	{
		super(context);
		textureHandles.add(loadTexture(R.drawable.green_grass));
	}
}
