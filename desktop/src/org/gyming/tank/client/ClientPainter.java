package org.gyming.tank.client;

import org.gyming.tank.connection.GameAction;
import org.gyming.tank.connection.GameFrame;

public class ClientPainter implements Runnable {
    Client client;

    public ClientPainter(Client client_) {
        client = client_;
    }

    void Paint(GameFrame g) {
        for (GameAction data : g.frameList) {
            Client.test2++;
        }
    }

    public void run() {
        while (true) {
            if (!client.download.isEmpty()) {
                Paint(client.download.peek());
                client.download.poll();
            }
        }
    }
}
