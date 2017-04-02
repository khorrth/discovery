package khorrth.discovery.scene;

import khorrth.discovery.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.opengl.GLES20;

import android.graphics.BitmapFactory;
import android.graphics.Bitmap;

import android.opengl.GLUtils;

import java.util.Random;

import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

public class SceneRenderer implements GLSurfaceView.Renderer
{
	protected final int NO_MATRIX_OFFSET = 0;
	protected final int GL_MATRIX_SIZE = 16;

	protected final int GL_FLOAT_SIZE = 4;
	protected final int GL_SHORT_SIZE = 4;

	protected final float GL_CLOCKWISE_ROTATION_MATRIX_X = 0.0f;
	protected final float GL_CLOCKWISE_ROTATION_MATRIX_Y = 0.0f;
	protected final float GL_CLOCKWISE_ROTATION_MATRIX_Z = 1.0f;

	protected final float GL_DEFAULT_TRANSLATION_MATRIX_Z = 0.0f;

	private final int BUFFER_START_POSITION = 0;


	protected FloatBuffer DEFAULT_MODEL_TEXTURE_COORDINATES_BUFFER;
	protected ShortBuffer DEFAULT_MODEL_TEXTURE_DRAW_ORDER_BUFFER;

	protected Context context;

	protected OrthographicCamera camera;

	protected int shaderProgram;

	public float deltaX = 0;
	public float deltaY = 0;

	protected Location currentLocation;

	public SceneRenderer(Context context)
	{
		this.context = context;
		random = new Random();
	}

	private void enableAlphaBlending()
	{
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		GLES20.glEnable(GLES20.GL_BLEND);
	}

	private void disableAlphaBlending()
	{
		GLES20.glDisable(GLES20.GL_BLEND);
	}

