package com.gamewolves.ld47.input;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class UIInputHandler implements IInputListener
{
	private static final UIInputHandler Instance = new UIInputHandler();
	private Array<Clickable> clickables = new Array<>();
	private boolean dragging = false;
	private Vector2 start, current;

	private UIInputHandler()
	{
		InputHandler.get().addInputListener(this);
	}

	public static UIInputHandler get()
	{
		return Instance;
	}

	public void addClickable(IClickable listener, Rectangle boundingBox)
	{
		clickables.add(new Clickable(listener, boundingBox));
	}

	public void updateBoundingBox(IClickable listener, Rectangle boundingBox)
	{
		for (int i = 0; i < clickables.size; i++)
		{
			if (clickables.get(i).listener == listener)
			{
				clickables.get(i).boundingBox = boundingBox;
				break;
			}
		}
	}

	public void removeClickable(IClickable listener)
	{
		for (int i = 0; i < clickables.size; i++)
		{
			if (clickables.get(i).listener == listener)
			{
				clickables.removeIndex(i);
				return;
			}
		}
	}

	@Override
	public void OnClick(Vector2 position)
	{

	}

	@Override
	public void OnHold(Vector2 position)
	{
		for (Clickable clickable : clickables)
		{
			if (clickable.boundingBox == null)
				continue;

			if (clickable.boundingBox.contains(position))
			{
				if (!clickable.hovered)
				{
					clickable.listener.OnHoverEnter();
					clickable.hovered = true;
				}

				clickable.listener.OnHover();
			}
			else if (clickable.hovered)
			{
				clickable.hovered = false;
				clickable.listener.OnHoverExit();
			}
		}
	}

	@Override
	public void OnRelease(Vector2 position)
	{
		for (Clickable clickable : clickables)
		{
			if (clickable.boundingBox == null)
				continue;

			if (clickable.boundingBox.contains(position))
			{
				clickable.hovered = false;
				clickable.listener.OnHoverExit();
				clickable.listener.OnClick();
			}
		}
	}

	@Override
	public void OnDrag(Vector2 start, Vector2 current, Vector2 delta)
	{

	}

	@Override
	public void OnDragEnd(Vector2 start, Vector2 end)
	{

	}

	private static class Clickable
	{
		public IClickable listener;
		public Rectangle boundingBox;
		public boolean hovered = false;

		public Clickable(IClickable listener, Rectangle boundingBox)
		{
			this.listener = listener;
			this.boundingBox = boundingBox;
		}
	}
}
