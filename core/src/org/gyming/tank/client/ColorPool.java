package org.gyming.tank.client;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class ColorPool {
    ArrayList<Color> userColorPool;
    HashMap<Integer, Color> user;
    int userCnt;

    public ColorPool() {
        user = new HashMap<>();
        userColorPool = new ArrayList<>();
        userColorPool.add(new Color(191f / 255f, 127f / 255f, 145f / 255f, 1f));
        userColorPool.add(new Color(0f / 255f, 178f / 255f, 225f / 255f, 1f));
        userColorPool.add(new Color(241f / 255f, 78f / 255f, 84f / 255f, 1f));
        userColorPool.add(new Color(0, 225f / 255f, 110f / 255f, 1f));
        userCnt = 0;
    }

    public Color getUserColor(Integer userID) {
        if (user.get(userID) == null) {
            user.put(userID, userColorPool.get(userCnt));
            userCnt++;
        }
        return user.get(userID);
    }
}
