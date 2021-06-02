package org.gyming.tank.object;

import org.gyming.tank.client.ActionGroup;
import org.gyming.tank.connection.GameAction;
import org.gyming.tank.connection.GameFrame;

public class BulletObject extends GameObject {
    private int playerID;

    public BulletObject(double speed, double direction, double posX, double posY, int hp, int playerID, ActionGroup actionGroup) {
        super(speed, direction, posX, posY, hp, actionGroup);
        this.playerID = playerID;
    }

    public final int getPlayerID() {
        return playerID;
    }

    public final void setPlayerID(int playerID) {
        this.playerID = playerID;
    }


    @Override
    public void reactAction(GameFrame actions) {

    }
}
