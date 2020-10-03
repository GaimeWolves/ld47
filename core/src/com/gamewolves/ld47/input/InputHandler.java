package com.gamewolves.ld47.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.gamewolves.ld47.Main;

public class InputHandler
{
	private static final InputHandler Instance = new InputHandler();
	private Array<IInputListener> inputListeners = new Array<>();
	private boolean dragging = false;
	private Vector2 start, current;

	private InputHandler() { }

	public static InputHandler get()
	{
		return Instance;
	}

	public void addInputListener(IInputListener listener)
	{
		inputListeners.add(listener);
	}

	public void removeInputListener(IInputListener listener)
	{
		inputListeners.removeValue(listener, true);
	}

	public void update()
	{
		if (inputListeners.isEmpty())
			return;

		// Click event
		if (Gdx.input.justTouched())
		{
			Vector2 worldPosition = toWorldCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

			for (IInputListener listener : inputListeners)
				listener.OnClick(worldPosition);
		}

		// Drag or hold events
		if (Gdx.input.isTouched())
		{
			Vector2 worldPosition = toWorldCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

			if (!dragging)
			{
				dragging = true;
				start = worldPosition;
				current = worldPosition;
			}

			Vector2 difference = worldPosition.cpy().sub(current);
			current = worldPosition;

			for (IInputListener listener : inputListeners)
			{
				listener.OnHold(worldPosition);
				listener.OnDrag(start, current, difference);
			}
		}
		else // DragEnd or release events
		{
			if (dragging)
			{
				for (IInputListener listener : inputListeners)
				{
					listener.OnRelease(current);
					listener.OnDragEnd(start, current);
				}

				dragging = false;
			}
		}
	}

	private Vector2 toWorldCoordinates(Vector2 screenCoordinates)
	{
		return new Vector2(
				Main.get().camera.unproject(new Vector3(screenCoordinates.x, screenCoordinates.y, 0)).x,
				Main.get().camera.unproject(new Vector3(screenCoordinates.x, screenCoordinates.y, 0)).y
		);
	}

	public Vector2 getMousePositionInWorld()
	{
		return toWorldCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
	}
}
