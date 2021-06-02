package org.gyming.tank.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.gyming.tank.client.ActionGroup;
import org.gyming.tank.connection.GameAction;

abstract public class GameObject extends Actor {
    protected int identifier;
    protected double speed;
    protected double direction;
    protected double posX, posY;
    protected int hp;
    protected ActionGroup actionGroup;
    protected TextureRegion region;
    protected Texture texture;

    public GameObject(double speed, double direction, double posX, double posY, int hp, ActionGroup actionGroup) {
        this.speed = speed;
        this.direction = direction;
        this.posX = posX;
        this.posY = posY;
        this.hp = hp;
        this.actionGroup = actionGroup;
        texture = createTexture();
        region = createRegion();
        setSize(this.region.getRegionWidth(), this.region.getRegionHeight());
    }

    abstract public void act(float delta) ;

    abstract public void reactAction(GameAction action);

    abstract public Texture createTexture();

    abstract public TextureRegion createRegion();

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

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }
}
