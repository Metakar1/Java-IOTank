package org.gyming.tank.client;

import com.google.gson.Gson;
import org.gyming.tank.connection.GameFrame;
import org.gyming.tank.connection.MsgIO;

public class ClientDownloader implements Runnable {
    TankGame game;
    MsgIO S2C;
    int frameCnt = 0;

    public ClientDownloader(TankGame game_, MsgIO S2C_) {
        game = game_;
        S2C = S2C_;
    }
    int cnt=0;
    public void run() {
        GameFrame g;

        while (true) {
            try {
                Gson gson = new Gson();
                g = gson.fromJson(S2C.receive(), GameFrame.class);
//                if (g != null)
//                    System.out.println(g.frameList.size());
                if (!g.frameList.isEmpty()) {
                    frameCnt++;
                    System.out.println("FRAME CNT: " + frameCnt);
                }
                game.download.offer(g);
                if(!g.frameList.isEmpty())  {
                    cnt++;
                    System.out.println(cnt);
                }

//                GameFrame gg = game.download.peek();
//                while(g==null) {
//                    g = game.download.peek();
//                }
//                game.download.poll();
//                Gdx.graphics.requestRendering();
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
