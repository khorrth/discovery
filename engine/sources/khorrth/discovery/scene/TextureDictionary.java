package khorrth.discovery.scene;

import khorrth.discovery.R;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.opengl.GLES20;

import android.graphics.BitmapFactory;
import android.graphics.Bitmap;

import android.opengl.GLUtils;

import java.util.List;
import java.util.ArrayList;

public class TextureDictionary
{

	protected int BASE_IMAGE_MIPMAP_LEVEL = 0;
	protected int DEFAULT_BORDER_COLOR = 0;

	Context context;

	List<Integer> textureHandles;

	public TextureDictionary(Context context)
	{
		this.context = context;
		textureHandles = new ArrayList<Integer>();
	}

	protected int loadTexture(final int resourceId) // Refactor it to make atlases available
	{
		final int[] textureHandle = new int[1];

		int texturesCount = 1;
		int texturesOffset = 0;

		GLES20.glGenTextures(texturesCount, textureHandle, texturesOffset);

		if (textureHandle[0] != 0)
		{
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled = false;   // No pre-scaling

			// Read in the resource
			final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, BASE_IMAGE_MIPMAP_LEVEL, bitmap, DEFAULT_BORDER_COLOR);

			bitmap.recycle();
    	}

    	if (textureHandle[0] == 0)
    	{
			throw new RuntimeException("Error loading texture.");
    	}

		return textureHandle[0];
	}

	public int getTextureHandle(int index)
	{
		return textureHandles.get(index);
	}
}
