package org.gyming.tank.object;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.gyming.tank.client.TankGame;
import org.gyming.tank.connection.GameAction;

public class BulletObject extends GameObject {
    public int bulletSpeed = 10;
    public int bulletHP = 10;
    public int bulletSize;
    public int bulletATK;
    public int bulletTime;
    private int playerID;

    public BulletObject(float speed, float direction, float posX, float posY, int hp, int playerID, TankGame game, Stage stage, Group[] group, int bulletSize, int bulletATK, int bulletTime) {
        super(speed, direction, posX, posY, hp, game, stage, group);
        this.playerID = playerID;

        this.posX -= texture.getWidth() / 2.0f;
        this.posY -= texture.getHeight() / 2.0f;
        this.bulletATK = bulletATK;
        this.bulletSize = bulletSize;
        this.bulletTime = bulletTime;
        texture = createTexture();
    }

    public final int getPlayerID() {
        return playerID;
    }

    public final void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    @Override
    protected Texture createTexture() {
        Pixmap pixmap = new Pixmap(bulletSize * 2, bulletSize * 2, Pixmap.Format.RGBA8888);
        pixmap.setColor(colorPool.getUserBoarderColor(playerID));
        pixmap.fillCircle(bulletSize, bulletSize, bulletSize);
        pixmap.setColor(colorPool.getUserColor(playerID));
        pixmap.fillCircle(bulletSize, bulletSize, bulletSize - 3);
        Texture texture = new Texture(pixmap);
        System.out.println(this.bulletSize);
        return texture;
    }

    @Override
    public void fire(GameAction action, float posX, float posY) {

    }

    @Override
    protected void recoverSpeed() {

    }

    @Override
    protected void getDmg() {
    }

    @Override
    public void qSkill(GameAction action, float posX, float posY) {
    }

    public void die() {
        stage.getRoot().removeActor(this);
        group[1].removeActor(this);
        group[0].removeActor(this);
        this.setHp(0);
    }

    public void CheckCollision(GameObject B) {
        if(this.getHp()==0||B.getHp()==0)
            return;
        if (this.getPlayerID() == B.getPlayerID())
            return;

        if (B instanceof BulletObject) {
            B.setHp(B.getHp()-this.bulletATK);
            this.setHp(this.getHp()-((BulletObject) B).bulletATK);
        }
        else if (B instanceof PlayerObject || B instanceof SupplyObject) {
            this.setHp(this.getHp()-10);
            B.setDirection((float) (Math.PI * 2 - B.getDirection()));
            B.setHp(B.getHp() - this.bulletATK);
            B.dmg = 110;

            if (B.getHp() <= 0) {
                if (this.getPlayerID() == game.playerID) {
                    if (game.playerMP < 10)
                        game.playerMP++;
                }
            }
        }
    }

    public void paint(Batch batch,float parentAlpha) {
        batch.draw(texture, posX, posY, texture.getWidth() / 2f, texture.getHeight() / 2f,
                texture.getWidth(), texture.getHeight(), 1, 1, 180, 0, 0,
                texture.getWidth(), texture.getHeight(), false, false);

    }


}