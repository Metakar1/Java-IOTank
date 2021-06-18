package org.gyming.tank.client;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.google.gson.Gson;
import org.gyming.tank.connection.GameAction;
import org.gyming.tank.connection.GameFrame;
import org.gyming.tank.object.BulletObject;
import org.gyming.tank.object.GameObject;
import org.gyming.tank.object.PlayerObject;
import org.gyming.tank.object.SupplyObject;

import java.util.Random;

public class MainScreen extends ScreenAdapter {
    public static int boarder = 800;
    public static int supplies = 0;
    public static int width, height;
    public static Random dataMaker;
    public static PlayerObject MainPlayer;
    static int fireGap = 0;
    TankGame game;
    Group group[];
    Stage stage, uiStage, controllerStage;
    ProgressBar mpProgress;
    Touchpad moveTouchpad, fireTouchpad;
    ImageButton fireButton;
    Thread downloader, listener;
    private Array<Music> characterBgms;

    public MainScreen(TankGame game) {
        this.stage = new Stage();
        this.uiStage = new Stage();
        this.controllerStage = new Stage();
        this.game = game;
        mpProgress = new ProgressBar(0f, 10f, 1f, false, game.skin, "progressbar-tank");
        mpProgress.setWidth(300);
        mpProgress.setPosition((Gdx.graphics.getWidth() - mpProgress.getWidth()) / 2f, 50f);
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            moveTouchpad = new Touchpad(0, game.skin, "touchpad-tank");
//            moveTouchpad.setSize(400f, 400f);
            moveTouchpad.setPosition(100f, 60f);
            fireTouchpad = new Touchpad(0, game.skin, "touchpad-tank");
//            fireTouchpad.setSize(400f, 400f);
            fireTouchpad.setPosition(Gdx.graphics.getWidth() - 500f, 60f);
            fireButton = new ImageButton(game.skin, "firebutton-tank");
            fireButton.setPosition(Gdx.graphics.getWidth() - 300f, 500f);
            controllerStage.addActor(moveTouchpad);
            controllerStage.addActor(fireTouchpad);
            controllerStage.addActor(fireButton);
        }

