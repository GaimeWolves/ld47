package com.gamewolves.ld47.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GamePreferences
{
	private static Preferences preferences;

	public static void init()
	{
		preferences = Gdx.app.getPreferences("gamewolves_ld47");

		load();
		save();
	}

	private static void save()
	{

		preferences.flush();
		load();
	}

	private static void load()
	{

	}
}
