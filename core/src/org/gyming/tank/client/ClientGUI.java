package org.gyming.tank.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class ClientGUI extends Game {
    private StartScreen startScreen;
    private MainScreen mainScreen;
    private GameOverScreen gameOverScreen;

    @Override
    public void create() {
        Gdx.graphics.setContinuousRendering(false);
        startScreen = new StartScreen(this);
        mainScreen = new MainScreen(this);
        gameOverScreen = new GameOverScreen(this);
    }
}
