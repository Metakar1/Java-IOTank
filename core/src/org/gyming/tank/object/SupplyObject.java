package org.gyming.tank.object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.gyming.tank.client.MainScreen;
import org.gyming.tank.client.TankGame;
import org.gyming.tank.connection.GameAction;

public class SupplyObject extends GameObject {
    public int alpha;
    public float theta;
    public float selfdirect;

    public SupplyObject(float speed, float direction, float posX, float posY, int hp, TankGame game, Stage stage, Group[] group, float theta) {
        super(speed, direction, posX, posY, hp, game, stage, group);
        this.theta = theta;
    }

    public final int getPlayerID() {
        return -1;
    }

    @Override
    protected Texture createTexture() {
        Pixmap pixmap = new Pixmap(40, 40, Pixmap.Format.RGBA8888);
        pixmap.setColor(191f / 255f, 174f / 255f, 78f / 255f, 1f);
        pixmap.fillRectangle(0, 0, 40, 40);
        pixmap.setColor(255f / 255f, 232f / 255f, 105f / 255f, 1f);
        pixmap.fillRectangle(5, 5, 30, 30);
        return new Texture(pixmap);
    }

    @Override
    protected void fire(GameAction action, float posX, float posY) {
    }

    @Override
    protected void recoverSpeed() {
    }

    @Override
    protected void getDmg() {
        if (dmg > 100) {
            Pixmap pixmap = new Pixmap(40, 40, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.RED);
            pixmap.fillRectangle(0, 0, 40, 40);
            pixmap.setColor(Color.RED);
            pixmap.fillRectangle(5, 5, 30, 30);
            texture = new Texture(pixmap);
            dmg -= 2;
            if (dmg == 100) {
                texture = createTexture();
            }
        }
    }

    @Override
    public void qSkill(GameAction action, float posX, float posY) {
    }

    public void die() {
        stage.getRoot().removeActor(this);
        this.setHp(0);
        group[1].removeActor(this);
        group[0].removeActor(this);
        MainScreen.supplies--;
    }

    public void checkCollision(GameObject B) {
        if (this.getHp() <= 0 || B.getHp() <= 0)
            return;
        if (this.getPlayerID() == B.getPlayerID())
            return;

        this.setDirection((float) (Math.PI * 2 - this.getDirection()));

        if (B instanceof BulletObject) {
            this.dmg = 110;
            this.setHp(this.getHp() - ((BulletObject) B).bulletATK);
            B.setHp(0);
            if (this.getHp() <= 0) {
                if (B.getPlayerID() == game.playerID) {
                    if (game.playerMP < 10)
                        game.playerMP++;
                }
            }
        }
        else if (B instanceof PlayerObject || B instanceof SupplyObject) {

            if (B instanceof PlayerObject) {
                B.dmg = 110;
                this.dmg = 110;
            }
            if (B instanceof PlayerObject) {
                this.setHp(this.getHp() - 5);
                B.setHp(B.getHp() - 5);
            }
            B.setDirection((float) (Math.PI * 2 - B.getDirection()));
            if (this.getHp() <= 0) {
                if (B instanceof PlayerObject) {
                    if (B.getPlayerID() == game.playerID) {
                        if (game.playerMP < 10)
                            game.playerMP++;
                    }
                }
            }
        }
    }

    public void paint(Batch batch, float parentAlpha) {
        batch.draw(texture, posX, posY, texture.getWidth() / 2f, texture.getHeight() / 2f,
                texture.getWidth(), texture.getHeight(), this.dmg * 1.0f / 100f, this.dmg * 1.0f / 100f,
                this.selfdirect, 0, 0, texture.getWidth(), texture.getHeight(), false, false);
    }
}
