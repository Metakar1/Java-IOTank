package org.gyming.tank.connection;

import java.util.List;
import java.util.Vector;
//表示一帧中所有操作的集合
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
