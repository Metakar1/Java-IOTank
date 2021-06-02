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
        userColorPool.add(new Color((float) (191.0/255.0), (float)(127.0/255.0), (float)(145.0/255.0), 1));
        userColorPool.add(new Color((float)(0/255.0), (float)(178/255.0), (float)(225/255.0), 1));
        userColorPool.add(new Color((float)(241/255.0), (float)(78/255.0), (float)(84/255.0), 1));
        userColorPool.add(new Color(0, (float)(225/255.0), (float)(110/255.0), 1));
        userCnt = 0;
    }

    public Color getUserColor(Integer userId) {
        if (user.get(userId) == null) {
            user.put(userId, userColorPool.get(userCnt));
            userCnt++;
        }
        return user.get(userId);
    }
}
