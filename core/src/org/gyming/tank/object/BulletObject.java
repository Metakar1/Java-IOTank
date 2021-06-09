package org.gyming.tank.object;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.gyming.tank.client.TankGame;
import org.gyming.tank.connection.GameAction;

public class BulletObject extends GameObject {
    public static int bulletSpeed = 10;
    public static int bulletHP = 10;
    static int bulletSize = 10;
    private int playerID;

    public BulletObject(float speed, float direction, float posX, float posY, int hp, int playerID, TankGame game, Stage stage, Group[] group) {
        super(speed, direction, posX, posY, hp, game, stage, group);
        this.playerID = playerID;
        texture = createTexture();
        this.posX -= texture.getWidth() / 2.0f;
        this.posY -= texture.getHeight() / 2.0f;
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

    public void die() {
        stage.getRoot().removeActor(this);
        group[1].removeActor(this);
        group[0].removeActor(this);
        this.setHp(0);
    }
}