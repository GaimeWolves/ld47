package com.gamewolves.ld47.states;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Menu extends State
{
	@Override
	public void loadResources(AssetManager assetManager)
	{

	}

	@Override
	public void initialize()
	{
		super.initialize();
		nextState = new Menu();
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
	public void dispose(AssetManager assetManager)
	{

	}
}
