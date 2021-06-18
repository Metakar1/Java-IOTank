package org.gyming.tank.client;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

public class StartScreen extends ScreenAdapter {
    private final TankGame game;
    private final TextField serverAddressField, portField, userField, roomField;
    private final Dialog messageDialog;
    private final Stage startStage;
    private Music bgm;

    public StartScreen(final TankGame game) {
        this.game = game;
        TextButton confirmButton;

        // 移动端适配
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            serverAddressField = new TextField("", game.skin, "textfield-tank-android");
            portField = new TextField("", game.skin, "textfield-tank-android");
            userField = new TextField("", game.skin, "textfield-tank-android");
            roomField = new TextField("", game.skin, "textfield-tank-android");
            confirmButton = new TextButton("OK", game.skin, "textbutton-tank-red-android");
        }
        else {
            serverAddressField = new TextField("", game.skin, "textfield-tank");
            portField = new TextField("", game.skin, "textfield-tank");
            userField = new TextField("", game.skin, "textfield-tank");
            roomField = new TextField("", game.skin, "textfield-tank");
            confirmButton = new TextButton("OK", game.skin, "textbutton-tank-red");
        }

        // 设置提示语
        serverAddressField.setMessageText("Input the server address.");
        portField.setMessageText("Input the server port.");
        userField.setMessageText("Input your username.");
        roomField.setMessageText("Input the room name.");

        // 有空未填写时弹出提示框
        messageDialog = new Dialog("Error", game.skin, "window-tank");
        messageDialog.text("Username and room name cannot be empty.", game.skin.get("label-tank", Label.LabelStyle.class));
        messageDialog.button("OK", true, game.skin.get("textbutton-tank-gray",
                TextButton.TextButtonStyle.class)).addListener(new ClickListener() {
        });
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
                game.setScreen(game.characterSelectionScreen);
            }
        });

        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            serverAddressField.setSize(800, 100);
            portField.setSize(800, 100);
            userField.setSize(800, 100);
            roomField.setSize(800, 100);
            confirmButton.setSize(200, 100);
            serverAddressField.setPosition((Gdx.graphics.getWidth() - serverAddressField.getWidth()) / 2f, Gdx.graphics.getHeight() / 2f + 300f);
            portField.setPosition((Gdx.graphics.getWidth() - portField.getWidth()) / 2f, Gdx.graphics.getHeight() / 2f + 150f);
            userField.setPosition((Gdx.graphics.getWidth() - userField.getWidth()) / 2f, Gdx.graphics.getHeight() / 2f);
            roomField.setPosition((Gdx.graphics.getWidth() - roomField.getWidth()) / 2f, Gdx.graphics.getHeight() / 2f - 150f);
            confirmButton.setPosition((Gdx.graphics.getWidth() - confirmButton.getWidth()) / 2f, Gdx.graphics.getHeight() / 2f - 300f);
        }
        else {
            serverAddressField.setSize(300, serverAddressField.getPrefHeight());
            portField.setSize(300, portField.getPrefHeight());
            userField.setSize(300, userField.getPrefHeight());
            roomField.setSize(300, roomField.getPrefHeight());
            serverAddressField.setPosition((Gdx.graphics.getWidth() - serverAddressField.getWidth()) / 2f, Gdx.graphics.getHeight() / 2f + 100f);
            portField.setPosition((Gdx.graphics.getWidth() - portField.getWidth()) / 2f, Gdx.graphics.getHeight() / 2f + 50f);
            userField.setPosition((Gdx.graphics.getWidth() - userField.getWidth()) / 2f, Gdx.graphics.getHeight() / 2f);
            roomField.setPosition((Gdx.graphics.getWidth() - roomField.getWidth()) / 2f, Gdx.graphics.getHeight() / 2f - 50f);
            confirmButton.setPosition((Gdx.graphics.getWidth() - confirmButton.getWidth()) / 2f, Gdx.graphics.getHeight() / 2f - 100f);
        }

        startStage = new Stage();
        startStage.addActor(serverAddressField);
        startStage.addActor(portField);
        startStage.addActor(userField);
        startStage.addActor(roomField);
        startStage.addActor(confirmButton);

        // 添加背景音乐
        bgm = Gdx.audio.newMusic(Gdx.files.internal("start_bgm.mp3"));
        bgm.setLooping(true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(startStage);
        userField.setText("");
        roomField.setText("");
        serverAddressField.setText("127.0.0.1");
        portField.setText("7650");
        bgm.play();
    }

    @Override
    public void hide() {
        bgm.stop();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(game.skin.getColor("gray"));
        startStage.act();
        startStage.draw();
    }

    @Override
    public void dispose() {
        startStage.dispose();
        bgm.dispose();
    }
}
