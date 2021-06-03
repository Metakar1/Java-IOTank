package org.gyming.tank.object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.gyming.tank.client.ColorPool;
import org.gyming.tank.client.TankGame;
import org.gyming.tank.connection.GameAction;
import org.gyming.tank.connection.GameFrame;

abstract public class GameObject extends Actor {
    protected static ColorPool colorPool = new ColorPool();
    protected int identifier;
    protected float speed;
    protected float direction;
    protected float posX, posY;
    protected int hp;
    protected TankGame game;
    protected Texture texture;
    protected Stage stage;
    public Rectangle area;


    public GameObject(float speed, float direction, float posX, float posY, int hp, TankGame game, Stage stage) {
        this.speed = speed;
        this.direction = direction;
        this.posX = posX;
        this.posY = posY;
        this.hp = hp;
        this.game = game;
        texture = createTexture();
        this.stage = stage;
//        region = createRegion();
        setSize(this.texture.getWidth(), this.texture.getHeight());
        this.area = new Rectangle();
    }

    static public Texture drawCircle(int r, Color color) {
        Pixmap pixmap = new Pixmap(r * 2, r * 2, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillCircle(r, r, r);
        Texture texture = new Texture(pixmap);
        return texture;
    }

    protected abstract Texture createTexture();

    protected abstract void fire(GameAction action, float posX, float posY);

    protected abstract void recoverSpeed();

    @Override
    public void act(float delta) {
        posX += speed * MathUtils.sin(direction);
        posY += speed * MathUtils.cos(direction);

        area.set(posX,posY,texture.getWidth(),texture.getHeight());

        recoverSpeed();
        GameFrame actions = game.actionGroup.modify.get(identifier);
        if (actions != null) {
            for (GameAction i : actions.frameList) {
//                System.out.println(i.getType());
//                System.out.println(identifier);
                switch (i.getType()) {
                    case "Move":
                        direction = i.getDirection();
                        speed = i.getValue();
                        break;
                    case "Fire":
                        fire(i, posX, posY);
                        break;
                    case "NewPlayer":
//                    System.out.println();
                        PlayerObject player = new PlayerObject(0, 0, i.getDirection(), i.getValue(), PlayerObject.playerHP, i.getProperty().hashCode(), i.getProperty(), game, stage);
                        stage.addActor(player);
                        break;
                }
            }
            actions.frameList.clear();
        }
        if ((this instanceof PlayerObject) && (this.identifier == game.playerID))
            stage.getCamera().position.set(posX, posY, 0);
        try {
            if(getHp()<=0)
                game.ToBeDeleted.put(this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
//        System.out.println(t);
        batch.draw(texture, (float) posX, (float) posY);
    }

//    abstract public Texture createTexture();

//    abstract public TextureRegion createRegion();

    public final float getPosX() {
        return posX;
    }

    public final void setPosX(float posX) {
        this.posX = posX;
    }

    public final float getPosY() {
        return posY;
    }

    public final void setPosY(float posY) {
        this.posY = posY;
    }

    public final float getSpeed() {
        return speed;
    }

    public final void setSpeed(float speed) {
        this.speed = speed;
    }

    public final float getDirection() {
        return direction;
    }

    public final void setDirection(float direction) {
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

    public abstract void die();
    public abstract int getPlayerID();
}