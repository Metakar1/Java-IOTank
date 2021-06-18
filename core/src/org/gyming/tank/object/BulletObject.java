package org.gyming.tank.object;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.gyming.tank.client.TankGame;
import org.gyming.tank.connection.GameAction;

public class BulletObject extends GameObject {
    public  int bulletSpeed = 10;
    public  int bulletHP = 10;
    public int bulletSize;
    public int bulletATK;
    private int playerID;
    public int bulletTime;

    public BulletObject(float speed, float direction, float posX, float posY, int hp, int playerID, TankGame game, Stage stage, Group[] group,int bulletSize,int bulletATK,int bulletTime) {
        super(speed, direction, posX, posY, hp, game, stage, group);
        this.playerID = playerID;

        this.posX -= texture.getWidth() / 2.0f;
        this.posY -= texture.getHeight() / 2.0f;
        this.bulletATK = bulletATK;
        this.bulletSize = bulletSize;
        this.bulletTime = bulletTime;
        texture = createTexture();
    }

    public final int getPlayerID() {
        return playerID;
    }

    public final void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    @Override
    protected Texture createTexture() {
        Pixmap pixmap = new Pixmap(bulletSize * 2, bulletSize * 2, Pixmap.Format.RGBA8888);
        pixmap.setColor(colorPool.getUserBoarderColor(playerID));
        pixmap.fillCircle(bulletSize, bulletSize, bulletSize);
        pixmap.setColor(colorPool.getUserColor(playerID));
        pixmap.fillCircle(bulletSize, bulletSize, bulletSize - 3);
        Texture texture = new Texture(pixmap);
        System.out.println(this.bulletSize);
        return texture;
    }

    @Override
    public void fire(GameAction action, float posX, float posY) {

    }

    @Override
    protected void recoverSpeed() {

    }
    @Override
    protected void getDmg(){}

    @Override
    public void qSkill(GameAction action, float posX, float posY){}

    public void die() {
        stage.getRoot().removeActor(this);
        group[1].removeActor(this);
        group[0].removeActor(this);
        this.setHp(0);
    }
}