	// Move to scene somehow..?
	private void renderBackground()
	{
		GLES20.glClearColor(18.0f/255.0f, 30.0f/255.0f, 32.0f/255.0f, 1.0f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
	}

	private void renderChunk(Chunk renderableChunk)
	{
	 	for (int currentChunkStructureIndex = 0; currentChunkStructureIndex < renderableChunk.structures.size(); currentChunkStructureIndex++)
	 	{
	 	 	renderModel(renderableChunk.structures.get(currentChunkStructureIndex));
	 	}
	 	// renderModel(renderableChunk.structures.get(0));
	 }


	private void loadShaders()
	{
		int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		GLES20.glShaderSource(vertexShader, context.getString(R.string.DEFAULT_MODEL_VERTEX_SHADER_CODE));
		GLES20.glCompileShader(vertexShader);

		int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
		GLES20.glShaderSource(fragmentShader, context.getString(R.string.DEFAULT_MODEL_FRAGMENT_SHADER_CODE));
		GLES20.glCompileShader(fragmentShader);

		shaderProgram = GLES20.glCreateProgram();
		GLES20.glAttachShader(shaderProgram, vertexShader);
		GLES20.glAttachShader(shaderProgram, fragmentShader);
		GLES20.glLinkProgram(shaderProgram);
	}

	// Rename maybe?
	private void configureShaderProgram()
	{
		GLES20.glUseProgram(shaderProgram);
	}

	private void renderLocation()
	{
		renderChunk(currentLocation.getChunk(0, 0));
	}

	@Override
	public void onDrawFrame(GL10 unused)
	{
		enableAlphaBlending();
		renderBackground();
		renderLocation();
		disableAlphaBlending();
	}

	private float[] calculateModelVertices(Model model)
	{
		// return new float[] { -model.getWidth()/2, -model.getHeight()/2, 0.f,
		//                      -model.getWidth()/2,  model.getHeight()/2, 0.f,
		//                       model.getWidth()/2, -model.getHeight()/2, 0.f,
		//                       model.getWidth()/2,  model.getHeight()/2, 0.f, };
		return new float[] {                0,                 0, 0.f,
		                                    0, model.getHeight(), 0.f,
		                     model.getWidth(),                 0, 0.f,
		                     model.getWidth(), model.getHeight(), 0.f, };
	}

	protected final float[] DEFAULT_MODEL_TEXTURE_COORDINATES = new float[] {
		0.0f,  1.0f,
		0.0f,  0.0f,
		1.0f,  1.0f,
		1.0f,  0.0f,
	};

	protected final short[] DEFAULT_MODEL_TEXTURE_DRAW_ORDER = new short[] { 0, 1, 2, 3 };

	float[] DEFAULT_MODEL_INDENTITY_MATRIX =  new float[GL_MATRIX_SIZE];

	private void renderModel(Model model)
	{
		int modelTextureHandle;

		float[] modelIdentityMatrix = DEFAULT_MODEL_INDENTITY_MATRIX;

		float[] modelTextureCoordinates = DEFAULT_MODEL_TEXTURE_COORDINATES;

		short[] modelTextureDrawOrder = DEFAULT_MODEL_TEXTURE_DRAW_ORDER;

		ShortBuffer modelTextureDrawOrderBuffer = DEFAULT_MODEL_TEXTURE_DRAW_ORDER_BUFFER;
		FloatBuffer modelTextureCoordinatesBuffer = DEFAULT_MODEL_TEXTURE_COORDINATES_BUFFER;

		modelTextureHandle = model.getCurrentAnimationFrame();

		float[] modelVertices = calculateModelVertices(model);

		FloatBuffer modelVertexBuffer;
		modelVertexBuffer = ByteBuffer.allocateDirect(modelVertices.length * GL_FLOAT_SIZE).order(ByteOrder.nativeOrder()).asFloatBuffer();
		modelVertexBuffer.put(modelVertices).position(BUFFER_START_POSITION);

		loadShaders();

		configureShaderProgram();

		float[] rotationMatrix = calculateRotationMatrix(model);
		float[] translatedIdentityMatrix = calculateTranslatedIdentityMatrix(model);
		float[] modelMatrix = calculateCombinedModelMatrix(rotationMatrix, translatedIdentityMatrix);


		int modelPositionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
		GLES20.glEnableVertexAttribArray(modelPositionHandle);
		GLES20.glVertexAttribPointer(modelPositionHandle, 3, GLES20.GL_FLOAT, false, 0, modelVertexBuffer);

		int mTextureUniformHandle = GLES20.glGetAttribLocation(shaderProgram, "u_Texture");
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, model.getCurrentAnimationFrame());
		GLES20.glUniform1i(mTextureUniformHandle, 0);

		int mTextureCoordinateHandle = GLES20.glGetAttribLocation(shaderProgram, "a_TexCoordinate");
		GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
		GLES20.glVertexAttribPointer(mTextureCoordinateHandle, 2, GLES20.GL_FLOAT,
		false, 0, modelTextureCoordinatesBuffer);

		float[] cameraAndModelMatrix = new float[16]; // Used as a matrix for rendering
		Matrix.multiplyMM(cameraAndModelMatrix, 0, camera.getProjectionAndViewMatrix(), 0, modelMatrix, 0);
		int mtrxhandle = GLES20.glGetUniformLocation(shaderProgram, "uMVPMatrix");
		GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, cameraAndModelMatrix, 0);

		GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, modelVertices.length, GLES20.GL_UNSIGNED_SHORT, modelTextureDrawOrderBuffer);

		GLES20.glDisableVertexAttribArray(modelPositionHandle);
		GLES20.glDisableVertexAttribArray(mTextureCoordinateHandle);
	}

	protected float[] calculateCombinedModelMatrix(float[] translatedIdentityMatrix, float[] rotationMatrix)
	{
		float[] combinedModelMatrix = new float[GL_MATRIX_SIZE];
		Matrix.multiplyMM(combinedModelMatrix, NO_MATRIX_OFFSET, translatedIdentityMatrix, NO_MATRIX_OFFSET, rotationMatrix, NO_MATRIX_OFFSET);
		return combinedModelMatrix;
	}

	protected float[] calculateTranslatedIdentityMatrix(Model subject)
	{
		float[] translatedIdentityMatrix = DEFAULT_MODEL_INDENTITY_MATRIX.clone();
		Matrix.translateM(translatedIdentityMatrix, NO_MATRIX_OFFSET, subject.getPositionX(), subject.getPositionY(), GL_DEFAULT_TRANSLATION_MATRIX_Z);
		return translatedIdentityMatrix;
	}

	protected float[] calculateRotationMatrix(Model subject)
	{
		float[] rotationMatrix = new float[GL_MATRIX_SIZE];
	 	Matrix.setRotateM(rotationMatrix, NO_MATRIX_OFFSET, subject.getRotationAngle(), GL_CLOCKWISE_ROTATION_MATRIX_X, GL_CLOCKWISE_ROTATION_MATRIX_Y, GL_CLOCKWISE_ROTATION_MATRIX_Z);
		return rotationMatrix;
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		camera = new OrthographicCamera(width, height);
	}

	private void configureCamera() // initially // to be refactored
	{
		camera = new OrthographicCamera(OrthographicCamera.DEFAULT_VIEWPORT_WIDTH, OrthographicCamera.DEFAULT_VIEWPORT_HEIGHT);
	}

	protected float[] calculateIdentityMatrix()
	{
		float[] identityMatrix = new float[GL_MATRIX_SIZE];
		Matrix.setIdentityM(identityMatrix, NO_MATRIX_OFFSET);
		return identityMatrix;
	}

	int scale = 2;

	int tileSize = 32;

	Random random;

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		DEFAULT_MODEL_TEXTURE_COORDINATES_BUFFER = ByteBuffer.allocateDirect(DEFAULT_MODEL_TEXTURE_COORDINATES.length * GL_FLOAT_SIZE).order(ByteOrder.nativeOrder()).asFloatBuffer();
		DEFAULT_MODEL_TEXTURE_COORDINATES_BUFFER.put(DEFAULT_MODEL_TEXTURE_COORDINATES).position(BUFFER_START_POSITION);

		DEFAULT_MODEL_TEXTURE_DRAW_ORDER_BUFFER = ByteBuffer.allocateDirect(DEFAULT_MODEL_TEXTURE_DRAW_ORDER.length * GL_SHORT_SIZE).order(ByteOrder.nativeOrder()).asShortBuffer();
		DEFAULT_MODEL_TEXTURE_DRAW_ORDER_BUFFER.put(DEFAULT_MODEL_TEXTURE_DRAW_ORDER).position(BUFFER_START_POSITION);

		DEFAULT_MODEL_INDENTITY_MATRIX = calculateIdentityMatrix();

		configureCamera();



		currentLocation = new Location();
		Chunk testChunk = new Chunk();
		currentLocation.addChunk(testChunk);
		for (int currentStructureIndex = 0;  currentStructureIndex < random.nextInt(6); currentStructureIndex++)
		{
			testChunk.addStructure(new Structure(tileSize*random.nextInt(currentLocation.DEFAULT_CHUNK_SIZE), tileSize*random.nextInt(currentLocation.DEFAULT_CHUNK_SIZE), 32*scale, 32*scale, new BrownGrassTextureDictionary(context)));
		}
		for (int currentStructureIndex = 0;  currentStructureIndex < random.nextInt(6); currentStructureIndex++)
		{
			testChunk.addStructure(new Structure(tileSize*random.nextInt(currentLocation.DEFAULT_CHUNK_SIZE), tileSize*random.nextInt(currentLocation.DEFAULT_CHUNK_SIZE), 32*scale, 32*scale, new GreenGrassTextureDictionary(context)));
		}
		for (int currentStructureIndex = 0;  currentStructureIndex < random.nextInt(6); currentStructureIndex++)
		{
			testChunk.addStructure(new Structure(tileSize*random.nextInt(currentLocation.DEFAULT_CHUNK_SIZE), tileSize*random.nextInt(currentLocation.DEFAULT_CHUNK_SIZE), 128*scale, 128*scale, new TreeTextureDictionary(context)));
		}
	}
}
