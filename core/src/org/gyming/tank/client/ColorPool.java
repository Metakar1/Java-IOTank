package org.gyming.tank.client;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class ColorPool {
    ArrayList<Color> userColorPool;
    ArrayList<Color> userBoarderColorPool;
    HashMap<Integer, Integer> user;
    int userCnt;

    public ColorPool() {
        user = new HashMap<>();
        userColorPool = new ArrayList<>();
        userBoarderColorPool = new ArrayList<>();
//        userColorPool.add(new Color(191f / 255f, 127f / 255f, 145f / 255f, 1f));
        userColorPool.add(new Color(0f / 255f, 178f / 255f, 225f / 255f, 1f));
        userColorPool.add(revert(241, 78, 84));
        userColorPool.add(new Color(241f / 255f, 78f / 255f, 84f / 255f, 1f));
        userColorPool.add(new Color(0, 225f / 255f, 110f / 255f, 1f));
        userBoarderColorPool.add(revert(0, 133, 169));
        userBoarderColorPool.add(revert(180, 58, 63));

        userCnt = 0;
    }

    Color revert(int r, int g, int b) {
        return new Color(r / 255f, g / 255f, b / 255f, 1);
    }

    public Color getUserColor(Integer userID) {
        if (user.get(userID) == null) {
            user.put(userID, userCnt);
            userCnt++;
        }
        return userColorPool.get(user.get(userID));
    }

    public Color getUserBoarderColor(Integer userID) {
        if (user.get(userID) == null) {
            user.put(userID, userCnt);
            userCnt++;
        }
        return userBoarderColorPool.get(userID);
    }

    public Color getGunBoarderColor() {
        return revert(114, 114, 114);
    }

    public Color getGunColor() {
        return revert(183, 183, 183);
    }
}
