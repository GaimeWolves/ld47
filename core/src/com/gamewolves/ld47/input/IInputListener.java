package com.gamewolves.ld47.input;

import com.badlogic.gdx.math.Vector2;

public interface IInputListener
{
	void OnClick(Vector2 position);

	void OnHold(Vector2 position);

	void OnRelease(Vector2 position);

	void OnDrag(Vector2 start, Vector2 current, Vector2 delta);

	void OnDragEnd(Vector2 start, Vector2 end);
}
