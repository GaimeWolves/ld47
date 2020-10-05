package com.gamewolves.ld47.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.gamewolves.ld47.Main;
import com.gamewolves.ld47.entities.BulletManager;
import com.gamewolves.ld47.entities.Crane;
import com.gamewolves.ld47.entities.Tower;
import com.gamewolves.ld47.entities.enemies.Enemy;
import com.gamewolves.ld47.entities.enemies.WaveManager;
import com.gamewolves.ld47.entities.guns.LaserGun;
import com.gamewolves.ld47.entities.projectiles.Projectile;
import com.gamewolves.ld47.graphics.AnimatedSprite;
import com.gamewolves.ld47.graphics.Button;
import com.gamewolves.ld47.physics.Physics;
import com.gamewolves.ld47.transitions.ITransition;
import com.gamewolves.ld47.transitions.TransitionHandler;

public class Game extends State
{
	private Tower tower = new Tower();
	private Crane crane = new Crane();
	private BulletManager bulletManager = new BulletManager();
	private WaveManager waveManager = new WaveManager();

	private Texture backgroundTexture;
	private AnimatedSprite grassSprite, riverSprite;

	private ITransition fadeInTransition;
	private ITransition fadeOutTransition;
	private ITransition gameOverTransition;
	private boolean retry;
	private boolean isGameOver;

	private Label gameOverLabel;
	private Button retryButton;
	private Button exitButton;
	private Label retryButtonLabel;
	private Label exitButtonLabel;

	private Sound selectSound;
	private Sound hitSound;

	@Override
	public void loadResources(AssetManager assetManager)
	{
		assetManager.load("map/map.png", Texture.class);
		assetManager.load("map/wateranim.png", Texture.class);
		assetManager.load("map/grassanim-Sheet.png", Texture.class);

		assetManager.load("cracter/prof_idle_1.png", Texture.class);
		assetManager.load("cracter/prof_idle_2.png", Texture.class);
		assetManager.load("cracter/prof_idle_3.png", Texture.class);
		assetManager.load("cracter/health_bg.png", Texture.class);
		assetManager.load("cracter/health_bar.png", Texture.class);

		assetManager.load("enemies/1/back_1.png", Texture.class);
		assetManager.load("enemies/1/front_1.png", Texture.class);
		assetManager.load("enemies/1/shoot_1.png", Texture.class);
		assetManager.load("enemies/1/shot.png", Texture.class);
		assetManager.load("enemies/1/side_1.png", Texture.class);

		assetManager.load("enemies/3/hex_beschwör.png", Texture.class);
		assetManager.load("enemies/3/hex_front_back_anim.png", Texture.class);
		assetManager.load("enemies/3/hex_side_anim.png", Texture.class);
		assetManager.load("enemies/3/crabwalk.png", Texture.class);
		assetManager.load("enemies/3/crab_spawn.png", Texture.class);

		assetManager.load("enemies/2/back_2.png", Texture.class);
		assetManager.load("enemies/2/front_2.png", Texture.class);
		assetManager.load("enemies/2/enemy_2_spawnanim.png", Texture.class);
		assetManager.load("enemies/2/ausschluepf_2.png", Texture.class);
		assetManager.load("enemies/2/explode_2.png", Texture.class);
		assetManager.load("enemies/2/side_2.png", Texture.class);
		assetManager.load("enemies/2/charge_2.png", Texture.class);

		assetManager.load("cracter/weapons/weapon_1.png", Texture.class);
		assetManager.load("cracter/weapons/ws_1.png", Texture.class);
		assetManager.load("cracter/weapons/wsanim_1.png", Texture.class);

		assetManager.load("cracter/weapons/weapon_2.png", Texture.class);
		assetManager.load("cracter/weapons/ws_2.png", Texture.class);
		assetManager.load("cracter/weapons/wsanim_2.png", Texture.class);

		assetManager.load("cracter/weapons/weapon_3.png", Texture.class);
		assetManager.load("cracter/weapons/ws_3.png", Texture.class);
		assetManager.load("cracter/weapons/wsanim_3.png", Texture.class);
		assetManager.load("cracter/weapons/explodeanim_3.png", Texture.class);

		assetManager.load("cracter/weapons/weapon_4.png", Texture.class);
		assetManager.load("cracter/weapons/laser_start.png", Texture.class);
		assetManager.load("cracter/weapons/laser.png", Texture.class);

		assetManager.load("map/grap/grab_arm.png", Texture.class);
		assetManager.load("map/grap/grab_grab_grabbed.png", Texture.class);
		assetManager.load("map/grap/grab_grab_opened.png", Texture.class);
		assetManager.load("map/grap/grab_grab.png", Texture.class);

		assetManager.load("menu/button_1.png", Texture.class);
		assetManager.load("menu/button_2.png", Texture.class);
		assetManager.load("menu/button_3.png", Texture.class);
		assetManager.load("menu/button_4.png", Texture.class);

		assetManager.load("sound/explode.wav", Sound.class);
		assetManager.load("sound/hit.wav", Sound.class);
		assetManager.load("sound/laser.wav", Sound.class);
		assetManager.load("sound/newgun.wav", Sound.class);
		assetManager.load("sound/select.wav", Sound.class);
		assetManager.load("sound/shoot.wav", Sound.class);
	}