        characterBgms = new Array<>();
        Music bgm = Gdx.audio.newMusic(Gdx.files.internal("0_bgm.mp3"));
        bgm.setLooping(true);
        characterBgms.add(bgm);
        bgm = Gdx.audio.newMusic(Gdx.files.internal("1_bgm.mp3"));
        bgm.setLooping(true);
        characterBgms.add(bgm);
        bgm = Gdx.audio.newMusic(Gdx.files.internal("2_bgm.mp3"));
        bgm.setLooping(true);
        characterBgms.add(bgm);
    }

    public static boolean isInside(GameObject A) {
        if (A.getPlayerID() == 0)
            return true;
//        System.out.println(A.getPosX());
//        System.out.println(A.getPosY());
        if (A.getPosX() < boarder || A.getPosX() > width + boarder)
            return false;
        if (A.getPosY() < boarder || A.getPosY() > height + boarder)
            return false;
        return true;
    }

    public Image makeBackground(int width, int height, int delta, int lineDelta) {
        this.width = width;
        this.height = height;
        int newHeight = height + 2 * delta, newWidth = width + 2 * delta;
        Pixmap pixmap = new Pixmap(newWidth, newHeight, Pixmap.Format.RGBA8888);
        Color backColor = new Color(205.f / 255, 205.f / 255, 205.f / 255, 1);
        Color lineColor = new Color(195.f / 255, 195.f / 255, 195.f / 255, 1);
        Color boardColor = new Color(184.f / 255, 184.f / 255, 184.f / 255, 1);
        pixmap.setColor(backColor);
        pixmap.fillRectangle(0, 0, newWidth, newHeight);
        pixmap.setColor(boardColor);
        pixmap.fillRectangle(0, 0, newWidth, delta);
        pixmap.fillRectangle(0, 0, delta, newHeight);
        pixmap.fillRectangle(newWidth - delta, 0, delta, newHeight);
        pixmap.fillRectangle(0, newHeight - delta, newWidth, delta);
        pixmap.setColor(lineColor);
        for (int i = 0; i <= newWidth; i += lineDelta) {
            pixmap.fillRectangle(i, 0, 1, newHeight);
        }
        for (int i = 0; i <= newHeight; i += lineDelta) {
            pixmap.fillRectangle(0, i, newWidth, 1);
        }
        Image image = new Image(new Texture(pixmap));
        image.setZIndex(0);
        return image;
    }

    public void updateFrame(GameFrame gameFrame) {
        for (GameAction i : gameFrame.frameList) {
            if(i.getType().equals("NewPlayer")) {
                if (game.actionGroup.modify.get(0) == null)
                    game.actionGroup.modify.put(0, new GameFrame(0));
                GameFrame cur = game.actionGroup.modify.get(0);
                cur.add(i);
                game.actionGroup.modify.put(0, cur);
                return;
            }
            if (game.actionGroup.modify.get(i.getObjectID()) == null)
                game.actionGroup.modify.put(i.getObjectID(), new GameFrame(0));
            GameFrame cur = game.actionGroup.modify.get(i.getObjectID());
            cur.add(i);
            game.actionGroup.modify.put(i.getObjectID(), cur);
        }
    }

    public void CheckCollision() {
        while (!game.toBeDeleted.isEmpty()) {
            game.toBeDeleted.peek().die();
            game.toBeDeleted.poll();
        }
        if (stage.getActors().isEmpty())
            return;
        for (int i2 = 0; i2 < 2; i2++)
            for (int i = 0; i < group[i2].getChildren().size; i++) {
                Object[] array = group[i2].getChildren().begin();
                GameObject A = (GameObject) array[i];

                for (int j2 = 0; j2 < 2; j2++)
                    for (int j = 0; j < group[j2].getChildren().size; j++) {
                        Object[] array2 = group[j2].getChildren().begin();
                        GameObject B = (GameObject) array2[j];

                        if (A.area.overlaps(B.area)) {
                            A.CheckCollision(B);
                        }
                    }
            }
    }

    private void listenKey() {
        float x = 0, y = 0;
        fireGap += 10;

        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            x = moveTouchpad.getKnobX() - 200f;
            y = moveTouchpad.getKnobY() - 200f;
        }
        else {
            if (Gdx.input.isKeyPressed(Input.Keys.W))
                y += 1;
            if (Gdx.input.isKeyPressed(Input.Keys.S))
                y -= 1;
            if (Gdx.input.isKeyPressed(Input.Keys.D))
                x += 1;
            if (Gdx.input.isKeyPressed(Input.Keys.A))
                x -= 1;
        }
