package com.gamewolves.ld47.utils;

import com.badlogic.gdx.graphics.Color;

public class MathUtils
{
	public static int HSLtoRGB(float h, float s, float l)
	{
		float r, g, b;

		h %= 1;

		float region = (h * 360) / 60f;

		float c = (1 - Math.abs(2 * l - 1)) * s;
		float x = c * (1 - Math.abs(region % 2 - 1));

		switch ((int)Math.floor(region))
		{
			case 0:
				r = c; g = x; b = 0;
				break;
			case 1:
				r = x; g = c; b = 0;
				break;
			case 2:
				r = 0; g = c; b = x;
				break;
			case 3:
				r = 0; g = x; b = c;
				break;
			case 4:
				r = x; g = 0; b = c;
				break;
			case 5:
				r = c; g = 0; b = x;
				break;
			default:
				r = 0; g = 0; b = 0;
				break;
		}

		float m = l - c / 2f;

		r += m;
		g += m;
		b += m;

		return Color.argb8888(1, r, g, b);
	}
}
