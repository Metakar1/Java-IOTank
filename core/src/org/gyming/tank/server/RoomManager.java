package org.gyming.tank.server;

import org.gyming.tank.connection.ConnectMsg;
import org.gyming.tank.connection.GameFrame;
import org.gyming.tank.connection.User;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

public class RoomManager implements Runnable {
    HashMap<String, Room> roomHashMap;
    ExecutorService threadPool;
    LinkedBlockingQueue<RoomInfo> queue;

    RoomManager(ExecutorService _threadPool) {
        threadPool = _threadPool;
        queue = new LinkedBlockingQueue<>();
        roomHashMap = new HashMap<>();
    }

    public void add(ConnectMsg connectMsg, User user) {
        if (roomHashMap.get(connectMsg.room) == null) {
            Room room = new Room(connectMsg.room);
            roomHashMap.put(connectMsg.room, room);
            threadPool.submit(room);
            System.out.println("QWQ");
        }
        Room room = roomHashMap.get(connectMsg.room);
        Client client = new Client(connectMsg, user);
        threadPool.submit(client.receive);
        threadPool.submit(client.send);
        for(GameFrame gameFrame:room.totFrame) {
            client.download.offer(gameFrame);
        }
        room.clients.offer(client);
    }

    @Override
    public void run() {
        while (true) {
            if (!queue.isEmpty()) {
                add(queue.peek().connectMsg, queue.peek().user);
                queue.poll();
            }
        }
    }
}
