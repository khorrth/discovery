package khorrth.discovery.scene;

import android.opengl.Matrix;
import android.opengl.GLES20;

// TODO: Tune "DEFAULT_MINIMAL_DEPTH" and "DEFAULT_MAXIMAL_DEPTH" values.

public class OrthographicCamera
{
	public static final int NO_VIEWPORT_X_OFFSET = 0;
	public static final int NO_VIEWPORT_Y_OFFSET = 0;

	public static final int DEFAULT_VIEWPORT_X_OFFSET = NO_VIEWPORT_X_OFFSET;
	public static final int DEFAULT_VIEWPORT_Y_OFFSET = NO_VIEWPORT_Y_OFFSET;

	public static final float DEFAULT_MINIMAL_DEPTH = 0;
	public static final float DEFAULT_MAXIMAL_DEPTH = 50;

	public static final int DEFAULT_VIEWPORT_WIDTH = 1;
	public static final int DEFAULT_VIEWPORT_HEIGHT = 1;

	public static final float DEFAULT_POSITION_X = 0.0f;
	public static final float DEFAULT_POSITION_Y = 0.0f;
	public static final float DEFAULT_POSITION_Z = 5.0f;

	public static final float DEFAULT_DIRECTION_X = 0.0f;
	public static final float DEFAULT_DIRECTION_Y = 0.0f;
	public static final float DEFAULT_DIRECTION_Z = -5.0f;

	public static final float DEFAULT_AXIS_DIRECTION_X = 0.0f;
	public static final float DEFAULT_AXIS_DIRECTION_Y = 1.0f;
	public static final float DEFAULT_AXIS_DIRECTION_Z = 0.0f;

	private static final int NO_MATRIX_OFFSET = 0;
	private static final int GL_MATRIX_SIZE = 16;

	protected float minimalDepth = DEFAULT_MINIMAL_DEPTH;
	protected float maximalDepth = DEFAULT_MAXIMAL_DEPTH;

	protected int viewportXOffset = NO_VIEWPORT_X_OFFSET;
	protected int viewportYOffset = NO_VIEWPORT_Y_OFFSET;

	protected int viewportWidth = DEFAULT_VIEWPORT_WIDTH;
	protected int viewportHeight = DEFAULT_VIEWPORT_HEIGHT;

	protected float positionX = DEFAULT_POSITION_X;
	protected float positionY = DEFAULT_POSITION_Y;
	protected float positionZ = DEFAULT_POSITION_Z;

	protected float directionX = DEFAULT_DIRECTION_X;
	protected float directionY = DEFAULT_DIRECTION_Y;
	protected float directionZ = DEFAULT_DIRECTION_Z;

	protected float axisDirectionX = DEFAULT_AXIS_DIRECTION_X;
	protected float axisDirectionY = DEFAULT_AXIS_DIRECTION_Y;
	protected float axisDirectionZ = DEFAULT_AXIS_DIRECTION_Z;

	protected float[] projectionMatrix = new float[GL_MATRIX_SIZE];
	protected float[] viewMatrix = new float[GL_MATRIX_SIZE];
	protected float[] projectionAndViewMatrix = new float[GL_MATRIX_SIZE];


	public OrthographicCamera(int viewportWidth, int viewportHeight)
	{
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		configureViewport();
		calculateProjectionMatrix();
		calculateViewMatrix();
		calculateProjectionAndViewMatrix();
	}

	protected void configureViewport()
	{
		GLES20.glViewport(viewportXOffset, viewportYOffset, viewportWidth, viewportHeight);
	}

	protected void calculateProjectionMatrix()
	{
		Matrix.orthoM(projectionMatrix, NO_MATRIX_OFFSET, viewportXOffset,
		viewportWidth, viewportYOffset, viewportHeight, minimalDepth,
		maximalDepth);
	}

	protected void calculateViewMatrix()
	{
		Matrix.setLookAtM(viewMatrix, NO_MATRIX_OFFSET, positionX, positionY,
		positionZ, directionX, directionY, directionZ, axisDirectionX, axisDirectionY, axisDirectionZ);
	}

	protected void calculateProjectionAndViewMatrix()
	{
		Matrix.multiplyMM(projectionAndViewMatrix, NO_MATRIX_OFFSET, projectionMatrix, NO_MATRIX_OFFSET, viewMatrix, NO_MATRIX_OFFSET);
	}

	public float[] getProjectionAndViewMatrix()
	{
		return projectionAndViewMatrix;
	}

	public int getViewportWidth()
	{
		return viewportWidth;
	}

	public int getViewportHeight()
	{
		return viewportHeight;
	}

	public void translate(float positionX, float positionY) // Centers camera on an object
	{
		this.positionX = -viewportWidth/2 + positionX; // Alias /2 to center?;
		this.positionY = -viewportHeight/2 + positionY;
		this.directionX = -viewportWidth/2 + positionX;
		this.directionY = -viewportHeight/2 + positionY;
		calculateViewMatrix();
		calculateProjectionAndViewMatrix();
	}
}
