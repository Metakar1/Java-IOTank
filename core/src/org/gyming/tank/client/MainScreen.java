package org.gyming.tank.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.google.gson.Gson;
import org.gyming.tank.connection.GameAction;
import org.gyming.tank.object.BulletObject;
import org.gyming.tank.object.GameObject;
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

    public Texture makeBackGround(int width, int height, int delta, int lineDelta) {
        int newHeight = height+2*delta, newWidth = width+2*delta;
        Pixmap pixmap = new Pixmap(newWidth, newHeight, Pixmap.Format.RGBA8888);
        Color backColor = new Color(205.f/255,205.f/255,205.f/255,1);
        Color lineColor = new Color(195.f/255,195.f/255,195.f/255,1);
        pixmap.setColor(backColor);
        pixmap.fillRectangle(0,0,delta,newHeight);
        pixmap.fillRectangle(0,0,newWidth,delta);
        pixmap.fillRectangle(newWidth-delta,0,delta,newHeight);
        pixmap.fillRectangle(0,newWidth-delta,newWidth,delta);
        pixmap.setColor(lineColor);
        for(int i=0;i<=newWidth;i+=lineDelta) {
            pixmap.fillRectangle(i,0,1,newHeight);
        }
        return new Texture(pixmap);


    }

    public void CheckCollision()
    {
        if(stage.getActors().isEmpty())
            return;
        for(int i=0; i < stage.getActors().size; i++)
        {
            GameObject A = (GameObject) stage.getActors().items[i];
            if(A.getHp()==0)
                continue;
            for (int j=i+1; j < stage.getActors().size; j++)
            {
                GameObject B = (GameObject) stage.getActors().items[j];

                if(B.getHp()==0)
                    continue;
                if(A.area.overlaps(B.area))
                {
                    if(A instanceof BulletObject) {
                    }

                    break;
                }
            }
        }
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
        CheckCollision();
        stage.draw();
    }

    @Override
    public void dispose() {

    }
}
