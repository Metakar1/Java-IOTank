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
    private final BitmapFont font;
    private final Skin skin;
    private final TextField serverAddressField, portField, userField, roomField;
    private final Dialog messageDialog;
    private final Stage startStage;

    public StartScreen(final TankGame game) {

        font = new BitmapFont();
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        serverAddressField = new TextField("10.44.0.188", skin);
        portField = new TextField("7650", skin);
        userField = new TextField("", skin);
        roomField = new TextField("", skin);
        serverAddressField.setMessageText("Input the server address.");
        portField.setMessageText("Input the server port.");
        userField.setMessageText("Input your username.");
        roomField.setMessageText("Input the room name.");
        TextButton confirmButton = new TextButton("OK", skin);
        messageDialog = new Dialog("Error", skin);
        messageDialog.text("Username and room name cannot be empty.");
        messageDialog.button("OK", true).addListener(new ClickListener() {});
        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (userField.getText().equals("") || roomField.getText().equals("") ||
                        serverAddressField.getText().equals("") || portField.getText().equals("")) {
                    messageDialog.show(startStage);
                    return;
                }
                game.setServerAddress(serverAddressField.getText());
                game.setPort(Integer.parseInt(portField.getText()));
                game.setUserName(userField.getText());
                game.setRoomName(roomField.getText());
                game.buildConnection();
                game.setScreen(game.mainScreen);
            }
        });

        serverAddressField.setSize(300, serverAddressField.getPrefHeight());
        portField.setSize(300, portField.getPrefHeight());
        userField.setSize(300, userField.getPrefHeight());
        roomField.setSize(300, roomField.getPrefHeight());
        serverAddressField.setPosition((Gdx.graphics.getWidth() - serverAddressField.getWidth()) / 2f, Gdx.graphics.getHeight() / 2f + 100f);
        portField.setPosition((Gdx.graphics.getWidth() - portField.getWidth()) / 2f, Gdx.graphics.getHeight() / 2f + 50f);
        userField.setPosition((Gdx.graphics.getWidth() - userField.getWidth()) / 2f, Gdx.graphics.getHeight() / 2f);
        roomField.setPosition((Gdx.graphics.getWidth() - roomField.getWidth()) / 2f, Gdx.graphics.getHeight() / 2f - 50f);
        confirmButton.setPosition((Gdx.graphics.getWidth() - confirmButton.getWidth()) / 2f, Gdx.graphics.getHeight() / 2f - 100f);

        startStage = new Stage();
        startStage.addActor(serverAddressField);
        startStage.addActor(portField);
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
