package khorrth.discovery.scene;

import android.app.Activity;
import android.os.Bundle;

public class SceneActivity extends Activity {

	private SceneView view;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		view = new SceneView(this);
		setContentView(view);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		view.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		view.onResume();
	}
}
