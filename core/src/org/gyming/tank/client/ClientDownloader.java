package org.gyming.tank.client;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import org.gyming.tank.connection.GameFrame;
import org.gyming.tank.connection.MsgIO;

public class ClientDownloader implements Runnable {
    TankGame game;
    MsgIO S2C;

    public ClientDownloader(TankGame game_, MsgIO S2C_) {
        game = game_;
        S2C = S2C_;
    }

    public void run() {
        GameFrame g;

        while (true) {
            try {
                Gson gson = new Gson();
                g = gson.fromJson(S2C.receive(), GameFrame.class);
                game.download.offer(g);
                Gdx.graphics.requestRendering();
            }
            catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
