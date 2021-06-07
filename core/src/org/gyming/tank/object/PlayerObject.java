package org.gyming.tank.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.gyming.tank.client.TankGame;
import org.gyming.tank.connection.GameAction;

public class PlayerObject extends GameObject {
    public static int playerSize = 30;
    public static int playerSpeed = 25;
    public static int playerHP = 100;
    public static int playerFireGap = 60;
    public static int ratio = 1;
    public static int gunHeight = playerSize*ratio*2/3+3;
    public static int gunWidth = gunHeight*5/6;
    public static int cirR = playerSize*ratio-gunHeight/2;
    static public int boarder = 3*ratio;
    private int playerID;
    private String playerName;
    private Stage stage;
//    public static int playerGunWidth =

    public PlayerObject(float speed, float direction, float posX, float posY, int hp, int playerID,
                        String playerName, TankGame game, Stage stage, Group[] group) {
        super(speed, direction, posX, posY, hp, game, stage, group);
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

        int cutSize =playerSize*ratio;


        Pixmap pixmap = new Pixmap(cutSize * 2, cutSize * 2, Pixmap.Format.RGBA8888);

        pixmap.setColor(colorPool.getGunBoarderColor());
        pixmap.fillRectangle(cirR-gunWidth/2,0,gunWidth,gunHeight);

        pixmap.setColor(colorPool.getGunColor());
        pixmap.fillRectangle(cirR-gunWidth/2+boarder,0+boarder,gunWidth-boarder*2,gunHeight-boarder*2);


        pixmap.setColor(colorPool.getUserBoarderColor(playerID));
        pixmap.fillCircle(cirR, cirR+gunHeight-boarder*2, cirR);
        pixmap.setColor(colorPool.getUserColor(playerID));
        pixmap.fillCircle(cirR, cirR+gunHeight-boarder*2, cirR - boarder);

        Pixmap pixmap1 = new Pixmap(playerSize*2,playerSize*2,Pixmap.Format.RGBA8888);
        pixmap1.drawPixmap(pixmap,0,0,pixmap.getWidth(),pixmap.getHeight(),0,0,pixmap1.getWidth(),pixmap1.getHeight());
        Texture texture = new Texture(pixmap1);
        texture.setFilter (Texture.TextureFilter.Linear, Texture.TextureFilter.Linear );
//        System.out.println(colorPool.getUserColor(playerID));
//        System.out.println("***");
//        texture
        return texture;
    }

    @Override
    public void fire(GameAction action, float posX, float posY) {
//        System.out.println(360-gunDirection);
        float diffX = MathUtils.sin((360-gunDirection)/180)*PlayerObject.gunWidth;
        float diffY = MathUtils.cos((360-gunDirection)/180)*PlayerObject.gunWidth;
        float rx = posX + PlayerObject.cirR +1;
        float ry = posY + texture.getHeight()-(PlayerObject.cirR+PlayerObject.gunHeight-PlayerObject.boarder*2)-1;
        System.out.println(Float.toString(diffX)+" "+Float.toString(diffY));
        rx += 40*MathUtils.sin((360-gunDirection)/180*(float) Math.PI);
        ry += 40*MathUtils.cos((360-gunDirection)/180*(float) Math.PI);
        BulletObject bullet = new BulletObject(BulletObject.bulletSpeed, action.getDirection(), rx, ry, BulletObject.bulletHP, playerID, game, stage, group);
        group[0].addActor(bullet);
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
        group[1].removeActor(this);
        group[0].removeActor(this);
        this.setHp(0);
    }
}