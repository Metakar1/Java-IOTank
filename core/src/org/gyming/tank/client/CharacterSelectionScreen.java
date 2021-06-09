package org.gyming.tank.client;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class CharacterSelectionScreen extends ScreenAdapter {
    private final TankGame game;
    private final Stage stage;
    private final Label startLabel, nextLabel, prevLabel;
    private final Array<Image> characterImages;
    private final Array<Sound> characterSounds;
    private final Array<Label> characterSkills;
    private final Sound applySound;
    private final Dialog messageDialog;
    private Sound lastCharacterSound;
    public int selectedType;

    CharacterSelectionScreen(final TankGame game) {
        this.game = game;
        this.stage = new Stage();
        messageDialog = new Dialog("Oooooooops", game.skin, "window-tank");
        messageDialog.text("It seems that we cannot find your server.", game.skin.get("label-tank", Label.LabelStyle.class));
        messageDialog.button("OK", true, game.skin.get("textbutton-tank-gray",
                TextButton.TextButtonStyle.class)).addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.startScreen);
            }
        });
        startLabel = new Label("Start!", game.skin, "label-chara-tank");
        startLabel.setPosition(Gdx.graphics.getWidth() - startLabel.getWidth() - 10f, 20f);
        startLabel.getColor().a = 0.8f;
        startLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playerType = selectedType;
                if (lastCharacterSound != null)
                    lastCharacterSound.stop();
//                game.setScreen(game.gameOverScreen);
                if (game.buildConnection()) {
                    applySound.play();
                    game.setScreen(game.mainScreen);
                }
                else {
                    messageDialog.show(stage);
                    System.out.println("CONNECTION ERROR.");
//                    game.setScreen(game.startScreen);
                }
            }
        });

        nextLabel = new Label(">", game.skin, "label-chara-tank");
        nextLabel.setPosition(Gdx.graphics.getWidth() - 20f - nextLabel.getWidth(), Gdx.graphics.getHeight() / 2f);
        nextLabel.getColor().a = 0.3f;
        nextLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedType = (selectedType + 1) % 3;
                System.out.println(selectedType);
                uiUpdate();
            }
        });

        prevLabel = new Label("<", game.skin, "label-chara-tank");
        prevLabel.setPosition(20f, Gdx.graphics.getHeight() / 2f);
        prevLabel.getColor().a = 0.3f;
        prevLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedType = (selectedType - 1 + 3) % 3;
                System.out.println(selectedType);
                uiUpdate();
            }
        });

        characterImages = new Array<>();
        Image characterImage = new Image(new Texture(Gdx.files.internal("0.jpg")));
        float scale = Math.max(Gdx.graphics.getHeight() / characterImage.getHeight(), Gdx.graphics.getWidth() / characterImage.getWidth());
        characterImage.setScale(scale, scale);
        characterImages.add(characterImage);
        characterImage = new Image(new Texture(Gdx.files.internal("1.jpg")));
        scale = Math.max(Gdx.graphics.getHeight() / characterImage.getHeight(), Gdx.graphics.getWidth() / characterImage.getWidth());
        characterImage.setScale(scale, scale);
        characterImages.add(characterImage);
        characterImage = new Image(new Texture(Gdx.files.internal("2.jpg")));
        scale = Math.max(Gdx.graphics.getHeight() / characterImage.getHeight(), Gdx.graphics.getWidth() / characterImage.getWidth());
        characterImage.setScale(scale, scale);
        characterImages.add(characterImage);

        characterSounds = new Array<>();
        characterSounds.add(Gdx.audio.newSound(Gdx.files.internal("0_select.mp3")));
        characterSounds.add(Gdx.audio.newSound(Gdx.files.internal("1_select.mp3")));
        characterSounds.add(Gdx.audio.newSound(Gdx.files.internal("2_select.mp3")));
        applySound = Gdx.audio.newSound(Gdx.files.internal("game_begin.mp3"));

        String skillLabelStyle;
        if (Gdx.app.getType() == Application.ApplicationType.Android)
            skillLabelStyle = "label-chara-skill-tank-android";
        else
            skillLabelStyle = "label-chara-skill-tank";
        characterSkills = new Array<>();
        Label characterSkill = new Label("Special skill: skill1.", game.skin, skillLabelStyle);
        characterSkill.setPosition((Gdx.graphics.getWidth() - characterSkill.getWidth()) / 2f, 100f);
        characterSkill.getColor().a = 0.8f;
        characterSkills.add(characterSkill);
        characterSkill = new Label("Special skill: skill2.", game.skin, skillLabelStyle);
        characterSkill.setPosition((Gdx.graphics.getWidth() - characterSkill.getWidth()) / 2f, 100f);
        characterSkill.getColor().a = 0.8f;
        characterSkills.add(characterSkill);
        characterSkill = new Label("Special skill: skill3.", game.skin, skillLabelStyle);
        characterSkill.setPosition((Gdx.graphics.getWidth() - characterSkill.getWidth()) / 2f, 100f);
        characterSkill.getColor().a = 0.8f;
        characterSkills.add(characterSkill);
    }

    private void uiUpdate() {
        stage.clear();
        stage.addActor(characterImages.get(selectedType));
        stage.addActor(game.characterFlags.get(selectedType));
        stage.addActor(characterSkills.get(selectedType));
        stage.addActor(nextLabel);
        stage.addActor(prevLabel);
        stage.addActor(startLabel);
        if (lastCharacterSound != null)
            lastCharacterSound.stop();
        lastCharacterSound = characterSounds.get(selectedType);
        lastCharacterSound.play();
    }

    @Override
    public void show() {
        selectedType = 0;
        lastCharacterSound = null;
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
