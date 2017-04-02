package khorrth.discovery.scene;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class SceneView extends GLSurfaceView
{
	private final float TOUCH_FACTOR = 4.0f / 360; // 4 is zoom of sprites, 360 is lower side of screen

	private final int CURRENT_EGL_CONTEXT_CLIENT_VERSION = 2;

	private final SceneRenderer renderer;
	private Context context;

	public SceneView(Context context)
	{
		super(context);
		this.context = context;

		setEGLContextClientVersion(CURRENT_EGL_CONTEXT_CLIENT_VERSION);
		renderer = new SceneRenderer(context);
		setRenderer(renderer);
	}

	private float mPreviousX = 0;
	private float mPreviousY = 0;
	float x;
	float y;
	float dx;
	float dy;

	public boolean onTouchEvent(MotionEvent currentTouchEvent)
	{
		// Joystick implemented here
		switch (currentTouchEvent.getAction())
		{
			case MotionEvent.ACTION_MOVE:
				x = currentTouchEvent.getX() * TOUCH_FACTOR;
				y = -currentTouchEvent.getY() * TOUCH_FACTOR;
				dx = x - mPreviousX;
				dy = y - mPreviousY;
				renderer.deltaX = dx;
				renderer.deltaY = dy;
			break;

			case MotionEvent.ACTION_DOWN:
				x = currentTouchEvent.getX() * TOUCH_FACTOR;
				y = -currentTouchEvent.getY() * TOUCH_FACTOR;
				mPreviousX = x;
				mPreviousY = y;
			break;

			case MotionEvent.ACTION_UP:
				x = currentTouchEvent.getX() * TOUCH_FACTOR;
				y = -currentTouchEvent.getY() * TOUCH_FACTOR;
				mPreviousX = x;
				mPreviousY = y;
				dx = x - mPreviousX;
				dy = y - mPreviousY;
				renderer.deltaX = dx;
				renderer.deltaY = dy;
			break;
		}
		// requestRender(); // ???
		return true; // true alias ?
	}
}
