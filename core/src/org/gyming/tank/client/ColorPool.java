package org.gyming.tank.client;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class ColorPool {
    ArrayList<Color> userColorPool;
    HashMap<Integer, Color> user;
    int userCnt;

    public ColorPool() {
        userColorPool = new ArrayList<>();
        userColorPool.add(new Color(191, 127, 145, 1));
        userColorPool.add(new Color(0, 178, 225, 1));
        userColorPool.add(new Color(241, 78, 84, 1));
        userColorPool.add(new Color(0, 225, 110, 1));
    }

    public Color getUserColor(Integer userId) {
        if (user.get(userId) == null) {
            user.put(userId, userColorPool.get(userCnt));
            userCnt++;
        }
        return user.get(userId);
    }
}
