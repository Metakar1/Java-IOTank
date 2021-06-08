package org.gyming.tank.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

public class CharacterSelectionScreen extends ScreenAdapter {
    private final TankGame game;
    private final Stage stage;
    private final TextButton startButton, nextButton, prevButton;
    public int selectedType;

    CharacterSelectionScreen(final TankGame game) {
        this.game = game;
        this.stage = new Stage();
        startButton = new TextButton("Start!", game.skin, "textbutton-tank-red");
        startButton.setPosition(Gdx.graphics.getWidth() / 2f - startButton.getWidth() / 2f, 100f);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playerType = selectedType;
//                game.setScreen(game.gameOverScreen);
                game.buildConnection();
                game.setScreen(game.mainScreen);
            }
        });

        nextButton = new TextButton(">>", game.skin, "textbutton-tank-blue");
        nextButton.setPosition(Gdx.graphics.getWidth() - 200f - nextButton.getWidth(), Gdx.graphics.getHeight() / 2f);
        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedType = (selectedType + 1) % 3;
                System.out.println(selectedType);
                stage.clear();
                uiUpdate();
            }
        });

        prevButton = new TextButton("<<", game.skin, "textbutton-tank-blue");
        prevButton.setPosition(200f, Gdx.graphics.getHeight() / 2f);
        prevButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedType = (selectedType - 1 + 3) % 3;
                System.out.println(selectedType);
                stage.clear();
                uiUpdate();
            }
        });
    }

    private void uiUpdate() {
        Image characterImage = game.characterImage.get(selectedType);
        characterImage.setPosition(Gdx.graphics.getWidth() / 2f - 215f, Gdx.graphics.getHeight() / 2f - 215f);
        stage.addActor(characterImage);
        stage.addActor(nextButton);
        stage.addActor(prevButton);
        stage.addActor(startButton);
    }

    @Override
    public void show() {
        selectedType = 0;
        Gdx.input.setInputProcessor(stage);
        uiUpdate();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(game.skin.getColor("gray"));
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {

    }
}
