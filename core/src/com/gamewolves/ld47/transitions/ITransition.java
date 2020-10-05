package com.gamewolves.ld47.transitions;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Interface representing a transition.
 * <p>
 * The methods are called in the following order:
 * <p>
 * transitionEnter()
 * update() <--------. when update returned false
 * render() >--------|
 * transitionExit()<-' when update returned true
 */
public interface ITransition
{
	void transitionEnter();

	boolean update(float deltaTime, float transitionTime);

	void render(SpriteBatch spriteBatch);
	void renderUI(SpriteBatch spriteBatch);

	void transitionExit();
}
