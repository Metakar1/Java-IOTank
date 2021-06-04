package org.gyming.tank.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.gyming.tank.client.TankGame;
import org.gyming.tank.connection.GameAction;

public class BulletObject extends GameObject {
    public static int bulletSpeed = 10;
    public static int bulletHP = 10;
    static int bulletSize = 10;
    private int playerID;

    public BulletObject(float speed, float direction, float posX, float posY, int hp, int playerID, TankGame game, Stage stage) {
        super(speed, direction, posX, posY, hp, game, stage);
        this.playerID = playerID;
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
        return drawCircle(bulletSize, colorPool.getUserColor(playerID));
    }

    @Override
    public void fire(GameAction action, float posX, float posY) {

    }

    @Override
    protected void recoverSpeed() {

    }

    public void die() {
        stage.getRoot().removeActor(this);
        this.setHp(0);
    }
}
