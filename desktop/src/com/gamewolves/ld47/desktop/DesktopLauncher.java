package com.gamewolves.ld47.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gamewolves.ld47.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.resizable = false;
		config.title = "kek";

		Main main = new Main();
		main.device = Main.Device.DESKTOP;

		new LwjglApplication(main, config);
	}
}
