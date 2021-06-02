package org.gyming.tank.test;

import org.gyming.tank.server.Server;

public class RunServer {
    public static void main(String[] args) {
        try {
            Server server = new Server(7650);
            server.startListen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
