package org.gyming.tank.client;

import com.badlogic.gdx.Game;
import com.google.gson.Gson;
import org.gyming.tank.connection.ConnectMsg;
import org.gyming.tank.connection.GameFrame;
import org.gyming.tank.connection.MsgIO;

import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class TankGame extends Game {
    StartScreen startScreen;
    MainScreen mainScreen;
    GameOverScreen gameOverScreen;

    public static int test1 = 0, test2 = 0;
    private static String serverAddress = "192.168.123.74";
    private static int port = 7650;
    public int nowFrame, lastFireFrame;
    String name;
    String room;
    MsgIO S2C, C2S;
    LinkedBlockingQueue<GameFrame> download;
    LinkedBlockingQueue<String> queue;
    ActionGroup actionGroup;

    public void BuildConnection() {
        nowFrame = 0;
        lastFireFrame = 0;
        try {
            C2S = new MsgIO(new Socket(serverAddress, port));
            S2C = new MsgIO(new Socket(serverAddress, port));
            S2C.send(name);
            C2S.send(name);
            Gson gson = new Gson();
            C2S.send(gson.toJson(new ConnectMsg("join", name, room)));
            System.out.println("GYMing is so awful!!!!");

            Thread downloader = new Thread(new ClientDownloader(this, S2C));
            Thread painter = new Thread(new ClientPainter(this));
            Thread listener = new Thread(new ClientListener(this, C2S, queue));
            downloader.start();
            painter.start();
            listener.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create () {
        startScreen = new StartScreen(this);
        mainScreen = new MainScreen(this);
        gameOverScreen = new GameOverScreen(this);
        download = new LinkedBlockingQueue<>();
        queue = new LinkedBlockingQueue<>();
        setScreen(startScreen);
    }

    @Override
    public void render () {
    }

    @Override
    public void dispose () {
    }
}
