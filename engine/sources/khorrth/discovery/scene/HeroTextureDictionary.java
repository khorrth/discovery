package khorrth.discovery.scene;

import android.content.Context;

import khorrth.discovery.R;

public class HeroTextureDictionary extends TextureDictionary
{
	public HeroTextureDictionary(Context context)
	{
		super(context);
		textureHandles.add(loadTexture(R.drawable.carl_r));
		// textureHandles.add(loadTexture(R.drawable.carl_r));
		// textureHandles.add(loadTexture(R.drawable.carl_r));
	}
}
