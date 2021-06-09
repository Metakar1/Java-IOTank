package org.gyming.tank.object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import org.gyming.tank.client.MainScreen;
import org.gyming.tank.client.TankGame;
import org.gyming.tank.connection.GameAction;

public class SupplyObject extends GameObject {

    public int alpha;
    public float theta;
    public float selfdirect;
    public SupplyObject(float speed, float direction, float posX, float posY, int hp, TankGame game, Stage stage,Group[] group,float theta) {
        super(speed, direction, posX, posY, hp, game, stage, group);
        this.theta = theta;
//        this.addAction();
//        this.addAction(Actions.alpha(0f,5f))
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

    @Override
    protected void getDmg(){
        if (dmg>100) {
            Pixmap pixmap = new Pixmap(40, 40, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.RED);
            pixmap.fillRectangle(0, 0, 40, 40);
            pixmap.setColor(Color.RED);
            pixmap.fillRectangle(5, 5, 30, 30);
            texture = new Texture(pixmap);
            dmg -= 2;
//            System.out.println(dmg);
            if(dmg==100)
            {
                texture = createTexture();
            }
        }
    }


    public void die() {
        stage.getRoot().removeActor(this);
        this.setHp(0);
        group[1].removeActor(this);
        group[0].removeActor(this);
        MainScreen.supplies--;
//        System.out.println(MainScreen.supplies);
    }

}
