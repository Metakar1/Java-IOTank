package org.gyming.tank.client;

import com.badlogic.gdx.Game;

public class TankGame extends Game {
    StartScreen startScreen;
    MainScreen mainScreen;
    GameOverScreen gameOverScreen;

    @Override
    public void create () {
        startScreen = new StartScreen(this);
        mainScreen = new MainScreen(this);
        gameOverScreen = new GameOverScreen(this);
        setScreen(startScreen);
    }

    @Override
    public void render () {
    }

    @Override
    public void dispose () {
    }
}