//        System.out.println("ASASASAS" + fireTouchpad.getKnobX() + " " + fireTouchpad.getKnobY());

        try {
            float direction = MathUtils.atan2(x, y);
            Gson gson = new Gson();
            if (x != 0 || y != 0) {
                game.queue.put(gson.toJson(new GameAction("Move", direction, game.playerID, "", 4), GameAction.class));
            }
            float posX, posY, angle;
            if (Gdx.app.getType() == Application.ApplicationType.Android) {
                posX = fireTouchpad.getKnobX() - 200f;
                posY = fireTouchpad.getKnobY() - 200f;
                angle = MathUtils.atan2(posX, posY);
                if (fireTouchpad.isTouched())
                    if (fireGap >= MainPlayer.playerFireGap) {
                        game.queue.put(gson.toJson(new GameAction("Fire", angle, game.playerID, "", 0), GameAction.class));
                        fireGap = 0;
                    }
                if (game.playerMP == 10 && fireButton.isPressed()) {
                    // Special skill.

                    game.playerMP = 0;
                    game.queue.put(gson.toJson(new GameAction("QSkill", angle, game.playerID, "", 0), GameAction.class));
                }
            }
            else {
                posX = -(Gdx.graphics.getWidth() / 2f - Gdx.input.getX());
                posY = Gdx.graphics.getHeight() / 2f - Gdx.input.getY();
                angle = MathUtils.atan2(posX, posY);
                if (Gdx.input.isTouched())
                    if (fireGap >= MainPlayer.playerFireGap) {
                        System.out.println(MainPlayer.playerFireGap);
                        game.queue.put(gson.toJson(new GameAction("Fire", angle, game.playerID, "", 0), GameAction.class));
                        fireGap = 0;
                    }
                if (game.playerMP == 10 && Gdx.input.isKeyPressed(Input.Keys.Q)) {
                    // Special skill.
                    game.queue.put(gson.toJson(new GameAction("QSkill", angle, game.playerID, "", 0), GameAction.class));
                    game.playerMP = 0;
                }
            }
            game.queue.put(gson.toJson(new GameAction("Rotate", angle, game.playerID, "", 0), GameAction.class));
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void generateSup(double X, double Y) {
        double r = dataMaker.nextGaussian() * 40 + 200;
        int size = (int) (dataMaker.nextGaussian() * 1 + 2);
        if (size <= 0)
            return;
        supplies += size;
        while ((size--) != 0) {
            double theta = 2 * MathUtils.PI * dataMaker.nextFloat();
            double delta = (0.5 + dataMaker.nextGaussian() * 0.1) * r;
            group[0].addActor(new SupplyObject(0f, 0, (float) (X + delta * Math.cos(theta)), (float) (Y + delta * Math.sin(theta)), 50, game, stage, group, (float) dataMaker.nextGaussian() * 0.2f));
        }
    }

    private void keepSup() {
        if (supplies < 30) {
            while (supplies < 30) {
                double X, Y;
                X = width / 2 + dataMaker.nextGaussian() * 1000 + boarder;
                Y = height / 2 + dataMaker.nextGaussian() * 500 + boarder;
                generateSup(X, Y);
            }
        }
    }

    @Override
    public void show() {
        dataMaker = new Random(game.getRoomName().hashCode());
        game.playerID = game.getUserName().hashCode();
        game.playerMP = 0;
        mpProgress.setValue(0f);
        game.queue.clear();
        stage.clear();
        this.group = new Group[2];
        group[0] = new Group();
        group[1] = new Group();
        group[0].addActor(new PlayerObject(0, 0, 0, 0, 0, 0, "f", game, stage, group, 1));
        stage.addActor(makeBackground(3840, 2160, boarder, 30));
        this.stage.addActor(group[0]);
        this.stage.addActor(group[1]);
        GameObject.colorPool.init();
        Gson gson = new Gson();
        try {
            if(game.playerType==0)
                game.queue.put(gson.toJson(new GameAction("NewPlayer", boarder + 100, game.playerType, game.getUserName(), boarder + 100)));
            if(game.playerType==1)
                game.queue.put(gson.toJson(new GameAction("NewPlayer", 3840+boarder-100, game.playerType, game.getUserName(), 2160+boarder-100)));
            if(game.playerType==2)
                game.queue.put(gson.toJson(new GameAction("NewPlayer", boarder + 100, game.playerType, game.getUserName(), 2160+boarder-100)));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (Gdx.app.getType() == Application.ApplicationType.Android)
            Gdx.input.setInputProcessor(controllerStage);
        else
            Gdx.input.setInputProcessor(stage);
        try {
            downloader = new Thread(new ClientDownloader(game, game.S2C));
            listener = new Thread(new ClientListener(game, game.C2S, game.queue));
            downloader.start();
            listener.start();
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        characterBgms.get(game.playerType).play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 0);
        listenKey();
        GameFrame g = game.download.peek();
        while (g == null) {
            g = game.download.peek();
        }
        updateFrame(g);
        game.download.poll();
        stage.act(delta);
        uiStage.clear();
        Label mpLabel = new Label("MP " + game.playerMP + "/10", game.skin, "label-tank");
        mpLabel.setPosition(mpProgress.getX() + (mpProgress.getWidth() - mpLabel.getWidth()) / 2f, mpProgress.getY() - 20f);
        mpProgress.setValue((float) game.playerMP);
        uiStage.addActor(mpLabel);
        uiStage.addActor(mpProgress);
        uiStage.addActor(game.characterFlags.get(game.playerType));
        uiStage.act();
        controllerStage.act();
        CheckCollision();
        keepSup();
        stage.draw();
        uiStage.draw();
        controllerStage.draw();
    }

    @Override
    public void hide() {
        characterBgms.get(game.playerType).stop();
        downloader.interrupt();
        listener.interrupt();
//        System.out.println("TEST");
    }

    @Override
    public void dispose() {

    }
}
