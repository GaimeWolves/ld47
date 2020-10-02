package com.gamewolves.ld47.transitions;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TransitionHandler
{
	private static final TransitionHandler Instance = new TransitionHandler();
	private ITransition currentTransition = null;
	private boolean finished = false;
	private float elapsedTime = 0;

	private TransitionHandler()
	{
	}

	public static TransitionHandler get()
	{
		return Instance;
	}

	public void setTransition(ITransition transition)
	{
		currentTransition = transition;
		finished = false;
		elapsedTime = 0;

		currentTransition.transitionEnter();
	}

	public void update(float deltaTime)
	{
		if (currentTransition != null)
		{
			elapsedTime += deltaTime;

			if (currentTransition.update(deltaTime, elapsedTime))
				finished = true;
		}
	}

	public void render(SpriteBatch batch)
	{
		if (currentTransition != null)
		{
			currentTransition.render(batch);

			if (finished)
			{
				currentTransition.transitionExit();
				currentTransition = null;
			}
		}
	}

	public boolean inTransition()
	{
		return currentTransition != null;
	}
}
