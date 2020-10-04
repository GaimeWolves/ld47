package com.gamewolves.ld47.states;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import com.gamewolves.ld47.entities.guns.LaserGun;
import com.gamewolves.ld47.entities.projectiles.Projectile;
import com.gamewolves.ld47.graphics.AnimatedSprite;
import com.gamewolves.ld47.physics.Physics;

public class Game extends State
{
	private Tower tower = new Tower();
	private Crane crane = new Crane();
	private BulletManager bulletManager = new BulletManager();
	private WaveManager waveManager = new WaveManager();

	private Texture backgroundTexture;
	private AnimatedSprite grassSprite, riverSprite;

	@Override
	public void loadResources(AssetManager assetManager)
	{
		assetManager.load("map/map.png", Texture.class);
		//assetManager.load("map/wateranimation_sheet.png", Texture.class);
		assetManager.load("map/grassanim-Sheet.png", Texture.class);

		assetManager.load("cracter/prof_idle_1.png", Texture.class);
		assetManager.load("cracter/prof_idle_2.png", Texture.class);
		assetManager.load("cracter/prof_idle_3.png", Texture.class);

		assetManager.load("enemies/1/back_1.png", Texture.class);
		assetManager.load("enemies/1/front_1.png", Texture.class);
		assetManager.load("enemies/1/shoot_1.png", Texture.class);
		assetManager.load("enemies/1/shot.png", Texture.class);
		assetManager.load("enemies/1/side_1.png", Texture.class);

		assetManager.load("cracter/weapons/weapon_1.png", Texture.class);
		assetManager.load("cracter/weapons/ws_1.png", Texture.class);
		assetManager.load("cracter/weapons/wsanim_1.png", Texture.class);

		assetManager.load("cracter/weapons/weapon_4.png", Texture.class);
		assetManager.load("cracter/weapons/laser_start.png", Texture.class);
		assetManager.load("cracter/weapons/laser.png", Texture.class);

		assetManager.load("map/grap/grab_arm.png", Texture.class);
		assetManager.load("map/grap/grab_grab_grabbed.png", Texture.class);
		assetManager.load("map/grap/grab_grab_opened.png", Texture.class);
		assetManager.load("map/grap/grab_grab.png", Texture.class);
	}

	@Override
	public void initialize()
	{
		super.initialize();
		nextState = new Game();

		tower.loadResources(Main.get().assetManager);
		crane.loadResources(Main.get().assetManager);
		waveManager.loadResources(Main.get().assetManager);

		tower.initialize(bulletManager);
		crane.initialize();
		waveManager.initialize(bulletManager);

		backgroundTexture = Main.get().assetManager.get("map/map.png");
		grassSprite = new AnimatedSprite((Texture) Main.get().assetManager.get("map/grassanim-Sheet.png"), backgroundTexture.getWidth(), backgroundTexture.getHeight(), 2);
		//riverSprite = new AnimatedSprite((Texture) Main.get().assetManager.get("map/wateranimation_sheet.png"), backgroundTexture.getWidth(), backgroundTexture.getHeight(), 1);
		grassSprite.setPosition(-backgroundTexture.getWidth() * .5f, -backgroundTexture.getHeight() * .5f);
		//riverSprite.setPosition(-backgroundTexture.getWidth() * .5f, -backgroundTexture.getHeight() * .5f);

		Physics.getWorld().setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				Object objectA = contact.getFixtureA().getUserData();
				Object objectB = contact.getFixtureB().getUserData();

				if (objectA instanceof Crane || objectB instanceof Crane) {
					if (objectA instanceof Enemy || objectB instanceof Enemy) {
						Enemy enemy = (Enemy) (objectA instanceof Enemy ? objectA : objectB);
						crane.addHoveredEnemy(enemy);
					} else if (objectA instanceof Projectile || objectB instanceof Projectile) {
						Projectile projectile = (Projectile) (objectA instanceof Projectile ? objectA : objectB);
						projectile.setDisposable();
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

							projectile.setDisposable();
						}
					}
				}
				else if (objectA instanceof Enemy || objectB instanceof Enemy)
				{
					Enemy enemy = (Enemy)(objectA instanceof Enemy ? objectA : objectB);

					if (objectA instanceof Projectile || objectB instanceof Projectile)
					{
						Projectile projectile = (Projectile) (objectA instanceof Projectile ? objectA : objectB);

						if (projectile.isPlayerShot())
						{
							enemy.hit(projectile.getDamage());
							projectile.setDisposable();
						}
					}
					else if (objectA instanceof LaserGun || objectB instanceof LaserGun)
					{
						enemy.setLasered(true);
					}
				}
			}

			@Override
			public void endContact(Contact contact) {
				Object objectA = contact.getFixtureA().getUserData();
				Object objectB = contact.getFixtureB().getUserData();

				if (objectA instanceof Crane || objectB instanceof Crane) {
					if (objectA instanceof Enemy || objectB instanceof Enemy) {
						Enemy enemy = (Enemy) (objectA instanceof Enemy ? objectA : objectB);
						crane.removeHoveredEnemy(enemy);
					}
				}
				else if (objectA instanceof Enemy || objectB instanceof Enemy)
				{
					if (objectA instanceof LaserGun || objectB instanceof LaserGun)
					{
						Enemy enemy = (Enemy)(objectA instanceof Enemy ? objectA : objectB);
						enemy.setLasered(false);
					}
				}
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
		Vector2 pos = tower.getPosition().scl(1.8f);
		Vector2 cam = new Vector2(Main.get().camera.position.x, Main.get().camera.position.y);
		Vector2 rem = pos.cpy().sub(cam);
		rem.scl(deltaTime * 3);

		((OrthographicCamera)Main.get().camera).translate(rem);

		Physics.update(deltaTime);

		tower.update(deltaTime);
		crane.update(deltaTime);
		bulletManager.update(deltaTime);
		waveManager.update(deltaTime, tower.getPosition());

		grassSprite.update(deltaTime);
		//riverSprite.update(deltaTime);
	}

	@Override
	public void render(SpriteBatch spriteBatch)
	{
		spriteBatch.begin();
		spriteBatch.draw(backgroundTexture, -backgroundTexture.getWidth() * .5f, -backgroundTexture.getHeight() * .5f);
		//grassSprite.render(spriteBatch);
		//riverSprite.render(spriteBatch);
		tower.render(spriteBatch);
		waveManager.render(spriteBatch, tower.getPosition());
		bulletManager.render(spriteBatch);
		crane.render(spriteBatch);
		spriteBatch.end();
	}

	@Override
	public void renderUI(SpriteBatch spriteBatch) {
		spriteBatch.begin();
		waveManager.renderUI(spriteBatch);
		spriteBatch.end();
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
