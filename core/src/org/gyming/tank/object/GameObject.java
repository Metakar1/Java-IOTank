package org.gyming.tank.object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.gyming.tank.client.ActionGroup;
import org.gyming.tank.client.ColorPool;
import org.gyming.tank.connection.GameAction;
import org.gyming.tank.connection.GameFrame;

abstract public class GameObject extends Actor {
    protected int identifier;
    protected double speed;
    protected double direction;
    protected double posX, posY;
    protected int hp;
    protected ActionGroup actionGroup;
    protected Texture texture;
    protected static ColorPool colorPool = new ColorPool();
    protected Stage stage;

    public GameObject(double speed, double direction, double posX, double posY, int hp, ActionGroup actionGroup, Stage stage) {
        this.speed = speed;
        this.direction = direction;
        this.posX = posX;
        this.posY = posY;
        this.hp = hp;
        this.actionGroup = actionGroup;
        texture = createTexture();
        this.stage = stage;
//        region = createRegion();
        setSize(this.texture.getWidth(), this.texture.getHeight());
    }

    protected abstract Texture createTexture();
    protected abstract void fire(GameAction action, double posX, double posY);

    @Override
    public void act(float delta) {
        posX+=speed*Math.sin(direction);
        posY+=speed*Math.cos(direction);
        GameFrame actions = actionGroup.modify.get(identifier);
        if(actions!=null) {
            for(GameAction i:actions.frameList) {
//                System.out.println(i.getType());
//                System.out.println(i.getObjectID());
                if(i.getType().equals("Move")) {
                    direction = i.getDirection();
                    speed = i.getValue();
                } else if(i.getType().equals("Fire")) {
                    fire(i,posX,posY);
                } else if(i.getType().equals("NewPlayer")) {
                    System.out.println();
                    PlayerObject player = new PlayerObject(PlayerObject.playerSpeed,0,i.getDirection(),i.getValue(),PlayerObject.playerHP,i.getProperty().hashCode(),i.getProperty(),actionGroup,stage);
                    stage.addActor(player);
                }
            }
            actions.frameList.clear();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
//        System.out.println(t);
        batch.draw(texture,(float) posX,(float) posY);
    }

    static public Texture drawCircle(int r,Color color) {
        Pixmap pixmap = new Pixmap(r*2,r*2,Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillCircle(r,r,r);
        Texture texture = new Texture(pixmap);
        return texture;
    }

//    abstract public Texture createTexture();

//    abstract public TextureRegion createRegion();

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