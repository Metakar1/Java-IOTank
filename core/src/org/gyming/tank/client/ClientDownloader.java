package org.gyming.tank.client;

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
                for (GameAction i : g.frameList) {
                    if (game.actionGroup.modify.get(i.getObjectID()) == null)
                        game.actionGroup.modify.put(i.getObjectID(), new GameFrame(0));
                    GameFrame cur = game.actionGroup.modify.get(i.getObjectID());
                    cur.add(i);
                }
                if (g.frameList.size() != 0) {
                    System.out.print(TankGame.test1);
                    System.out.print("        ");
                    System.out.println(TankGame.test2);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
