package org.gyming.tank.object;

public class PlayerObject extends GameObject {
    private int playerID;
    private String playerName;

    public PlayerObject(double speed, double direction, double posX, double posY, int hp, int playerID, String playerName) {
        super(speed, direction, posX, posY, hp);
        this.playerID = playerID;
        this.playerName = playerName;
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
}
