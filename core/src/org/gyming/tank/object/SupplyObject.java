package org.gyming.tank.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.gyming.tank.client.TankGame;
import org.gyming.tank.connection.GameAction;

public class SupplyObject extends GameObject {

    public final int getPlayerID() {
        return -1;
    }

    public SupplyObject(float speed, float direction, float posX, float posY, int hp, TankGame game, Stage stage) {
        super(speed, direction, posX, posY, hp, game, stage);
    }

    @Override
    protected Texture createTexture() {
        return null;
    }

    @Override
    protected void fire(GameAction action, float posX, float posY) {
        return;
    }

    @Override
    protected void recoverSpeed() {

    }

    public void die()
    {
        stage.getRoot().removeActor(this);
    }
}
