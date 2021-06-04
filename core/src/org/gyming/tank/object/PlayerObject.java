package org.gyming.tank.object;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.gyming.tank.client.TankGame;
import org.gyming.tank.connection.GameAction;

public class PlayerObject extends GameObject {
    public static int playerSize = 30;
    public static int playerSpeed = 25;
    public static int playerHP = 100;
    public static int playerFireGap = 60;
    private int playerID;
    private String playerName;
    private Stage stage;
//    public static int playerGunWidth =

    public PlayerObject(float speed, float direction, float posX, float posY, int hp, int playerID,
                        String playerName, TankGame game, Stage stage) {
        super(speed, direction, posX, posY, hp, game, stage);
        this.playerID = playerID;
        this.playerName = playerName;
        this.stage = stage;
        this.identifier = playerID;
    }

    public final int getPlayerID() {
        return playerID;
    }

    public final void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public final String getPlayerName() {
        return playerName;
    }

    public final void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @Override
    protected Texture createTexture() {
        playerSize=playerSize*2;
        int gunHeight = playerSize*2/3+3;
        int gunWidth = gunHeight*5/6;
        int cirR = playerSize-gunHeight/2;
        Pixmap pixmap = new Pixmap(playerSize * 2, playerSize * 2, Pixmap.Format.RGBA8888);

        pixmap.setColor(colorPool.getGunBoarderColor());
        pixmap.fillRectangle(cirR-gunWidth/2,0,gunWidth,gunHeight);

        pixmap.setColor(colorPool.getGunColor());
        pixmap.fillRectangle(cirR-gunWidth/2+3,0+3,gunWidth-6,gunHeight-6);


        pixmap.setColor(colorPool.getUserBoarderColor(playerID));
        pixmap.fillCircle(cirR, cirR+gunHeight-5, cirR);
        pixmap.setColor(colorPool.getUserColor(playerID));
        pixmap.fillCircle(cirR, cirR+gunHeight-5, cirR - 3);
        Texture texture = new Texture(pixmap);
        texture.setFilter (Texture.TextureFilter.Linear, Texture.TextureFilter.Linear );
//        System.out.println(colorPool.getUserColor(playerID));
//        System.out.println("***");
//        texture
        return texture;
    }

    @Override
    public void fire(GameAction action, float posX, float posY) {
        BulletObject bullet = new BulletObject(BulletObject.bulletSpeed, action.getDirection(), posX + (texture.getWidth()) / 2.0f, posY + (texture.getHeight()) / 2.0f, BulletObject.bulletHP, playerID, game, stage);
        stage.addActor(bullet);
    }

    @Override
    protected void recoverSpeed() {
        if (speed > 0) {
            speed = Math.max(speed - acceleration, 0);
        }
        else {
            speed = Math.min(speed + acceleration, 0);
        }
    }

    public void die() {
        stage.getRoot().removeActor(this);
        this.setHp(0);
    }
}
