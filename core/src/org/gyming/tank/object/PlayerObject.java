package org.gyming.tank.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.gyming.tank.client.ActionGroup;
import org.gyming.tank.connection.GameAction;

public class PlayerObject extends GameObject {
    private static int playerSize;
    private int playerID;
    private String playerName;
    private Stage stage;

    public PlayerObject(double speed, double direction, double posX, double posY, int hp, int playerID, String playerName, ActionGroup actionGroup, Stage stage) {
        super(speed, direction, posX, posY, hp, actionGroup, stage);
        this.playerID = playerID;
        this.playerName = playerName;
        this.stage = stage;
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
        return drawCircle(playerSize,colorPool.getUserColor(playerID));
    }

    @Override
    public void fire(GameAction action, double posX, double posY) {
        BulletObject bullet = new BulletObject(BulletObject.bulletSpeed,action.getDirection(),posX, posY, BulletObject.bulletHP,playerID,actionGroup,stage);
        stage.addActor(bullet);
    }
}
