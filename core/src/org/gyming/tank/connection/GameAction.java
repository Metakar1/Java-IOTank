package org.gyming.tank.connection;


//表示一个玩家的一次操作
public class GameAction {
    String type;
    float direction;
    int objectID;
    String property;
    float value;

    public GameAction(String type, float direction, int objectID, String property, float value) {
        this.type = type;
        this.direction = direction;
        this.objectID = objectID;
        this.property = property;
        this.value = value;
    }

    public int getObjectID() {
        return objectID;
    }

    public String getType() {
        return type;
    }

    public float getDirection() {
        return direction;
    }

    public float getValue() {
        return value;
    }

    public String getProperty() {
        return property;
    }
}
