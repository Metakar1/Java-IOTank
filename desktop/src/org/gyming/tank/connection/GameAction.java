package org.gyming.tank.connection;

public class GameAction {
    String type;
    double direction;
    int objectID;
    String property;
    double value;

    public GameAction(String type, double direction, int objectID, String property, double value) {
        this.type = type;
        this.direction = direction;
        this.objectID = objectID;
        this.property = property;
        this.value = value;
    }
}
