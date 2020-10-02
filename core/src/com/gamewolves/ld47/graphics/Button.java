package com.gamewolves.ld47.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gamewolves.ld47.input.IClickable;
import com.gamewolves.ld47.input.UIInputHandler;

public class Button extends Sprite implements IClickable
{
	private Runnable clickEventPassthrough = null;
	private boolean isActive = true;
	private float oldScale = 0;

	public Button()
	{
		super();
		UIInputHandler.get().addClickable(this, getBoundingRectangle());
	}

	public Button(Texture texture)
	{
		super(texture);
		UIInputHandler.get().addClickable(this, getBoundingRectangle());
	}

	public Button(Texture texture, int srcWidth, int srcHeight)
	{
		super(texture, srcWidth, srcHeight);
		UIInputHandler.get().addClickable(this, getBoundingRectangle());
	}

	public Button(Texture texture, int srcX, int srcY, int srcWidth, int srcHeight)
	{
		super(texture, srcX, srcY, srcWidth, srcHeight);
		UIInputHandler.get().addClickable(this, getBoundingRectangle());
	}

	public Button(TextureRegion region)
	{
		super(region);
		UIInputHandler.get().addClickable(this, getBoundingRectangle());
	}

	public Button(TextureRegion region, int srcX, int srcY, int srcWidth, int srcHeight)
	{
		super(region, srcX, srcY, srcWidth, srcHeight);
		UIInputHandler.get().addClickable(this, getBoundingRectangle());
	}

	public Button(Sprite sprite)
	{
		super(sprite);
		UIInputHandler.get().addClickable(this, getBoundingRectangle());
	}

	public void setClickListener(Runnable listener)
	{
		clickEventPassthrough = listener;
	}

	public void updateBoundingBox()
	{
		UIInputHandler.get().updateBoundingBox(this, getBoundingRectangle());
	}

	public void setActive(boolean isActive)
	{
		this.isActive = isActive;
	}

	@Override
	public void OnClick()
	{
		if (clickEventPassthrough != null && isActive)
			clickEventPassthrough.run();
	}

	@Override
	public void OnHoverEnter()
	{
		oldScale = getScaleX();
		if (isActive)
			setScale(oldScale * .9f);
	}

	@Override
	public void OnHover()
	{

	}

	@Override
	public void OnHoverExit()
	{
		setScale(oldScale);
	}

	public void dispose()
	{
		UIInputHandler.get().removeClickable(this);
	}
}
