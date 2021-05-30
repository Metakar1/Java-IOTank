package org.gyming.tank.client;

import com.google.gson.Gson;
import org.gyming.tank.connection.GameFrame;
import org.gyming.tank.connection.MsgIO;

public class ClientDownloader implements Runnable {
    Client client;
    MsgIO S2C;

    public ClientDownloader(Client client_, MsgIO S2C_) {
        client = client_;
        S2C = S2C_;
    }

    public void run() {
        GameFrame g;
        while (true) {
            try {
                Gson gson = new Gson();
                g = gson.fromJson(S2C.receive(), GameFrame.class);
                client.download.add(g);
                if (g.frameList.size() != 0) {
                    System.out.print(Client.test1);
                    System.out.print("        ");
                    System.out.println(Client.test2);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
