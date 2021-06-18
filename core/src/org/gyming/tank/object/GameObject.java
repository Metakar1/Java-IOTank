package org.gyming.tank.object;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.gyming.tank.client.ColorPool;
import org.gyming.tank.client.MainScreen;
import org.gyming.tank.client.TankGame;
import org.gyming.tank.connection.GameAction;
import org.gyming.tank.connection.GameFrame;

abstract public class GameObject extends Actor {
    public static ColorPool colorPool = new ColorPool();
    protected static float acceleration = 5.0f / 30;
    public Rectangle area;
    public int dmg;
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

    public GameObject(float speed, float direction, float posX, float posY, int hp, TankGame game, Stage stage, Group[] group) {
        this.speed = speed;
        this.direction = direction;
        this.posX = posX;
        this.posY = posY;
        this.hp = hp;
        this.game = game;
        if (!(this instanceof PlayerObject))
            this.texture = createTexture();
        this.stage = stage;
        this.group = group;
        if (!(this instanceof PlayerObject))
            setSize(this.texture.getWidth(), this.texture.getHeight());
        this.area = new Rectangle();
        this.gunDirection = 0;
        this.dmg = 100;
    }

    // 创建一个纹理，即GameObject的图像
    protected abstract Texture createTexture();

    // 发射子弹
    protected abstract void fire(GameAction action, float posX, float posY);

    // 实现加速度的函数，让玩家移动更加平滑
    protected abstract void recoverSpeed();

    // 受到伤害时重绘为红色
    protected abstract void getDmg();

    // 特殊技能
    abstract public void qSkill(GameAction action, float posX, float posY);