	@Override
	public void initialize()
	{
		super.initialize();

		selectSound = Main.get().assetManager.get("sound/select.wav");
		hitSound = Main.get().assetManager.get("sound/hit.wav");

		tower.loadResources(Main.get().assetManager);
		crane.loadResources(Main.get().assetManager);
		waveManager.loadResources(Main.get().assetManager);

		tower.initialize(bulletManager);
		crane.initialize();
		waveManager.initialize(bulletManager);

		backgroundTexture = Main.get().assetManager.get("map/map.png");
		grassSprite = new AnimatedSprite((Texture) Main.get().assetManager.get("map/grassanim-Sheet.png"), 320, 320, 2);
		riverSprite = new AnimatedSprite((Texture) Main.get().assetManager.get("map/wateranim.png"), 600, 440, 1);
		grassSprite.setPosition(-backgroundTexture.getWidth() * .5f, -backgroundTexture.getHeight() * .5f);
		riverSprite.setPosition(-backgroundTexture.getWidth() * .5f, -backgroundTexture.getHeight() * .5f);

		retryButton = new Button((Texture) Main.get().assetManager.get("menu/button_3.png"));
		retryButton.setOriginCenter();
		retryButton.setScale(1.5f);
		retryButton.setOriginBasedPosition(0, Main.get().Height * 0f);
		retryButton.updateBoundingBox();

		retryButtonLabel = new Label("RETRY", new Label.LabelStyle(Main.get().font, Color.WHITE));
		retryButtonLabel.setAlignment(Align.center);
		retryButtonLabel.setFontScale(0.4f);
		retryButtonLabel.setPosition(0, Main.get().Height * 0f, Align.center);
		retryButton.setActive(false);

		exitButton = new Button((Texture) Main.get().assetManager.get("menu/button_1.png"));
		exitButton.setOriginCenter();
		exitButton.setScale(1.5f);
		exitButton.setOriginBasedPosition(0, Main.get().Height * -.2f);
		exitButton.updateBoundingBox();
		exitButton.setActive(false);

		exitButtonLabel = new Label("RETURN", new Label.LabelStyle(Main.get().font, Color.WHITE));
		exitButtonLabel.setAlignment(Align.center);
		exitButtonLabel.setFontScale(0.4f);
		exitButtonLabel.setPosition(0, Main.get().Height * -.2f, Align.center);

		gameOverLabel = new Label("GAME OVER", new Label.LabelStyle(Main.get().font, Color.FIREBRICK));
		gameOverLabel.setAlignment(Align.center);
		gameOverLabel.setFontScale(0.8f);
		gameOverLabel.setPosition(0, Main.get().Height * .25f, Align.center);

		initializeTransitions();

		retryButton.setClickListener(() -> {
			selectSound.play(.125f);
			retry = true;
			TransitionHandler.get().setTransition(fadeOutTransition);
		});

		exitButton.setClickListener(() -> {
			selectSound.play(.125f);
			retry = false;
			TransitionHandler.get().setTransition(fadeOutTransition);
		});

		TransitionHandler.get().setTransition(fadeInTransition);

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
						if (crane.getGrabbedEnemy() == null) {
							Projectile projectile = (Projectile) (objectA instanceof Projectile ? objectA : objectB);
							if (projectile.canBeDisposed())
								projectile.setDisposable();
						}
					}
				}
				else if (objectA instanceof Tower || objectB instanceof Tower)
				{
					if (objectA instanceof Enemy || objectB instanceof Enemy)
					{
						Enemy enemy = (Enemy)(objectA instanceof Enemy ? objectA : objectB);

						if (!enemy.isGrabbed()) {
							hitSound.play(0.125f);
							if (tower.hit(enemy.getContactDamage())) {
								TransitionHandler.get().setTransition(gameOverTransition);
							}
							enemy.repell(tower.getPosition());
						}
					}
					else if (objectA instanceof Projectile || objectB instanceof Projectile)
					{
						Projectile projectile = (Projectile) (objectA instanceof Projectile ? objectA : objectB);

						if (!projectile.isPlayerShot())
						{
							hitSound.play(0.125f);
							if (tower.hit(projectile.getDamage()))  {
								TransitionHandler.get().setTransition(gameOverTransition);
							}

							if (projectile.canBeDisposed())
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
							hitSound.play(0.125f);
							enemy.hit(projectile.getDamage());
							if (projectile.canBeDisposed())
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
		if (isGameOver)
			return;

		Vector2 pos = tower.getPosition().scl(1.8f);
		Vector2 cam = new Vector2(Main.get().camera.position.x, Main.get().camera.position.y);
		Vector2 rem = pos.cpy().sub(cam);
		rem.scl(deltaTime * 3);

		((OrthographicCamera)Main.get().camera).translate(rem);

		Physics.update(deltaTime);

		tower.update(deltaTime);
		crane.update(deltaTime);
		bulletManager.update(deltaTime, tower.getPosition(), waveManager.getEnemies());
		waveManager.update(deltaTime, tower);

		grassSprite.update(deltaTime);
		riverSprite.update(deltaTime);
	}

	@Override
	public void render(SpriteBatch spriteBatch)
	{
		spriteBatch.begin();
		spriteBatch.draw(backgroundTexture, -backgroundTexture.getWidth() * .5f, -backgroundTexture.getHeight() * .5f);
		grassSprite.render(spriteBatch);
		riverSprite.render(spriteBatch);
		tower.render(spriteBatch);
		waveManager.render(spriteBatch, tower.getPosition());
		bulletManager.render(spriteBatch);
		crane.render(spriteBatch);
		tower.renderUiInWorldSpace(spriteBatch);
		spriteBatch.end();
	}

	@Override
	public void renderUI(SpriteBatch spriteBatch) {
		spriteBatch.begin();
		waveManager.renderUI(spriteBatch);
		tower.renderUI(spriteBatch);

		if (isGameOver)
		{
			gameOverLabel.draw(spriteBatch, spriteBatch.getColor().a);
			retryButton.draw(spriteBatch);
			retryButtonLabel.draw(spriteBatch, spriteBatch.getColor().a);
			exitButton.draw(spriteBatch);
			exitButtonLabel.draw(spriteBatch, spriteBatch.getColor().a);
		}

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

	// Transitions

	private void initializeTransitions()
	{
		fadeOutTransition = new ITransition()
		{
			float translationValue = 0;
			float retryButtonY = retryButton.getY();
			float exitButtonY = exitButton.getY();
			float retryButtonLabelY = retryButtonLabel.getY();
			float exitButtonLabelY = exitButtonLabel.getY();
			float gameOverLabelY = gameOverLabel.getY();

			@Override
			public void transitionEnter()
			{
				retryButton.setActive(false);
				exitButton.setActive(false);
			}

			@Override
			public boolean update(float deltaTime, float transitionTime)
			{
				translationValue = (float) (Math.sqrt(Math.abs(Math.sin(transitionTime * Math.PI / 2))));

				retryButton.setPosition(retryButton.getX(), retryButtonY - translationValue * Main.get().Height);
				retryButtonLabel.setPosition(retryButtonLabel.getX(), retryButtonLabelY - translationValue * Main.get().Height);
				exitButton.setPosition(exitButton.getX(), exitButtonY - translationValue * Main.get().Height);
				exitButtonLabel.setPosition(exitButtonLabel.getX(),exitButtonLabelY - translationValue * Main.get().Height);
				gameOverLabel.setPosition(gameOverLabel.getX(),gameOverLabelY + translationValue * Main.get().Height);

				return transitionTime >= 1;
			}

			@Override
			public void render(SpriteBatch spriteBatch)
			{
				spriteBatch.setColor(1, 1, 1, Math.min(1, 1 - translationValue));
				Game.this.render(spriteBatch);
				spriteBatch.setColor(Color.WHITE);
			}

			@Override
			public void renderUI(SpriteBatch spriteBatch)
			{
				spriteBatch.setColor(1, 1, 1, Math.min(1, 1 - translationValue));
				Game.this.renderUI(spriteBatch);
				spriteBatch.setColor(Color.WHITE);
			}

			@Override
			public void transitionExit()
			{
				if (retry)
					nextState = new Game();
				else
					nextState = new Menu();

				disposable = true;
			}
		};

		fadeInTransition = new ITransition()
		{
			float translationValue = 0;

			@Override
			public void transitionEnter() { }

			@Override
			public boolean update(float deltaTime, float transitionTime)
			{
				translationValue = (float) (1 - Math.sqrt(Math.sin(transitionTime * Math.PI / 2)));

				Game.this.update(deltaTime);

				return transitionTime >= 1;
			}

			@Override
			public void render(SpriteBatch spriteBatch)
			{
				spriteBatch.setColor(1, 1, 1, Math.min(1, 1 - translationValue));
				Game.this.render(spriteBatch);
				spriteBatch.setColor(Color.WHITE);
			}

			@Override
			public void renderUI(SpriteBatch spriteBatch)
			{
				spriteBatch.setColor(1, 1, 1, Math.min(1, 1 - translationValue));
				Game.this.renderUI(spriteBatch);
				spriteBatch.setColor(Color.WHITE);
			}

			@Override
			public void transitionExit()
			{

			}
		};

		gameOverTransition = new ITransition()
		{
			float translationValue = 0;
			float retryButtonY = retryButton.getY();
			float exitButtonY = exitButton.getY();
			float retryButtonLabelY = retryButtonLabel.getY();
			float exitButtonLabelY = exitButtonLabel.getY();
			float gameOverLabelY = gameOverLabel.getY();

			@Override
			public void transitionEnter()
			{
				update(0, 0);
				isGameOver = true;
			}

			@Override
			public boolean update(float deltaTime, float transitionTime)
			{
				translationValue = (float) (1 - Math.sqrt(Math.sin(transitionTime * Math.PI / 2)));

				retryButton.setPosition(retryButton.getX(), retryButtonY - translationValue * Main.get().Height);
				retryButtonLabel.setPosition(retryButtonLabel.getX(), retryButtonLabelY - translationValue * Main.get().Height);
				exitButton.setPosition(exitButton.getX(), exitButtonY - translationValue * Main.get().Height);
				exitButtonLabel.setPosition(exitButtonLabel.getX(),exitButtonLabelY - translationValue * Main.get().Height);
				gameOverLabel.setPosition(gameOverLabel.getX(),gameOverLabelY + translationValue * Main.get().Height);

				return transitionTime >= 1;
			}

			@Override
			public void render(SpriteBatch spriteBatch)
			{
				Game.this.render(spriteBatch);
			}

			@Override
			public void renderUI(SpriteBatch spriteBatch)
			{
				Game.this.renderUI(spriteBatch);
			}

			@Override
			public void transitionExit()
			{
				retryButton.setPosition(retryButton.getX(), retryButtonY);
				retryButtonLabel.setPosition(retryButtonLabel.getX(), retryButtonLabelY);
				exitButton.setPosition(exitButton.getX(), exitButtonY);
				exitButtonLabel.setPosition(exitButtonLabel.getX(), exitButtonLabelY);
				gameOverLabel.setPosition(gameOverLabel.getX(), gameOverLabelY);

				retryButton.setActive(true);
				exitButton.setActive(true);
			}
		};
	}
}
