package com.alchera.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.alchera.game.Alchera;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = Alchera.HEIGHT;
		config.width = Alchera.WIDTH;
		config.vSyncEnabled = true;
		new LwjglApplication(new Alchera(), config);
	}
}
