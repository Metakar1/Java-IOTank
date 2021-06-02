package org.gyming.tank.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.google.gson.Gson;
import org.gyming.tank.connection.GameAction;
import org.gyming.tank.connection.GameFrame;
import org.gyming.tank.object.PlayerObject;

import java.util.HashMap;

public class MainScreen extends ScreenAdapter {
    TankGame game;
    Stage stage;
    HashMap<Integer, PlayerObject> idmap;

    public MainScreen(TankGame game) {
        this.stage = new Stage();
        this.game = game;
    }

    private void ListenKey() {
        int x = 0, y = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            y += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            y -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            x += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            x += 1;

        try {
            double direction = Math.atan2(x, y);
            Gson gson = new Gson();
            if (x != 0 || y != 0)
                game.queue.put(gson.toJson(new GameAction("Move", direction, game.PlayerId, "", 1), GameAction.class));

            if (Gdx.input.isTouched())
                game.queue.put(gson.toJson(new GameAction("Fire", direction, game.PlayerId, "", 0), GameAction.class));
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void show() {
        game.PlayerId = game.getUserName().hashCode();
        Gson gson = new Gson();
        try {
            game.queue.put(gson.toJson(new GameAction("NewPlayer", 0, game.getUserName().hashCode(), game.getUserName(), 0)));
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
        ScreenUtils.clear(1, 1, 1, 0);
        ListenKey();
        stage.draw();
    }

    @Override
    public void dispose() {

    }
}
