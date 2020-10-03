package com.gamewolves.ld47.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class AnimatedSprite
{
	private Array<TextureRegion> textures;
	private float time;
	private float animTime;
	private Vector2 position = new Vector2();
	private float rotation;
	private boolean centered;
	private Vector2 scale = new Vector2(1, 1);

	public AnimatedSprite(Array<TextureRegion> textures, float animTime)
	{
		this.textures = textures;
		this.animTime = animTime;
	}

	public AnimatedSprite(Texture texture, int xSplit, int ySplit, float animTime)
	{
		this(new TextureRegion(texture), xSplit, ySplit, animTime);
	}

	public AnimatedSprite(TextureRegion region, int xSplit, int ySplit, float animTime)
	{
		textures = new Array<>();

		int width = xSplit > 0 ? xSplit : region.getRegionWidth();
		int height = ySplit > 0 ? ySplit : region.getRegionHeight();

		for (int x = 0; x < region.getRegionWidth(); x += width)
		{
			for (int y = 0; y < region.getRegionHeight(); y += height)
			{
				textures.add(
						new TextureRegion(
								region,
								x,
								y,
								width,
								height
						)
				);
			}
		}

		this.animTime = animTime;
	}

	public void update(float deltaTime)
	{
		time = (time + deltaTime) % animTime;
	}

	public void render(SpriteBatch batch) {
		TextureRegion region = textures.get((int) Math.floor(time / animTime * textures.size));

		float originX = centered ? region.getRegionWidth() * .5f : 0;
		float originY = centered ? region.getRegionHeight() * .5f : 0;

		batch.draw(
				region,
				position.x - originX,
				position.y - originY,
				originX,
				originY,
				region.getRegionWidth(),
				region.getRegionHeight(),
				scale.x,
				scale.y,
				rotation
		);
	}

	public void setPosition(Vector2 position)
	{
		this.position = position;
	}

	public void setPosition(float x, float y)
	{
		this.position = new Vector2(x, y);
	}

	public float getWidth()
	{
		return textures.get(0).getRegionWidth();
	}

	public float getHeight()
	{
		return textures.get(0).getRegionHeight();
	}

	public float getX()
	{
		return position.x;
	}

	public float getY()
	{
		return position.y;
	}

	public void setRotation(float rotation) { this.rotation = rotation; }

	public void setCentered(boolean centered) { this.centered = centered; }

	public float getTime() { return time; }

	public void setTime(float time) { this.time = time; }

	public void setScale(float x, float y) { scale = new Vector2(x, y); }
}
