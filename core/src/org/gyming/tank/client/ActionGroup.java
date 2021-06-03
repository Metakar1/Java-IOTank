package org.gyming.tank.client;

import org.gyming.tank.connection.GameFrame;

import java.util.HashMap;

public class ActionGroup {
    public HashMap<Integer, GameFrame> modify;

    public ActionGroup() {
        modify = new HashMap<>();
    }
}
