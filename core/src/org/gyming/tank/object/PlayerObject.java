package org.gyming.tank.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import org.gyming.tank.client.TankGame;
import org.gyming.tank.connection.GameAction;

public class PlayerObject extends GameObject {
    public  int playerSize;
    public  int playerSpeed;
    public  int playerHP;
    public  int playerFireGap;
    public  int ratio;
    public  int gunHeight;
    public  int gunWidth;
    public  int cirR;
    static  int boarder;
    private int playerID;
    private int playerType;
    private String playerName;
    private Stage stage;
    public ProgressBar hpProgress;

//    public static int playerGunWidth =

    public PlayerObject(float speed, float direction, float posX, float posY, int hp, int playerID,
                        String playerName, TankGame game, Stage stage, Group[] group, int type) {

        super(speed, direction, posX, posY, hp, game, stage, group);
        playerType = type;
        if(type==1) {
            playerSize = 30;
            playerSpeed = 25;
            playerHP = 100;
            playerFireGap = 60;
            ratio = 1;
            gunHeight = playerSize * ratio * 2 / 3 + 3;
            gunWidth = gunHeight * 5 / 6;
            cirR = playerSize * ratio - gunHeight / 2;
            boarder = 3 * ratio;
        }
        this.hpProgress = new ProgressBar(0f, 100f, 1f, false, game.skin, "progressbar-tank");
        this.texture = createTexture();
        hpProgress.setWidth(this.texture.getWidth()-10);
        hpProgress.setHeight(20);
        this.setHp(playerHP);
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
        float diffX = MathUtils.sin((360-gunDirection)/180)*this.gunWidth;
        float diffY = MathUtils.cos((360-gunDirection)/180)*this.gunWidth;
        float rx = posX + this.cirR +1;
        float ry = posY + texture.getHeight()-(this.cirR+this.gunHeight-PlayerObject.boarder*2)-1;
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