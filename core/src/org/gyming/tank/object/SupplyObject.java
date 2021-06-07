package org.gyming.tank.object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.gyming.tank.client.MainScreen;
import org.gyming.tank.client.TankGame;
import org.gyming.tank.connection.GameAction;

public class SupplyObject extends GameObject {
    public SupplyObject(float speed, float direction, float posX, float posY, int hp, TankGame game, Stage stage,Group[] group) {
        super(speed, direction, posX, posY, hp, game, stage, group);
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
        return;
    }

    @Override
    protected void recoverSpeed() {

    }

    public void die() {
        stage.getRoot().removeActor(this);
        this.setHp(0);
        group[1].removeActor(this);
        group[0].removeActor(this);
        MainScreen.supplies--;
        System.out.println(MainScreen.supplies);
    }

}
