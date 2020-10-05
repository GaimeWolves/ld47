package com.gamewolves.ld47.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.gamewolves.ld47.Main;
import com.gamewolves.ld47.graphics.Button;
import com.gamewolves.ld47.transitions.ITransition;
import com.gamewolves.ld47.transitions.TransitionHandler;

public class Menu extends State
{
	private Texture backgroundTexture;

	// UI Elements
	private Button playButton;
	private Button exitButton;

	private Label playButtonLabel;
	private Label exitButtonLabel;

	// Transition
	private ITransition fadeInTransition;
	private ITransition fadeOutTransition;

	private Sound selectSound;

	@Override
	public void loadResources(AssetManager assetManager)
	{
		assetManager.load("menu/menu_background.png", Texture.class);
		assetManager.load("menu/button_1.png", Texture.class);
		assetManager.load("menu/button_2.png", Texture.class);
		assetManager.load("menu/button_3.png", Texture.class);
		assetManager.load("menu/button_4.png", Texture.class);

		assetManager.load("sound/select.wav", Sound.class);
	}

	@Override
	public void initialize()
	{
		super.initialize();
		nextState = new Game();

		backgroundTexture = Main.get().assetManager.get("menu/menu_background.png");
		selectSound = Main.get().assetManager.get("sound/select.wav");

		playButton = new Button((Texture) Main.get().assetManager.get("menu/button_3.png"));
		playButton.setOriginCenter();
		playButton.setScale(3f);
		playButton.setOriginBasedPosition(Main.get().Width * -.125f, Main.get().Height * .1f);
		playButton.updateBoundingBox();

		playButtonLabel = new Label("PLAY", new Label.LabelStyle(Main.get().font, Color.WHITE));
		playButtonLabel.setAlignment(Align.center);
		playButtonLabel.setFontScale(0.8f);
		playButtonLabel.setPosition(Main.get().Width * -.125f, Main.get().Height * .1f, Align.center);
		playButton.setActive(false);

		exitButton = new Button((Texture) Main.get().assetManager.get("menu/button_1.png"));
		exitButton.setOriginCenter();
		exitButton.setScale(3f);
		exitButton.setOriginBasedPosition(Main.get().Width * -.125f, Main.get().Height * -.1f);
		exitButton.updateBoundingBox();
		exitButton.setActive(false);

		exitButtonLabel = new Label("EXIT", new Label.LabelStyle(Main.get().font, Color.WHITE));
		exitButtonLabel.setAlignment(Align.center);
		exitButtonLabel.setFontScale(0.8f);
		exitButtonLabel.setPosition(Main.get().Width * -.125f, Main.get().Height * -.1f, Align.center);

		initializeTransitions();

		playButton.setClickListener(() -> TransitionHandler.get().setTransition(fadeOutTransition));
		exitButton.setClickListener(() -> Gdx.app.exit());

		TransitionHandler.get().setTransition(fadeInTransition);
	}

	@Override
	public void update(float deltaTime)
	{

	}

	@Override
	public void render(SpriteBatch spriteBatch)
	{

	}

	@Override
	public void renderUI(SpriteBatch spriteBatch)
	{
		spriteBatch.begin();
		spriteBatch.draw(backgroundTexture, -Main.get().Width * .5f, -Main.get().Height * .5f, Main.get().Width, Main.get().Height);
		playButton.draw(spriteBatch);
		exitButton.draw(spriteBatch);
		playButtonLabel.draw(spriteBatch, 1);
		exitButtonLabel.draw(spriteBatch, 1);
		spriteBatch.end();
	}

	@Override
	public void dispose(AssetManager assetManager)
	{

	}

	// Transitions
	private void initializeTransitions()
	{
		fadeOutTransition = new ITransition()
		{
			float translationValue = 0;
			float playButtonX = playButton.getX();
			float exitButtonX = exitButton.getX();
			float playButtonLabelX = playButtonLabel.getX();
			float exitButtonLabelX = exitButtonLabel.getX();

			@Override
			public void transitionEnter()
			{
				playButton.setActive(false);
				exitButton.setActive(false);

				selectSound.play(0.125f);
			}

			@Override
			public boolean update(float deltaTime, float transitionTime)
			{
				translationValue = (float) (Math.sqrt(Math.abs(Math.sin(transitionTime * Math.PI / 2))));

				playButton.setPosition(playButtonX - translationValue * Main.get().Height, playButton.getY());
				playButtonLabel.setPosition(playButtonLabelX - translationValue * Main.get().Height, playButtonLabel.getY());
				exitButton.setPosition(exitButtonX - translationValue * Main.get().Height, exitButton.getY());
				exitButtonLabel.setPosition(exitButtonLabelX - translationValue * Main.get().Height, exitButtonLabel.getY());

				Menu.this.update(deltaTime);

				return transitionTime >= 1;
			}

			@Override
			public void render(SpriteBatch spriteBatch)
			{
				spriteBatch.setColor(1, 1, 1, Math.min(1, 1 - translationValue));
				Menu.this.render(spriteBatch);
				spriteBatch.setColor(Color.WHITE);
			}

			@Override
			public void renderUI(SpriteBatch spriteBatch)
			{
				spriteBatch.setColor(1, 1, 1, Math.min(1, 1 - translationValue));
				Menu.this.renderUI(spriteBatch);
				spriteBatch.setColor(Color.WHITE);
			}

			@Override
			public void transitionExit()
			{
				disposable = true;
			}
		};

		fadeInTransition = new ITransition()
		{
			float translationValue = 0;
			float playButtonX = playButton.getX();
			float exitButtonX = exitButton.getX();
			float playButtonLabelX = playButtonLabel.getX();
			float exitButtonLabelX = exitButtonLabel.getX();

			@Override
			public void transitionEnter() { }

			@Override
			public boolean update(float deltaTime, float transitionTime)
			{
				translationValue = (float) (1 - Math.sqrt(Math.sin(transitionTime * Math.PI / 2)));

				playButton.setPosition(playButtonX - translationValue * Main.get().Height, playButton.getY());
				playButtonLabel.setPosition(playButtonLabelX - translationValue * Main.get().Height, playButtonLabel.getY());
				exitButton.setPosition(exitButtonX - translationValue * Main.get().Height, exitButton.getY());
				exitButtonLabel.setPosition(exitButtonLabelX - translationValue * Main.get().Height, exitButtonLabel.getY());

				Menu.this.update(deltaTime);

				return transitionTime >= 1;
			}

			@Override
			public void render(SpriteBatch spriteBatch)
			{
				spriteBatch.setColor(1, 1, 1, Math.min(1, 1 - translationValue));
				Menu.this.render(spriteBatch);
				spriteBatch.setColor(Color.WHITE);
			}

			@Override
			public void renderUI(SpriteBatch spriteBatch)
			{
				spriteBatch.setColor(1, 1, 1, Math.min(1, 1 - translationValue));
				Menu.this.renderUI(spriteBatch);
				spriteBatch.setColor(Color.WHITE);
			}

			@Override
			public void transitionExit()
			{
				playButton.setPosition(playButtonX, playButton.getY());
				playButtonLabel.setPosition(playButtonLabelX, playButtonLabel.getY());
				exitButton.setPosition(exitButtonX, exitButton.getY());
				exitButtonLabel.setPosition(exitButtonLabelX, exitButtonLabel.getY());

				playButton.setActive(true);
				exitButton.setActive(true);
			}
		};
	}
}
