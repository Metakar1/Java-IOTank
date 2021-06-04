package org.gyming.tank.client;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import org.gyming.tank.connection.GameAction;
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
//                GameFrame gg = game.download.peek();
//                while(g==null) {
//                    g = game.download.peek();
//                }
//                game.download.poll();
                Gdx.graphics.requestRendering();
//                if (g.frameList.size() != 0) {
//                    System.out.print(TankGame.test1);
//                    System.out.print("        ");
//                    System.out.println(TankGame.test2);
//                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