    // 负责每个对象属性的修改，render 进行一次渲染就执行一次
    @Override
    public void act(float delta) {

        // 每一帧减少子弹的剩余存在时间，以此控制射程
        if (this instanceof BulletObject)
            ((BulletObject) this).bulletTime--;

        if (this instanceof PlayerObject) {
            // 接下来判定属性，alpha!=0表示该角色正在持续某种状态
            if (((PlayerObject) this).alpha != 0 && ((PlayerObject) this).playerType == 1) {
                int cutSize = ((PlayerObject) this).playerSize * ((PlayerObject) this).ratio;

                Pixmap pixmap = new Pixmap(cutSize * 2, cutSize * 2, Pixmap.Format.RGBA8888);

                pixmap.setColor(0, 0, 0, 0);
                Pixmap pixmap1 = new Pixmap(((PlayerObject) this).playerSize * 2, ((PlayerObject) this).playerSize * 2, Pixmap.Format.RGBA8888);
                pixmap1.drawPixmap(pixmap, 0, 0, pixmap.getWidth(), pixmap.getHeight(), 0, 0, pixmap1.getWidth(), pixmap1.getHeight());
                Texture texture = new Texture(pixmap1);
                texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                this.texture = texture;
                --((PlayerObject) this).alpha;
                if (((PlayerObject) this).alpha == 0) {
                    this.texture = createTexture();
                }
            }
            if (((PlayerObject) this).alpha != 0 && ((PlayerObject) this).playerType == 0) {
                ((PlayerObject) this).bulletSpeed = 15;
                ((PlayerObject) this).bulletHP = 20;
                ((PlayerObject) this).bulletATK = 15;
                ((PlayerObject) this).bulletSize = 15;
                ((PlayerObject) this).playerFireGap = 100;
                ((PlayerObject) this).bulletTime = 200;
                ((PlayerObject) this).playerSize = 30;
                ((PlayerObject) this).playerSpeed = 35;
                ((PlayerObject) this).playerHP = Math.min(200, ((PlayerObject) this).playerHP * 2);

                if (((PlayerObject) this).alpha == 400) {
                    this.texture = createTexture();
                }
                ((PlayerObject) this).alpha--;

                if (((PlayerObject) this).alpha == 0) {
                    ((PlayerObject) this).bulletSpeed = 10;
                    ((PlayerObject) this).bulletHP = 10;
                    ((PlayerObject) this).bulletATK = 10;
                    ((PlayerObject) this).bulletSize = 10;
                    ((PlayerObject) this).playerFireGap = 250;
                    ((PlayerObject) this).bulletTime = 100;
                    ((PlayerObject) this).playerSize = 30;
                    ((PlayerObject) this).playerSpeed = 25;
                    ((PlayerObject) this).playerHP = 100;
                    this.texture = createTexture();
                }
            }
        }

        if (this instanceof SupplyObject) {
            ((SupplyObject) this).selfdirect += ((SupplyObject) this).theta;
            if (MainScreen.dataMaker.nextFloat() < 0.01) {
                direction = MainScreen.dataMaker.nextFloat() * 2 * MathUtils.PI;
                speed = 0.1f;
            }
            if (((SupplyObject) this).alpha < 120) {
                float alpha = ((SupplyObject) this).alpha * 1f / 120f;
                ((SupplyObject) this).alpha++;
                Pixmap pixmap = new Pixmap(40, 40, Pixmap.Format.RGBA8888);
                pixmap.setColor(191f / 255f, 174f / 255f, 78f / 255f, alpha);
                pixmap.fillRectangle(0, 0, 40, 40);
                pixmap.setColor(255f / 255f, 232f / 255f, 105f / 255f, alpha);
                pixmap.fillRectangle(5, 5, 30, 30);
                this.texture = new Texture(pixmap);
            }
        }

        // 子弹沿其运动方向运动一帧
        posX += speed * MathUtils.sin(direction);
        posY += speed * MathUtils.cos(direction);

        // 边界判定
        if (this instanceof PlayerObject || this instanceof SupplyObject) {
            posX = Math.max(posX, MainScreen.boarder);
            posX = Math.min(posX, MainScreen.boarder + MainScreen.width);
            posY = Math.max(posY, MainScreen.boarder);
            posY = Math.min(posY, MainScreen.boarder + MainScreen.height);
        }
        area.set(posX, posY, texture.getWidth(), texture.getHeight());

        int flag = 1;
        GameFrame actions = game.actionGroup.modify.get(identifier);
        if (actions != null) {
            // 这里是处理由服务器发来的，所有玩家的操作
            for (GameAction i : actions.frameList) {
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
                        PlayerObject player = new PlayerObject(0, 0, i.getDirection(), i.getValue(), 0, i.getObjectID(), i.getProperty(), game, stage, group, 0);
                        System.out.println(game.getUserName());
                        System.out.println(i.getProperty());
                        if (game.getUserName().hashCode() == i.getProperty().hashCode())
                            MainScreen.MainPlayer = player;
                        group[1].addActor(player);
                        break;
                    case "Rotate":
                        gunDirection = ((i.getDirection() + MathUtils.PI) / MathUtils.PI2) * 360;
                        gunDirection = (360 - (gunDirection + 180.f) % 360.f) % 360.f;
                        break;
                    case "QSkill":
                        qSkill(i, posX, posY);
                        break;
                }
            }
            if (flag == 1) recoverSpeed();
            actions.frameList.clear();
        }
        if (this instanceof PlayerObject) {
            ((PlayerObject) this).hpProgress.setValue(this.hp);
        }
        if ((this instanceof PlayerObject) && (this.identifier == game.playerID)) {
            stage.getCamera().position.set(posX + texture.getWidth() / 2f, posY + texture.getHeight() / 2f, 0);

            if (getHp() <= 0) {
                game.setScreen(game.gameOverScreen);
            }
        }
        try {
            if (getHp() <= 0 || (!MainScreen.isInside(this) && (this instanceof BulletObject)) || ((this instanceof BulletObject) && ((BulletObject) this).bulletTime <= 0))
                game.toBeDeleted.put(this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // 在屏幕上画出该 GameObject

        if (this.getPlayerID() == 0 && this instanceof PlayerObject)
            return;

        this.getDmg();
        this.paint(batch, parentAlpha);
    }

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

    // 处理物体死亡
    public abstract void die();

    // 获取玩家 ID
    public abstract int getPlayerID();

    // 判断与 B 的碰撞情况
    public abstract void checkCollision(GameObject B);

    public abstract void paint(Batch batch, float parentAlpha);
}