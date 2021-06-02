package org.gyming.tank.connection;

import java.util.ArrayList;
import java.util.List;

public class GameFrame {
    public int id;
    public List<GameAction> frameList;

    public GameFrame(int _id) {
        id = _id;
        frameList = new ArrayList<>();
    }

    public void add(GameAction gameAction) {
        frameList.add(gameAction);
    }
}
