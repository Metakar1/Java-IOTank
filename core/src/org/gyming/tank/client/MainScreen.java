package org.gyming.tank.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.google.gson.Gson;
import org.gyming.tank.connection.GameAction;
import org.gyming.tank.object.PlayerObject;

public class MainScreen extends ScreenAdapter {
    static int fireGap = 0;
    TankGame game;
    Stage stage;

    public MainScreen(TankGame game) {
        this.stage = new Stage();
        this.game = game;
        stage.addActor(new PlayerObject(0, 0, 50, 50, 0, 0, "f", game, stage));
    }

    private void ListenKey() {
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
                game.queue.put(gson.toJson(new GameAction("Move", direction, game.playerID, "", 1), GameAction.class));
            }
            if (Gdx.input.isTouched())
                if (fireGap >= PlayerObject.playerFireGap) {
                    float posX = -(Gdx.graphics.getWidth() / 2f - Gdx.input.getX());
                    float posY = Gdx.graphics.getHeight() / 2f - Gdx.input.getY();
                    System.out.println(posX);
                    System.out.println(posY);
                    float angle = MathUtils.atan2(posX, posY);
                    game.queue.put(gson.toJson(new GameAction("Fire", angle, game.playerID, "", 0), GameAction.class));
                    fireGap = 0;
                }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void show() {
        game.playerID = game.getUserName().hashCode();
        Gson gson = new Gson();
        try {
            game.queue.put(gson.toJson(new GameAction("NewPlayer", 0, 0, game.getUserName(), 0)));
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
        ListenKey();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {

    }
}
