package org.gyming.tank.object;

import org.gyming.tank.client.ActionGroup;
import org.gyming.tank.connection.GameAction;

public class SupplyObject extends GameObject {
    public SupplyObject(double speed, double direction, double posX, double posY, int hp, ActionGroup actionGroup) {
        super(speed, direction, posX, posY, hp, actionGroup);
    }

    @Override
    public void fire(GameAction actions) {

    }
}
