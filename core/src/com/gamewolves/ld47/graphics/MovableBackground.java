package com.gamewolves.ld47.graphics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gamewolves.ld47.Main;
import com.gamewolves.ld47.assetloaders.types.Text;

public class MovableBackground
{
	private float speed = 0;

	private String backdropPath, overlayPath, dataPath;
	private Texture backdrop, overlay;
	private TextureRegion[] overlayFrames;
	private Vector2[] positions;
	private int frameWidth, frameHeight;
	private int backgroundCount;
	private float height = 0;

	public void loadResources(String backdropPath, String overlayPath, String dataPath, AssetManager assetManager)
	{
		this.backdropPath = backdropPath;
		this.overlayPath = overlayPath;
		this.dataPath = dataPath;

		if (!assetManager.contains(backdropPath))
			assetManager.load(backdropPath, Texture.class);

		if (!assetManager.contains(overlayPath))
			assetManager.load(overlayPath, Texture.class);

		if (!assetManager.contains(dataPath))
			assetManager.load(dataPath, Text.class);
	}

	private void parseData(Text data)
	{
		String[] lines = data.getString().split("\n");

		frameWidth = Integer.parseInt(lines[0].split(",")[0]);
		frameHeight = Integer.parseInt(lines[0].split(",")[1]);

		positions = new Vector2[lines.length - 1];
		for (int i = 0; i < positions.length; i++)
		{
			positions[i] = new Vector2(
					Integer.parseInt(lines[i + 1].split(",")[0]),
					Integer.parseInt(lines[i + 1].split(",")[1])
			);
		}

		overlayFrames = new TextureRegion[overlay.getWidth() / frameWidth];
		for (int i = 0; i < overlayFrames.length; i++)
			overlayFrames[i] = new TextureRegion(overlay, frameWidth * i, 0, frameWidth, frameHeight);

		backgroundCount = (int) (Math.ceil(Main.get().Height / backdrop.getHeight()) + 1);
	}

	public void initialize()
	{
		backdrop = Main.get().assetManager.get(backdropPath);
		overlay = Main.get().assetManager.get(overlayPath);

		parseData((Text)Main.get().assetManager.get(dataPath));
	}

	public void update(float deltaTime)
	{
		height -= deltaTime * speed;

		if (height <= -backdrop.getHeight())
			height += backdrop.getHeight();
	}

	public void render(SpriteBatch batch)
	{
		int frame = (int) Math.floor(Main.get().elapsedTime % 1 * overlayFrames.length);

		batch.begin();
		for (int i = 0; i < backgroundCount; i++)
		{
			float yOff = height + i * backdrop.getHeight();
			batch.draw(backdrop, 0, yOff);

			for (Vector2 pos : positions)
			{
				float x = pos.x - frameWidth / 2.f;
				float y = yOff + pos.y - frameHeight / 2.f;

				batch.draw(overlayFrames[frame], x, y);
			}
		}
		batch.end();
	}

	public void dispose(AssetManager assetManager)
	{
		assetManager.unload(backdropPath);
		assetManager.unload(overlayPath);
		assetManager.unload(dataPath);
	}

	public void setSpeed(float speed)
	{
		this.speed = speed;
	}

	public void translate(float y)
	{
		height += y;

		if (height <= -backdrop.getHeight())
			height += backdrop.getHeight();
	}
}
