package org.gyming.tank.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    ServerSocket serverSocket;
    Socket socket;
    ExecutorService threadPool;
    RoomManager roomManager;
    UserManager userManager;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        threadPool = Executors.newFixedThreadPool(50);
        roomManager = new RoomManager(threadPool);
        threadPool.submit(roomManager);
        userManager = new UserManager(threadPool, roomManager);
        threadPool.submit(userManager);
    }

    public void startListen() throws IOException {
        while (true) {
            socket = serverSocket.accept();
            userManager.queue.offer(socket);
        }
    }
}