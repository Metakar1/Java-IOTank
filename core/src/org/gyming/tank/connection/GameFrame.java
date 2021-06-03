package org.gyming.tank.connection;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class GameFrame {
    public int id;
    public List<GameAction> frameList;

    public GameFrame(int _id) {
        id = _id;
        frameList = new Vector<>();
    }

    public void add(GameAction gameAction) {
        frameList.add(gameAction);
    }
}
