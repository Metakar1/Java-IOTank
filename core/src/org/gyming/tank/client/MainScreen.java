package org.gyming.tank.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
                game.queue.put(gson.toJson(new GameAction("Move", direction, game.mainPlayer.getPlayerID(), "", game.mainPlayer.getSpeed()), GameAction.class));

            if (Gdx.input.isTouched())
                game.queue.put(gson.toJson(new GameAction("Fire", direction, game.mainPlayer.getPlayerID(), "", 0), GameAction.class));
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void Update() {
        while (!game.download.isEmpty()) {
            GameFrame now = game.download.peek();
            for (GameAction it : now.frameList) {
                if (it.getType().equals("Move")) {
                    int id = it.getObjectID();
                    double direction, speed;
                    direction = it.getDirection();
                    speed = it.getValue();
                    PlayerObject temp = idmap.get(id);
                    temp.setPosX(temp.getPosX() + speed * Math.tan(direction));
                    temp.setPosY(temp.getPosY() + speed * Math.sqrt(1 - Math.tan(direction) * Math.tan(direction)));
                }
                else if (it.getType().equals("Fire")) {
                    int id = it.getObjectID();
                    PlayerObject temp = idmap.get(id);
                    temp.fire(it, temp.getPosX(), temp.getPosY());
                }
                else if (it.getType().equals("NewPlayer")) {
                    NewPlayer(it.getObjectID(), it.getProperty());
                }
                else {
                    System.out.println("Error " + it.getType());
                }
            }
        }
    }

    private void NewPlayer(int playerID, String playerName) {
        double x, y;
        x = y = 0; // random x,y
        PlayerObject temp = new PlayerObject(PlayerObject.playerSpeed, 0, x, y, PlayerObject.playerHP, playerID, playerName, game.actionGroup, stage);
        stage.addActor(temp);
        if (playerName.equals(game.getUserName()))
            game.mainPlayer = temp;
        idmap.put(playerID, temp);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ListenKey();
        Update();
    }

    @Override
    public void dispose() {

    }
}
