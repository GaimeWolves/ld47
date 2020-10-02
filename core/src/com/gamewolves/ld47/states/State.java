package com.gamewolves.ld47.states;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.lang.reflect.InvocationTargetException;

/**
 * Represents a game state
 */
public abstract class State
{
	// Booleans signaling the main loop to dispose/start the state
	protected boolean disposable = false;
	protected boolean initialized = false;

	// The next state to be run
	protected State nextState = null;

	// Methods that are called by the main loop at appropriate times
	public abstract void loadResources(AssetManager assetManager);

	public void initialize()
	{
		initialized = true;
	}

	public abstract void update(float deltaTime);

	public abstract void render(SpriteBatch spriteBatch);

	public abstract void dispose(AssetManager assetManager);

	public boolean isDisposable()
	{
		return disposable;
	}

	public boolean isInitialized()
	{
		return initialized;
	}

	public State getNextState()
	{
		return nextState;
	}

	// Creates the next state from the class variable
	public State createNextState()
	{
		return nextState;
	}
}
