package org.gyming.tank.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

public class CharacterSelectionScreen extends ScreenAdapter {
    private final TankGame game;
    private final Stage stage;
    private final Label startLabel, nextLabel, prevLabel;
    public int selectedType;

    CharacterSelectionScreen(final TankGame game) {
        this.game = game;
        this.stage = new Stage();
        startLabel = new Label("Start!", game.skin, "label-chara-tank");
        startLabel.setPosition(Gdx.graphics.getWidth() - startLabel.getWidth() - 10f, 20f);
        startLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playerType = selectedType;
//                game.setScreen(game.gameOverScreen);
                game.buildConnection();
                game.setScreen(game.mainScreen);
            }
        });

        nextLabel = new Label(">", game.skin, "label-chara-tank");
        nextLabel.setPosition(Gdx.graphics.getWidth() - 20f - nextLabel.getWidth(), Gdx.graphics.getHeight() / 2f);
        nextLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedType = (selectedType + 1) % 3;
                System.out.println(selectedType);
                stage.clear();
                uiUpdate();
            }
        });

        prevLabel = new Label("<", game.skin, "label-chara-tank");
        prevLabel.setPosition(20f, Gdx.graphics.getHeight() / 2f);
        prevLabel.addListener(new ClickListener() {
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
        stage.addActor(characterImage);
        stage.addActor(nextLabel);
        stage.addActor(prevLabel);
        stage.addActor(startLabel);
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
