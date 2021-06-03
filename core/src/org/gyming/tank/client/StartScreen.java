package org.gyming.tank.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

public class StartScreen extends ScreenAdapter {
    private final TankGame game;
    private final BitmapFont font;
    private final Skin skin;
    private final TextField userField, roomField;
    private final Dialog messageDialog;
    private final Stage startStage;

    public StartScreen(final TankGame game) {
        this.game = game;

        font = new BitmapFont();
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        userField = new TextField("", skin);
        roomField = new TextField("", skin);
        userField.setMessageText("Input your username.");
        roomField.setMessageText("Input the room name.");
        TextButton confirmButton = new TextButton("OK", skin);
        messageDialog = new Dialog("Error", skin);
        messageDialog.text("Username and room name cannot be empty.");
        messageDialog.button("OK", true).addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
           }
        });
        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (userField.getText().equals("") || roomField.getText().equals("")) {
                    messageDialog.show(startStage);
                    return;
                }
                game.setUserName(userField.getText());
                game.setRoomName(roomField.getText());
                game.buildConnection();
                game.PlayerId = game.getUserName().hashCode();
//                System.out.println(game.getRoomName());
                game.setScreen(game.mainScreen);
                System.out.println(1);
            }
        });

        userField.setSize(300, userField.getPrefHeight());
        roomField.setSize(300, roomField.getPrefHeight());
        userField.setPosition((Gdx.graphics.getWidth() - userField.getWidth()) / 2, Gdx.graphics.getHeight() / 2 + 50);
        roomField.setPosition((Gdx.graphics.getWidth() - roomField.getWidth()) / 2, Gdx.graphics.getHeight() / 2);
        confirmButton.setPosition((Gdx.graphics.getWidth() - confirmButton.getWidth()) / 2, Gdx.graphics.getHeight() / 2 - 50);

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
        ScreenUtils.clear(skin.getColor("gray"));
        startStage.act();
        startStage.draw();
    }

    @Override
    public void dispose() {
        font.dispose();
        skin.dispose();
        startStage.dispose();
    }
}
