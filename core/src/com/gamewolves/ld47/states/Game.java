package com.gamewolves.ld47.states;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gamewolves.ld47.Main;
import com.gamewolves.ld47.entities.BulletManager;
import com.gamewolves.ld47.entities.Crane;
import com.gamewolves.ld47.entities.Tower;
import com.gamewolves.ld47.entities.enemies.WaveManager;
import com.gamewolves.ld47.physics.Physics;

public class Game extends State
{
	private Tower tower = new Tower();
	private Crane crane = new Crane();
	private BulletManager bulletManager = new BulletManager();
	private WaveManager waveManager = new WaveManager();

	@Override
	public void loadResources(AssetManager assetManager)
	{
		tower.loadResources(assetManager);
		crane.loadResources(assetManager);
		waveManager.loadResources(assetManager);
	}

	@Override
	public void initialize()
	{
		super.initialize();
		nextState = new Game();

		tower.initialize(bulletManager);
		crane.initialize();
		waveManager.initialize(bulletManager);
	}

	@Override
	public void update(float deltaTime)
	{
		Vector2 pos = tower.getPosition();
		Vector2 cam = new Vector2(Main.get().camera.position.x, Main.get().camera.position.y);
		Vector2 rem = pos.cpy().sub(cam);
		rem.scl(deltaTime * 3);

		((OrthographicCamera)Main.get().camera).translate(rem);

		Physics.update(deltaTime);

		tower.update(deltaTime);
		crane.update(deltaTime);
		bulletManager.update(deltaTime);
		waveManager.update(deltaTime, tower.getPosition());
	}

	@Override
	public void render(SpriteBatch spriteBatch)
	{
		tower.render(spriteBatch);
		crane.render(spriteBatch);
		bulletManager.render(spriteBatch);
		waveManager.render(spriteBatch, tower.getPosition());
	}

	@Override
	public void dispose(AssetManager assetManager)
	{
		tower.dispose(assetManager);
		crane.dispose(assetManager);
		bulletManager.dispose(assetManager);
		waveManager.dispose(assetManager);
	}
}
