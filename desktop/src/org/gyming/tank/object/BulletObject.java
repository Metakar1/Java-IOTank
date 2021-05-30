package org.gyming.tank.object;

public class BulletObject extends GameObject {
    private int playerID;

    public BulletObject(double speed, double direction, double posX, double posY, int hp, int playerID) {
        super(speed, direction, posX, posY, hp);
        this.playerID = playerID;
    }

    public final int getPlayerID() {
        return playerID;
    }

    public final void setPlayerID(int playerID) {
        this.playerID = playerID;
    }
}
