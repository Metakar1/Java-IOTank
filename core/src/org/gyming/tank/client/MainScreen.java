package org.gyming.tank.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.google.gson.Gson;
import org.gyming.tank.connection.GameAction;
import org.gyming.tank.connection.GameFrame;
import org.gyming.tank.object.GameObject;
import org.gyming.tank.object.PlayerObject;

import java.util.HashMap;

public class MainScreen extends ScreenAdapter {
    TankGame game;
    Stage stage;
    static int FireGap = 0;
    public MainScreen(TankGame game) {
        this.stage = new Stage();
        this.game = game;
        stage.addActor(new PlayerObject(0,0,50,50,0,0,"f", game.actionGroup, stage));
//        Texture texture = GameObject.drawCircle(10, Color.W);
//        System.out.println(game.PlayerId);
    }

    private void ListenKey() {
        int x = 0, y = 0;
        FireGap+=10;

        if (Gdx.input.isKeyPressed(Input.Keys.W))
            y += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            y -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            x += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            x -= 1;

        try {
            double direction = Math.atan2(x, y);
            Gson gson = new Gson();
            if (x != 0 || y != 0) {
                game.queue.put(gson.toJson(new GameAction("Move", direction, game.playerId, "", 1), GameAction.class));
            }
            if (Gdx.input.isTouched())
                if (FireGap >= PlayerObject.playerFireGap) {
                    game.queue.put(gson.toJson(new GameAction("Fire", direction, game.playerId, "", 0), GameAction.class));
                    FireGap = 0;
                }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void show() {
        game.playerId = game.getUserName().hashCode();
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
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void render(float delta) {
//        System.out.println("ASASASASASSA");
        ScreenUtils.clear(0, 0, 0, 0);
        ListenKey();
        stage.act(delta);
        stage.draw();
//        GameObject actor = (GameObject) stage.getActors().get(1);
//        System.out.println(actor.);
    }

    @Override
    public void dispose() {

    }
}
