package org.gyming.tank.client;

import org.gyming.tank.connection.MsgIO;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientListener implements Runnable {
    TankGame game;
    MsgIO C2S;
    LinkedBlockingQueue<String> queue;

    public ClientListener(TankGame game, MsgIO C2S, LinkedBlockingQueue<String> queue) {
        this.game = game;
        this.C2S = C2S;
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            if (!queue.isEmpty()) {
                try {
                    C2S.send(queue.peek());
                }
                catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
                queue.poll();
            }
        }
    }
}
