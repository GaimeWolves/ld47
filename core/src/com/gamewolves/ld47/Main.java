package com.gamewolves.ld47;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
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
import com.gamewolves.ld47.states.Game;
import com.gamewolves.ld47.states.Menu;
import com.gamewolves.ld47.states.State;
import com.gamewolves.ld47.transitions.TransitionHandler;
import com.gamewolves.ld47.utils.GamePreferences;

public class Main extends ApplicationAdapter 
{
	public enum Device
	{
		DESKTOP,
		WEBAPP
	}

	private static Main Instance; // Singleton
	public SpriteBatch spriteBatch, UISpriteBatch;
	public ShapeRenderer shapeRenderer;
	public PolygonSpriteBatch polySpriteBatch;
	public Viewport viewport, UIViewport;
	public Camera camera, UICamera;
	public float Width;
	public float Height;
	public AssetManager assetManager;
	public BitmapFont font;
	public State currentState;
	public float elapsedTime = 0;
	public XmlReader xmlReader = new XmlReader();
	public Device device;
	public Music music;

	private float scale = .8f;

	public static Main get() { return Instance; }

	@Override
	public void create ()
	{
		Instance = this;

		spriteBatch = new SpriteBatch();
		UISpriteBatch = new SpriteBatch();
		polySpriteBatch = new PolygonSpriteBatch();
		shapeRenderer = new ShapeRenderer();

		if (device == Device.DESKTOP)
		{
			float w = Gdx.app.getGraphics().getDisplayMode().width;
			float h = Gdx.app.getGraphics().getDisplayMode().height;
			int size = (int) (Math.min(w, h) * scale);
			Gdx.graphics.setResizable(false);
			Gdx.graphics.setWindowedMode(size, size);
		}

		Width = 280;
		Height = 280;
		camera = new OrthographicCamera(Width, Height);
		viewport = new StretchViewport(Width, Height, camera);
		camera.update();

		UICamera = new OrthographicCamera(Width, Height);
		UIViewport = new StretchViewport(Width, Height, UICamera);
		UICamera.update();

		assetManager = new AssetManager(new InternalFileHandleResolver());
		LoaderRegistrar.registerLoaders(assetManager);

		//Initial loading (font, loading bar, etc)
		GamePreferences.init();
		Physics.init();
		assetManager.load("font.fnt", BitmapFont.class);
		assetManager.load("sound/moosik.ogg", Music.class);
		assetManager.finishLoading();
		font = assetManager.get("font.fnt");
		music = assetManager.get("sound/moosik.ogg");
		music.setLooping(true);
		music.play();
		music.setVolume(.125f);

		currentState = new Menu();
		currentState.loadResources(assetManager);
	}

	private void update(float deltaTime)
	{
		camera.update();
		UICamera.update();
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
		if (Gdx.input.isKeyJustPressed(Input.Keys.PLUS)) {
			scale += .1f;
			if (device == Device.DESKTOP)
			{
				float w = Gdx.app.getGraphics().getDisplayMode().width;
				float h = Gdx.app.getGraphics().getDisplayMode().height;
				int size = (int) (Math.min(w, h) * scale);
				Gdx.graphics.setResizable(false);
				Gdx.graphics.setWindowedMode(size, size);
			}
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) {
			scale -= .1f;
			if (device == Device.DESKTOP)
			{
				float w = Gdx.app.getGraphics().getDisplayMode().width;
				float h = Gdx.app.getGraphics().getDisplayMode().height;
				int size = (int) (Math.min(w, h) * scale);
				Gdx.graphics.setResizable(false);
				Gdx.graphics.setWindowedMode(size, size);
			}
		}

		update(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		spriteBatch.setProjectionMatrix(camera.combined);
		UISpriteBatch.setProjectionMatrix(UICamera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
		polySpriteBatch.setProjectionMatrix(camera.combined);

		// State finished loading?
		if (assetManager.isFinished() && currentState.isInitialized())
		{
			if (TransitionHandler.get().inTransition()) {
				TransitionHandler.get().render(spriteBatch);
				TransitionHandler.get().renderUI(UISpriteBatch);
			}
			else {
				currentState.render(spriteBatch);
				currentState.renderUI(UISpriteBatch);
			}
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
			float x = -20f + 15 * i;
			float y = (float) (Math.sin(elapsedTime * 10 + i * Math.PI / 3) * 10);

			shapeRenderer.circle(x, y, 5);
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

		music.stop();
		music.dispose();

		spriteBatch.dispose();
		shapeRenderer.dispose();
		assetManager.dispose();
	}
}
