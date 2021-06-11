package org.gyming.tank.object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import org.gyming.tank.client.ColorPool;
import org.gyming.tank.client.MainScreen;
import org.gyming.tank.client.TankGame;
import org.gyming.tank.connection.GameAction;
import org.gyming.tank.connection.GameFrame;

abstract public class GameObject extends Actor {
    protected static float acceleration = 5.0f / 30;
    public Rectangle area;
    protected int identifier;
    protected float speed;
    protected float direction;
    protected float posX, posY;
    protected int hp;
    protected TankGame game;
    protected Texture texture;
    protected Stage stage;
    protected float gunDirection;
    protected Group[] group;
    public static ColorPool colorPool = new ColorPool();
    public int dmg;


    public GameObject(float speed, float direction, float posX, float posY, int hp, TankGame game, Stage stage, Group[] group) {
        this.speed = speed;
        this.direction = direction;
        this.posX = posX;
        this.posY = posY;
        this.hp = hp;
        this.game = game;
        if(!(this instanceof PlayerObject)) this.texture = createTexture();
        this.stage = stage;
        this.group = group;
//        region = createRegion();
        if(!(this instanceof PlayerObject)) setSize(this.texture.getWidth(), this.texture.getHeight());
        this.area = new Rectangle();
        this.gunDirection = 0;
        this.dmg = 100;
    }

    protected abstract Texture createTexture();

    protected abstract void fire(GameAction action, float posX, float posY);

    protected abstract void recoverSpeed();

    protected abstract void getDmg();
    abstract public void QSkill(GameAction action, float posX, float posY);

