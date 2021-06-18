package org.gyming.tank.server;

import com.google.gson.Gson;
import org.gyming.tank.connection.ConnectMsg;
import org.gyming.tank.connection.User;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

class ParseConnectAction implements Runnable {
    User user;
    RoomManager roomManager;
    ExecutorService threadPool;

    ParseConnectAction(User _user, RoomManager _roomManager, ExecutorService _threadPool) {
        user = _user;
        roomManager = _roomManager;
        threadPool = _threadPool;
    }

    @Override
    public void run() {
        try {
            Gson gson = new Gson();
            ConnectMsg connectMsg = gson.fromJson(user.C2S.receive(), ConnectMsg.class);
            if (connectMsg.type.equals("join")) {
                roomManager.queue.offer(new RoomInfo(connectMsg, user));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}