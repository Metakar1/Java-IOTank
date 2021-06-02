package org.gyming.tank.client;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.google.gson.Gson;
import org.gyming.tank.connection.GameAction;

public class MainScreen implements Screen {
    TankGame game;
    Stage stage;

    public MainScreen(TankGame game) {
        this.stage = new Stage();
        this.game  = game;
    }

    private void ListenKey() throws  InterruptedException
    {
        int x = 0,y = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W))
            y += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            y -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            x += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            x += 1;

        double direction = Math.atan2(x, y);
        Gson gson = new Gson();
        if(x!=0 || y!=0)
            game.queue.put(gson.toJson(new GameAction("Move",direction,game.mainplayer.getPlayerID(),"",game.mainplayer.getSpeed()),GameAction.class));

        if(Gdx.input.isTouched())
            game.queue.put(gson.toJson(new GameAction("Fire",direction,game.mainplayer.getPlayerID(),"",0),GameAction.class));
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
