package org.gyming.tank.client;

import org.gyming.tank.connection.GameAction;
import org.gyming.tank.connection.GameFrame;

public class ClientPainter implements Runnable {
    TankGame game;

    public ClientPainter(TankGame game_) {
        game = game_;
    }

    void Paint(GameFrame g) {
        for (GameAction data : g.frameList) {
            TankGame.test2++;
        }
    }

    public void run() {
        while (true) {
            if (!game.download.isEmpty()) {
                Paint(game.download.peek());
                game.download.poll();
            }
        }
    }
}
