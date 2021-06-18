package org.gyming.tank.server;

public class ServerLauncher {
    public static void main(String[] args) {
        try {
            Server server = new Server(7650);
            server.startListen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
