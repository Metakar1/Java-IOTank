package org.gyming.tank.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.gyming.tank.client.ActionGroup;
import org.gyming.tank.connection.GameAction;

public class BulletObject extends GameObject {
    static int bulletSize = 10;
    public static int bulletSpeed = 1;
    public static int bulletHP = 10;
    private int playerID;

    public BulletObject(double speed, double direction, double posX, double posY, int hp, int playerID, ActionGroup actionGroup, Stage stage) {
        super(speed, direction, posX, posY, hp, actionGroup, stage);
        this.playerID = playerID;
    }

    public final int getPlayerID() {
        return playerID;
    }

    public final void setPlayerID(int playerID) {
        this.playerID = playerID;
    }


    @Override
    protected Texture createTexture() {
        return drawCircle(bulletSize,colorPool.getUserColor(playerID));
    }

    @Override
    public void fire(GameAction action, double posX, double posY) {

    }
}
