package org.gyming.tank.object;

public class GameObject {
    protected double speed;
    protected double direction;
    protected double posX, posY;
    protected int hp;

    public GameObject(double speed, double direction, double posX, double posY, int hp) {
        this.speed = speed;
        this.direction = direction;
        this.posX = posX;
        this.posY = posY;
        this.hp = hp;
    }

    public final double getPosX() {
        return posX;
    }

    public final void setPosX(double posX) {
        this.posX = posX;
    }

    public final double getPosY() {
        return posY;
    }

    public final void setPosY(double posY) {
        this.posY = posY;
    }

    public final double getSpeed() {
        return speed;
    }

    public final void setSpeed(double speed) {
        this.speed = speed;
    }

    public final double getDirection() {
        return direction;
    }

    public final void setDirection(double direction) {
        this.direction = direction;
    }

    public final int getHp() {
        return hp;
    }

    public final void setHp(int hp) {
        this.hp = hp;
    }
}
