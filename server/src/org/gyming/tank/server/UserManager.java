package org.gyming.tank.server;

import org.gyming.tank.connection.MsgIO;
import org.gyming.tank.connection.User;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

public class UserManager implements Runnable {
    HashMap<String, MsgIO> socketHashMap;
    ExecutorService threadPool;
    RoomManager roomManager;
    LinkedBlockingQueue<Socket> queue;

    UserManager(ExecutorService _threadPool, RoomManager _roomManager) {
        threadPool = _threadPool;
        roomManager = _roomManager;
        socketHashMap = new HashMap<>();
        queue = new LinkedBlockingQueue<>();
    }

    void add(Socket socket) throws IOException {
        MsgIO msgIO = new MsgIO(socket);
        String name = msgIO.receive();
        MsgIO cur = socketHashMap.get(name);
        if (cur != null) {
            threadPool.submit(new ParseConnectAction(new User(cur, msgIO), roomManager, threadPool));
            socketHashMap.put(name,null);
        }
        else {
            socketHashMap.put(name, msgIO);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (!queue.isEmpty()) {
                    add(queue.peek());
                    queue.poll();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
