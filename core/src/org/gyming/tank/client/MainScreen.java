package org.gyming.tank.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
    static int fireGap = 0;
    TankGame game;
    Stage stage;

    public MainScreen(TankGame game) {
        this.stage = new Stage();
        this.game = game;
        stage.addActor(new PlayerObject(0, 0, 50, 50, PlayerObject.playerHP, 0, "f", game, stage));
        stage.addActor(makeBackground(3840, 2160, boarder, 30));
    }

    public static boolean isInside(GameObject A) {
        if (A.getPlayerID() == 0)
            return true;
        System.out.println(A.getPosX());
        System.out.println(A.getPosY());
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
            if (game.actionGroup.modify.get(i.getObjectID()) == null)
                game.actionGroup.modify.put(i.getObjectID(), new GameFrame(0));
            GameFrame cur = game.actionGroup.modify.get(i.getObjectID());
            cur.add(i);
            game.actionGroup.modify.put(i.getObjectID(), cur);
        }
    }

    public void checkCollision() {
        while (!game.toBeDeleted.isEmpty()) {
            game.toBeDeleted.peek().die();
            game.toBeDeleted.poll();
        }
        if (stage.getActors().isEmpty())
            return;
        for (int i = 0; i < stage.getActors().size; i++) {
            if (stage.getActors().items[i] instanceof Image)
                continue;
            GameObject A = (GameObject) stage.getActors().items[i];
            if (A.getHp() <= 0)
                continue;
            for (int j = i + 1; j < stage.getActors().size; j++) {
                if (stage.getActors().items[j] instanceof Image)
                    continue;
                GameObject B = (GameObject) stage.getActors().items[j];
                if (B.getHp() <= 0)
                    continue;

                if (!(A instanceof SupplyObject) && !(B instanceof SupplyObject))
                    if (A.getPlayerID() == B.getPlayerID())
                        continue;

                if (A.area.overlaps(B.area)) {
                    System.out.println("FUCK");
                    if (A instanceof BulletObject) {
                        A.setHp(0);
                        if (B instanceof BulletObject)
                            B.setHp(0);
                        else if (B instanceof PlayerObject || B instanceof SupplyObject) {
                            B.setDirection((float) (Math.PI * 2 - B.getDirection()));
                            B.setHp(B.getHp() - 10);
                            System.out.println("FUCK");
                            if (B.getHp() < 0) {
                                //add EXP to A.player
                            }
                        }
                    }
                    else if (A instanceof PlayerObject) {
                        A.setSpeed(-1 * A.getSpeed());
                        if (B instanceof BulletObject) {
//                            System.out.println("FUCK");
                            A.setHp(A.getHp() - 10);
                            B.setHp(0);
                            if (A.getHp() < 0) {
                                // Add EXP to B.player
                            }
                        }
                        else if (B instanceof PlayerObject || B instanceof SupplyObject) {
                            A.setHp(A.getHp() - 5);
                            B.setHp(B.getHp() - 5);
                            if (A.getHp() < 0) {
                                if (B instanceof PlayerObject) {
                                    // add EXP to B.playerid
                                }
                            }
                            if (B.getHp() < 0) {
                                // add EXP to A.playerid
                            }
                            B.setDirection((float) (Math.PI * 2 - B.getDirection()));
                        }
                    }
                    else if (A instanceof SupplyObject) {
                        A.setDirection((float) (Math.PI * 2 - A.getDirection()));
                        System.out.println("FUCK");
                        if (B instanceof BulletObject) {
                            A.setHp(A.getHp() - 10);
                            B.setHp(0);
                            if (A.getHp() < 0) {
                                // Add EXP to B.player
                            }
                        }
                        else if (B instanceof PlayerObject || B instanceof SupplyObject) {
                            System.out.println("FUCK");
                            if (B instanceof PlayerObject) {
                                A.setHp(A.getHp() - 5);
                                B.setHp(B.getHp() - 5);
                            }
                            B.setDirection((float) (Math.PI * 2 - B.getDirection()));
                            if (A.getHp() < 0) {
                                if (B instanceof PlayerObject) {
                                    // add EXP to B.playerid
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    private void listenKey() {
        float x = 0, y = 0;
        fireGap += 10;

        if (Gdx.input.isKeyPressed(Input.Keys.W))
            y += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            y -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            x += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            x -= 1;

        try {
            float direction = MathUtils.atan2(x, y);
            Gson gson = new Gson();
            if (x != 0 || y != 0) {
                game.queue.put(gson.toJson(new GameAction("Move", direction, game.playerID, "", 4), GameAction.class));
            }
            float posX = -(Gdx.graphics.getWidth() / 2f - Gdx.input.getX());
            float posY = Gdx.graphics.getHeight() / 2f - Gdx.input.getY();
            float angle = MathUtils.atan2(posX, posY);
            if (Gdx.input.isTouched())
                if (fireGap >= PlayerObject.playerFireGap) {

                    System.out.println(posX);
                    System.out.println(posY);

                    game.queue.put(gson.toJson(new GameAction("Fire", angle, game.playerID, "", 0), GameAction.class));
                    fireGap = 0;
                }
            game.queue.put(gson.toJson(new GameAction("Rotate", angle, game.playerID, "", 0), GameAction.class));
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void GenerateSup(double X, double Y) {
        double r = dataMaker.nextGaussian() * 40 + 200;
        int size = (int) (dataMaker.nextGaussian() * 5 + 12);
        if (size <= 0)
            return;
        supplies += size;
        while ((size--) != 0) {
            double theta = 2 * MathUtils.PI * dataMaker.nextFloat();
            double delta = (0.5 + dataMaker.nextGaussian() * 0.1) * r;
            stage.addActor(new SupplyObject(0f, 0, (float) (X + delta * Math.cos(theta)), (float) (Y + delta * Math.sin(theta)), 50, game, stage));
        }
    }

    private void KeepSup() {
        if (supplies < 20) {
            while (supplies < 60) {
                double X, Y;
                X = width / 2 + dataMaker.nextGaussian() * 1000 + boarder;
                Y = height / 2 + dataMaker.nextGaussian() * 500 + boarder;
                GenerateSup(X, Y);
            }
        }
    }

    @Override
    public void show() {
        dataMaker = new Random(game.getRoomName().hashCode());
        game.playerID = game.getUserName().hashCode();
        Gson gson = new Gson();
        try {
            game.queue.put(gson.toJson(new GameAction("NewPlayer", boarder + 100, 0, game.getUserName(), boarder + 100)));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Gdx.input.setInputProcessor(stage);
        try {
            Thread downloader = new Thread(new ClientDownloader(game, game.S2C));
            Thread listener = new Thread(new ClientListener(game, game.C2S, game.queue));
            downloader.start();
            listener.start();
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        checkCollision();
        KeepSup();
        stage.draw();
    }

    @Override
    public void dispose() {

    }
}
