package khorrth.discovery.scene;

import java.util.List;
import java.util.ArrayList;

public class Model
{
	public static final float DEFAULT_ROTATION_ANGLE = 0.0f;
	public static final float DEFAULT_POSITION_X = 0.0f;
	public static final float DEFAULT_POSITION_Y = 0.0f;
	public static final float DEFAULT_WIDTH = 1;
	public static final float DEFAULT_HEIGHT = 1;
	public static final int   DEFAULT_ANIMATION_FRAME_INDEX = 0;

	protected float rotationAngle = DEFAULT_ROTATION_ANGLE;
	protected float positionX;
	protected float positionY;
	protected float width;
	protected float height;
	protected TextureDictionary animationFrames;
	protected int currentAnimationFrameIndex;

	public Model(float positionX, float positionY, float width, float height, TextureDictionary animationFrames)
	{
		this.setPosition(positionX, positionY);
		this.setSize(width, height);
		this.animationFrames = animationFrames;
		this.currentAnimationFrameIndex = DEFAULT_ANIMATION_FRAME_INDEX;
	}

	public void setAnimationFrame(int index)
	{
		this.currentAnimationFrameIndex = index;
	}

	public int getCurrentAnimationFrame()
	{
	 	return this.animationFrames.getTextureHandle(currentAnimationFrameIndex);
	}

	public void setPositionX(float positionX)
	{
		this.positionX = positionX;
	}

	public void setPositionY(float positionY)
	{
		this.positionY = positionY;
	}


	public void setPosition(float positionX, float positionY)
	{
		this.positionX = positionX;
		this.positionY = positionY;
	}

	public float getPositionX()
	{
		return this.positionX;
	}

	public float getPositionY()
	{
		return this.positionY;
	}

	public void setRotationAngle(float angle)
	{
		this.rotationAngle = angle;
	}

	public float getRotationAngle()
	{
		return this.rotationAngle;
	}

	public float getWidth()
	{
		return this.width;
	}

	public float getHeight()
	{
		return this.height;
	}

	public void setWidth(float width)
	{
		this.width = width;
	}

	public void setHeight(float height)
	{
		this.height = height;
	}

	public void setSize(float width, float height)
	{
		this.width = width;
		this.height = height;
	}
}
