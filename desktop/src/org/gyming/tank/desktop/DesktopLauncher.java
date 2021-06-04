package org.gyming.tank.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.gyming.tank.client.TankGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.resizable = false;
        config.title = "Tank Game";
        config.width = 1500;
        config.height = 900;
        config.foregroundFPS = 0;
        config.vSyncEnabled = false;
        new LwjglApplication(new TankGame(), config);
    }
}
