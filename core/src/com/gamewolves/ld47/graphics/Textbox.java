package com.gamewolves.ld47.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.gamewolves.ld47.Main;

public class Textbox
{
	private Array<String> lines;
	private NinePatch ninePatch;
	private Vector2 position;
	private Label label;
	private Vector2 dimensions;

	public Textbox(NinePatch ninePatch, Vector2 position)
	{
		this.ninePatch = ninePatch;
		this.position = position;

		label = new Label("", new Label.LabelStyle(Main.get().font, Color.WHITE));
		label.setFontScale(2f);
		label.setWidth(Main.get().Width * .8f);
		label.setAlignment(Align.top);
		label.setOrigin(Align.center);

		lines = new Array<>();
	}

	public void setText(String text)
	{
		lines.clear();
		lines.addAll(text.split("\n"));

		calculateDimensions();
	}

	private void calculateDimensions()
	{
		float width = 0;
		float height = 0;

		for (String line : lines)
		{
			label.setText(line);
			label.layout();

			if (label.getPrefWidth() > width)
			{
				width = label.getPrefWidth();
				if (width > Main.get().Width * .8f)
					width = Main.get().Width * .8f;
			}

			if (label.getPrefWidth() > Main.get().Width * .8f)
			{
				label.setWrap(true);
				label.layout();
			}

			height += label.getPrefHeight() + 10;
		}

		// Border margins
		width += 40;
		height += 40;

		dimensions = new Vector2(width, height);
	}

	public void render(SpriteBatch batch)
	{
		ninePatch.draw(
				batch,
				position.x - dimensions.x  * .5f,
				position.y - dimensions.y  * .5f,
				dimensions.x,
				dimensions.y
		);

		float height = 0;

		for (String line : lines)
		{
			label.setText(line);
			label.layout();

			if (label.getPrefWidth() > Main.get().Width * .8f)
			{
				label.setWrap(true);
				label.layout();
			}

			label.setPosition(position.x - Main.get().Width * .4f, position.y + dimensions.y * .5f - 30 - height);
			label.layout();

			label.draw(batch, 1);
			height += label.getPrefHeight() + 10;
		}
	}

	public void setFontSize(float size)
	{
		label.setFontScale(size);
		calculateDimensions();
	}

	public void clear()
	{
		lines.clear();
	}

	public boolean hasText()
	{
		return lines.notEmpty();
	}
}
