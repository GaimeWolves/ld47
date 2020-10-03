package com.gamewolves.ld47.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.gamewolves.ld47.Main;

import box2dLight.RayHandler;

public class Physics
{
	private static final Color AmbientColor = new Color(0.2f, 0.2f, 0.2f, 0.4f);

	private static Physics Instance;

	private World world;
	private RayHandler rayHandler;
	private Box2DDebugRenderer debugRenderer;

	private boolean drawDebug = false;

	private Physics()
	{
		world = new World(new Vector2(), true);
		world.setAutoClearForces(true);

		RayHandler.setGammaCorrection(true);
		RayHandler.useDiffuseLight(false);

		rayHandler = new RayHandler(world);
		rayHandler.setCombinedMatrix(Main.get().camera.combined.cpy(), 0, 0, Main.get().camera.viewportWidth, Main.get().camera.viewportHeight);
		rayHandler.setAmbientLight(AmbientColor);
		rayHandler.setBlurNum(3);
		rayHandler.setShadows(true);

		debugRenderer = new Box2DDebugRenderer();
	}

	public static void init()
	{
		Instance = new Physics();
	}

	public static void update(float deltaTime)
	{
		Instance.world.step(deltaTime, 8, 3);

		if (Gdx.input.isKeyJustPressed(Input.Keys.F1))
			Instance.drawDebug = !Instance.drawDebug;
	}

	public static void render()
	{
		Instance.rayHandler.updateAndRender();
	}

	public static void renderDebug(SpriteBatch batch)
	{
		if (Instance.drawDebug)
		{
			batch.begin();
			Instance.debugRenderer.render(Instance.world, Main.get().camera.combined.cpy());
			batch.end();
		}
	}

	public static void dispose()
	{
		Instance.rayHandler.dispose();
		Instance.world.dispose();
	}

	public static World getWorld() { return Instance.world; }
	public static RayHandler getRayHandler() { return Instance.rayHandler; }
}
