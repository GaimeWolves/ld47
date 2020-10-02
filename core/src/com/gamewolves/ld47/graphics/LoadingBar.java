package com.gamewolves.ld47.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

public class LoadingBar
{
	private float value = 1;
	private int align = Align.left;
	private int originalWidth;
	private Sprite sprite;

	public LoadingBar(Sprite sprite)
	{
		this.sprite = sprite;
		originalWidth = sprite.getRegionWidth();
	}

	public LoadingBar(Texture texture)
	{
		this(new Sprite(texture));
	}

	public LoadingBar(TextureRegion texture)
	{
		this(new Sprite(texture));
	}

	public void setValue(float value)
	{
		this.value = value;

		sprite.setRegionWidth((int)(originalWidth * value));

		switch (align)
		{
			case Align.left:
				sprite.setOrigin(0, sprite.getRegionHeight() * .5f);
				break;
			case Align.center:
				sprite.setOrigin(sprite.getRegionWidth() * .5f, sprite.getRegionHeight() * .5f);
				break;
			case Align.right:
				sprite.setOrigin(sprite.getRegionWidth(), sprite.getRegionHeight() * .5f);
				break;
		}
	}

	public void setAlignment(int align)
	{
		this.align = align;
		setValue(value);
	}

	public void translate(float x, float y)
	{
		sprite.translate(x, y);
	}

	public void translate(Vector2 translation)
	{
		translate(translation.x, translation.y);
	}

	public void setPosition(float x, float y)
	{
		sprite.setOriginBasedPosition(x, y);
	}

	public void setPosition(Vector2 position)
	{
		setPosition(position.x, position.y);
	}
}
