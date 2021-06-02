package org.gyming.tank.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class StartScreen implements Screen {
    TankGame game;
    BitmapFont font;
    Skin skin;
    TextField userField, roomField;
    TextButton confirmButton;
    Stage startStage;

    public StartScreen(TankGame game) {
        this.game = game;

        font = new BitmapFont();
        skin = new Skin();

        userField = new TextField("", skin);
        roomField = new TextField("", skin);
        userField.setMessageText("Please input your username.");
        roomField.setMessageText("Please input the room number you want to join.");
        confirmButton = new TextButton("OK", skin);
        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {

            }
        });

        startStage = new Stage();
        startStage.addActor(userField);
        startStage.addActor(roomField);
        startStage.addActor(confirmButton);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(startStage);
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        font.dispose();
        skin.dispose();
        startStage.dispose();
    }
}
