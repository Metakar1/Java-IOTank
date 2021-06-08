package org.gyming.tank.client;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ScreenUtils;

import java.io.IOException;

public class GameOverScreen extends ScreenAdapter {
    final private TankGame game;
    final private Stage gameOverStage;

    public GameOverScreen(TankGame game) {
        this.game = game;
        gameOverStage = new Stage();
    }

    @Override
    public void show() {
        try {
            game.C2S.closeConnection();
            game.S2C.closeConnection();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Label gameOverLabel = new Label("Game Over", game.skin, "label-title-tank");
        Label continueLabel = new Label("Click to continue...", game.skin, "label-info-tank");
        gameOverLabel.setPosition((Gdx.graphics.getWidth() - gameOverLabel.getWidth()) / 2f, Gdx.graphics.getHeight() / 2f + 100f);
        continueLabel.setPosition((Gdx.graphics.getWidth() - continueLabel.getWidth()) / 2f, Gdx.graphics.getHeight() / 2f);
        gameOverStage.addActor(gameOverLabel);
        gameOverStage.addActor(continueLabel);
        gameOverLabel.getColor().a = 0f;
        continueLabel.getColor().a = 0f;
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            gameOverLabel.addAction(Actions.alpha(1f, 5f));
            continueLabel.addAction(Actions.alpha(1f, 5f));
        }
        else {
            gameOverLabel.addAction(Actions.alpha(1f, 15f));
            continueLabel.addAction(Actions.alpha(1f, 15f));
            Image gameOverImage = new Image(new Texture(Gdx.files.internal("gameover.png")));
            gameOverImage.setZIndex(0);
            gameOverStage.addActor(gameOverImage);
        }
        Gdx.input.setInputProcessor(gameOverStage);
    }

    @Override
    public void render(float delta) {
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            ScreenUtils.clear(1, 1, 1, 1);
        }
        gameOverStage.act();
        gameOverStage.draw();
        if (Gdx.input.isTouched()) {
            game.setScreen(game.startScreen);
        }
    }

    @Override
    public void hide() {  gameOverStage.clear(); }

    @Override
    public void dispose() {
        gameOverStage.dispose();
    }
}