    @Override
    public void act(float delta) {
        if(this instanceof BulletObject)
            ((BulletObject) this).bulletTime--;

        if(this instanceof PlayerObject){
            if(((PlayerObject)this).alpha!=0&&((PlayerObject)this).playerType==1)
            {
                int cutSize =((PlayerObject)this).playerSize*((PlayerObject)this).ratio;

                Pixmap pixmap = new Pixmap(cutSize * 2, cutSize * 2, Pixmap.Format.RGBA8888);

                pixmap.setColor(0,0,0,0);
                Pixmap pixmap1 = new Pixmap(((PlayerObject)this).playerSize*2,((PlayerObject)this).playerSize*2,Pixmap.Format.RGBA8888);
                pixmap1.drawPixmap(pixmap,0,0,pixmap.getWidth(),pixmap.getHeight(),0,0,pixmap1.getWidth(),pixmap1.getHeight());
                Texture texture = new Texture(pixmap1);
                texture.setFilter (Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
//        System.out.println(colorPool.getUserColor(playerID));
//        System.out.println("***");
//        texture
                this.texture = texture;
                --((PlayerObject)this).alpha;
                if(((PlayerObject)this).alpha==0)
                {
                    this.texture = createTexture();
                }
            }
            if(((PlayerObject)this).alpha!=0&&((PlayerObject)this).playerType==0)
            {
                ((PlayerObject)this).bulletSpeed = 15;
                ((PlayerObject)this).bulletHP = 20;
                ((PlayerObject)this).bulletATK = 15;
                ((PlayerObject)this).bulletSize = 15;
                ((PlayerObject)this).playerFireGap = 100;
                ((PlayerObject)this).bulletTime = 200;
                ((PlayerObject)this).playerSize = 30;
                ((PlayerObject)this).playerSpeed = 35;
                ((PlayerObject)this).playerHP = Math.min(200,((PlayerObject)this).playerHP*2);

                if(((PlayerObject)this).alpha==400)
                {
                    this.texture = createTexture();
                }
                ((PlayerObject)this).alpha--;

                if(((PlayerObject)this).alpha==0)
                {
                    ((PlayerObject)this).bulletSpeed = 10;
                    ((PlayerObject)this).bulletHP = 10;
                    ((PlayerObject)this).bulletATK = 10;
                    ((PlayerObject)this).bulletSize = 10;
                    ((PlayerObject)this).playerFireGap = 250;
                    ((PlayerObject)this).bulletTime = 100;
                    ((PlayerObject)this).playerSize = 30;
                    ((PlayerObject)this).playerSpeed = 25;
                    ((PlayerObject)this).playerHP = 100;
                    this.texture = createTexture();
                }
            }
        }

        if (this instanceof SupplyObject) {
            ((SupplyObject)this).selfdirect += ((SupplyObject)this).theta;
            if (MainScreen.dataMaker.nextFloat() < 0.01) {
                direction = MainScreen.dataMaker.nextFloat() * 2 * MathUtils.PI;
                speed = 0.1f;
            }
            if(((SupplyObject)this).alpha<120)
            {
                float alpha = ((SupplyObject)this).alpha*1f/120f;
                ((SupplyObject)this).alpha++;
                Pixmap pixmap = new Pixmap(40, 40, Pixmap.Format.RGBA8888);
                pixmap.setColor(191f / 255f, 174f / 255f, 78f / 255f, alpha);
                pixmap.fillRectangle(0, 0, 40, 40);
                pixmap.setColor(255f / 255f, 232f / 255f, 105f / 255f, alpha);
                pixmap.fillRectangle(5, 5, 30, 30);
                this.texture = new Texture(pixmap);
            }
            else
            {

            }
        }

        posX += speed * MathUtils.sin(direction);
        posY += speed * MathUtils.cos(direction);

        if (this instanceof PlayerObject || this instanceof SupplyObject) {
            posX = Math.max(posX, MainScreen.boarder);
            posX = Math.min(posX, MainScreen.boarder + MainScreen.width);
            posY = Math.max(posY, MainScreen.boarder);
            posY = Math.min(posY, MainScreen.boarder + MainScreen.height);
        }
        area.set(posX, posY, texture.getWidth(), texture.getHeight());

        int flag = 1;
        GameFrame actions = game.actionGroup.modify.get(identifier);
//        game.actionGroup.modify.put(identifier,null);
        if (actions != null) {
            for (GameAction i : actions.frameList) {
//                System.out.println(i.getType());
//                System.out.println(identifier);
                switch (i.getType()) {
                    case "Move":
                        flag = 0;
                        direction = i.getDirection();
                        if (i.getValue() > speed) {
                            speed = Math.min(speed + acceleration, i.getValue());
                        }
                        else {
                            speed = Math.max(speed - acceleration, i.getValue());
                        }
                        break;
                    case "Fire":
                        fire(i, posX, posY);
                        break;
                    case "NewPlayer":
//                    System.out.println();
                        PlayerObject player = new PlayerObject(0, 0, i.getDirection(), i.getValue(), 0, i.getObjectID(), i.getProperty(), game, stage, group,0);
                        System.out.println(game.getUserName());
                        System.out.println(i.getProperty());
                        if(game.getUserName().hashCode()==i.getProperty().hashCode())
                            MainScreen.MainPlayer = player;
                        group[1].addActor(player);
                        break;
                    case "Rotate":
                        gunDirection = ((i.getDirection() + MathUtils.PI) / MathUtils.PI2) * 360;
                        gunDirection = (360 - (gunDirection + 180.f) % 360.f) % 360.f;
//                        System.out.println(gunDirection);
                        break;
                    case "QSkill":
                        QSkill(i, posX, posY);
                        break;
                }
            }
            if (flag == 1) recoverSpeed();
            actions.frameList.clear();
        }
        if(this instanceof PlayerObject) {
            ((PlayerObject) this).hpProgress.setValue(this.hp);
        }
        if ((this instanceof PlayerObject) && (this.identifier == game.playerID)) {
            stage.getCamera().position.set(posX + texture.getWidth() / 2f, posY + texture.getHeight() / 2f, 0);

            if (getHp() <= 0) {
                game.setScreen(game.gameOverScreen);
            }
        }
        try {
            if (getHp() <= 0 || (!MainScreen.isInside(this) && (this instanceof BulletObject))||((this instanceof BulletObject) && ((BulletObject) this).bulletTime<=0))
                game.toBeDeleted.put(this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
//        System.out.println(t);
        if(this.getPlayerID()==0&&this instanceof PlayerObject)
            return;

        this.getDmg();

        if (this instanceof PlayerObject) {
            batch.draw(texture, posX, posY, ((PlayerObject)this).cirR + 1, texture.getHeight() - (((PlayerObject)this).cirR + ((PlayerObject)this).gunHeight - PlayerObject.boarder * 2) - 1, texture.getWidth(), texture.getHeight(), this.dmg*1.0f/100f, this.dmg*1.0f/100f, gunDirection, 0, 0, texture.getWidth(), texture.getHeight(), false, false);
            ((PlayerObject) this).hpProgress.setPosition(posX-5,posY-((PlayerObject) this).hpProgress.getHeight());
            if(((PlayerObject) this).alpha==0)
                ((PlayerObject) this).hpProgress.draw(batch,parentAlpha);
//            System.out.println(((PlayerObject)this).cirR + 1);
        }
        else {
            if(this instanceof BulletObject)
                batch.draw(texture, posX, posY, texture.getWidth() / 2f, texture.getHeight() / 2f, texture.getWidth(), texture.getHeight(), 1, 1, 180, 0, 0, texture.getWidth(), texture.getHeight(), false, false);
            else
                batch.draw(texture, posX, posY, texture.getWidth() / 2f, texture.getHeight() / 2f, texture.getWidth(), texture.getHeight(), this.dmg*1.0f/100f, this.dmg*1.0f/100f, ((SupplyObject)this).selfdirect, 0, 0, texture.getWidth(), texture.getHeight(), false, false);
        }

//        batch.draw(texture, (float) posX, (float) posY);
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