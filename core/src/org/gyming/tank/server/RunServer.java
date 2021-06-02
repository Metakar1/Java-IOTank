package org.gyming.tank.server;

import java.io.IOException;

public class RunServer {
    public static void main (String[] arg) {
        Server server = null;
        try {
            server = new Server(7650);
            server.startListen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
