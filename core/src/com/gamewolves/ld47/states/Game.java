package com.gamewolves.ld47.states;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.gamewolves.ld47.Main;
import com.gamewolves.ld47.entities.BulletManager;
import com.gamewolves.ld47.entities.Crane;
import com.gamewolves.ld47.entities.Tower;
import com.gamewolves.ld47.entities.enemies.Enemy;
import com.gamewolves.ld47.entities.enemies.WaveManager;
import com.gamewolves.ld47.entities.projectiles.Projectile;
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

		Physics.getWorld().setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				Object objectA = contact.getFixtureA().getUserData();
				Object objectB = contact.getFixtureB().getUserData();

				if (objectA instanceof Crane || objectB instanceof Crane) {
					if (crane.getGrabbedEnemy() == null) {
						if (objectA instanceof Enemy || objectB instanceof Enemy) {
							Enemy enemy = (Enemy)(objectA instanceof Enemy ? objectA : objectB);

							crane.setGrabbedEnemy(enemy);
							enemy.grab();
						}
					}
				}
				else if (objectA instanceof Tower || objectB instanceof Tower)
				{
					if (objectA instanceof Enemy || objectB instanceof Enemy)
					{
						// Todo: gameover
						System.out.println("Gem ovär");
					}
					else if (objectA instanceof Projectile || objectB instanceof Projectile)
					{
						Projectile projectile = (Projectile) (objectA instanceof Projectile ? objectA : objectB);

						if (!projectile.isPlayerShot())
						{
							// Todo: gameover
							System.out.println("Gem ovär");
						}
					}
				}
				else if (objectA instanceof Enemy || objectB instanceof Enemy)
				{
					if (objectA instanceof Projectile || objectB instanceof Projectile)
					{
						Projectile projectile = (Projectile) (objectA instanceof Projectile ? objectA : objectB);
						Enemy enemy = (Enemy)(objectA instanceof Enemy ? objectA : objectB);

						if (projectile.isPlayerShot())
						{
							enemy.hit(projectile.getDamage());
							projectile.setDisposable();
						}
					}
				}
			}

			@Override
			public void endContact(Contact contact) {

			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {

			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {

			}
		});
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
