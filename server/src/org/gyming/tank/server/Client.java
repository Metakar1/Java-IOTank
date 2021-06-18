package org.gyming.tank.server;

import com.google.gson.Gson;
import org.gyming.tank.connection.ConnectMsg;
import org.gyming.tank.connection.GameAction;
import org.gyming.tank.connection.GameFrame;
import org.gyming.tank.connection.User;

import java.util.concurrent.LinkedBlockingQueue;

public class Client {
    User user;
    ConnectMsg connectMsg;
    LinkedBlockingQueue<GameAction> upload;
    LinkedBlockingQueue<GameFrame> download;
    Receive receive;
    Send send;
    boolean endState;

    public Client(ConnectMsg _connectMsg, User _user) {
        user = _user;
        connectMsg = _connectMsg;
        upload = new LinkedBlockingQueue<>();
        download = new LinkedBlockingQueue<>();
        receive = new Receive();
        send = new Send();
        endState = false;
    }

    public class Receive implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    String msg = user.C2S.receive();
                    Gson gson = new Gson();
                    GameAction action = gson.fromJson(msg, GameAction.class);
                    upload.offer(action);
                }
            }
            catch (Exception e) {
                endState = true;
            }
        }
    }

    public class Send implements Runnable {
        @Override
        public void run() {
            try {
                Gson gson = new Gson();
                while (true) {
                    if (!download.isEmpty()) {
                        GameFrame cur = download.peek();
                        user.S2C.send(gson.toJson(cur));
                        download.poll();
                    }
                }
            }
            catch (Exception e) {
                endState = true;
            }
        }
    }
}
