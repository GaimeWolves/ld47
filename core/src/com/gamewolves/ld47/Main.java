package com.gamewolves.ld47;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gamewolves.ld47.assetloaders.LoaderRegistrar;
import com.gamewolves.ld47.input.InputHandler;
import com.gamewolves.ld47.physics.Physics;
import com.gamewolves.ld47.states.Menu;
import com.gamewolves.ld47.states.State;
import com.gamewolves.ld47.transitions.TransitionHandler;
import com.gamewolves.ld47.utils.GamePreferences;

public class Main extends ApplicationAdapter 
{
	private static Main Instance; // Singleton
	public SpriteBatch spriteBatch;
	public ShapeRenderer shapeRenderer;
	public PolygonSpriteBatch polySpriteBatch;
	public Viewport viewport;
	public Camera camera;
	public float Width;
	public float Height;
	public AssetManager assetManager;
	public BitmapFont font;
	public State currentState;
	public float elapsedTime = 0;
	public XmlReader xmlReader = new XmlReader();

	public static Main get() { return Instance; }

	@Override
	public void create ()
	{
		Instance = this;

		spriteBatch = new SpriteBatch();
		polySpriteBatch = new PolygonSpriteBatch();
		shapeRenderer = new ShapeRenderer();

		// Todo get screen size properly
		float aspect_ratio = (float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
		Width = 1080;
		Height = Width / aspect_ratio;
		camera = new OrthographicCamera(Width, Height);
		camera.translate(Width / 2, Height / 2, 0);
		viewport = new StretchViewport(Width, Height, camera);
		camera.update();

		assetManager = new AssetManager();
		LoaderRegistrar.registerLoaders(assetManager);

		//Initial loading (font, loading bar, etc)
		GamePreferences.init();
		Physics.init();

		currentState = new Menu();
		currentState.loadResources(assetManager);
	}

	private void update(float deltaTime)
	{
		camera.update();
		elapsedTime += deltaTime;

		assetManager.update();
		if (!assetManager.isFinished())
			return;

		manageState();

		InputHandler.get().update();

		if (TransitionHandler.get().inTransition())
			TransitionHandler.get().update(deltaTime);
		else if (currentState.isInitialized())
			currentState.update(deltaTime);
	}

	@Override
	public void render ()
	{
		update(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		spriteBatch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
		polySpriteBatch.setProjectionMatrix(camera.combined);

		renderLoadingScreen();
		if (true) return;

		// State finished loading?
		if (assetManager.isFinished() && currentState.isInitialized())
		{
			if (TransitionHandler.get().inTransition())
				TransitionHandler.get().render(spriteBatch);
			else
				currentState.render(spriteBatch);
		}
		else
			renderLoadingScreen();

		Physics.renderDebug(spriteBatch);
	}
	
	private void renderLoadingScreen()
	{
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.WHITE);

		for (int i = 0; i < 3; i++)
		{
			float x = Width / 2 - 60 + 40 * i + 15;
			float y = (float) (Height / 2 + Math.sin(elapsedTime * 10 + i * Math.PI / 3) * 30);

			shapeRenderer.circle(x, y, 15);
		}

		shapeRenderer.end();
	}

	private void manageState()
	{
		if (!currentState.isInitialized() && assetManager.isFinished())
			currentState.initialize();

		if (currentState.isDisposable())
		{
			if (currentState.getNextState() == null)
				throw new IllegalStateException("Current state disposed but next state undefined");

			currentState.dispose(assetManager);
			currentState = currentState.createNextState();
			currentState.loadResources(assetManager);
		}
	}

	@Override
	public void dispose()
	{
		if (currentState != null)
			currentState.dispose(assetManager);

		spriteBatch.dispose();
		shapeRenderer.dispose();
		assetManager.dispose();
	}
}
