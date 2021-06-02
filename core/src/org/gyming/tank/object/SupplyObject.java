package org.gyming.tank.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.gyming.tank.client.ActionGroup;
import org.gyming.tank.connection.GameAction;

public class SupplyObject extends GameObject {
    public SupplyObject(double speed, double direction, double posX, double posY, int hp, ActionGroup actionGroup, Stage stage) {
        super(speed, direction, posX, posY, hp, actionGroup, stage);
    }

    @Override
    protected Texture createTexture() {
        return null;
    }

    @Override
    protected void fire(GameAction action, double posX, double posY) {

    }
}
