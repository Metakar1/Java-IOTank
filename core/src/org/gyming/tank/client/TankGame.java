package org.gyming.tank.client;

import com.badlogic.gdx.Game;
import com.google.gson.Gson;
import org.gyming.tank.connection.ConnectMsg;
import org.gyming.tank.connection.GameFrame;
import org.gyming.tank.connection.MsgIO;
import org.gyming.tank.object.PlayerObject;

import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class TankGame extends Game {
    StartScreen startScreen;
    MainScreen mainScreen;
    GameOverScreen gameOverScreen;
    private String userName, roomName;

    public int PlayerId;

    public static int test1 = 0, test2 = 0;
    private static String serverAddress = "127.0.0.1";
    private static int port = 7650;
    public int nowFrame, lastFireFrame;
    MsgIO S2C, C2S;
    LinkedBlockingQueue<GameFrame> download;
    LinkedBlockingQueue<String> queue;
    ActionGroup actionGroup;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void buildConnection() {
        nowFrame = 0;
        lastFireFrame = 0;
        try {
            C2S = new MsgIO(new Socket(serverAddress, port));
            S2C = new MsgIO(new Socket(serverAddress, port));
            S2C.send(userName);
            C2S.send(userName);
            Gson gson = new Gson();
            C2S.send(gson.toJson(new ConnectMsg("join", userName, roomName)));
            System.out.println("GYMing is so awful!!!!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create () {
        actionGroup = new ActionGroup();
        startScreen = new StartScreen(this);
        mainScreen = new MainScreen(this);
        gameOverScreen = new GameOverScreen(this);
        download = new LinkedBlockingQueue<>();
        queue = new LinkedBlockingQueue<>();
        setScreen(startScreen);
    }

    @Override
    public void render () {
        super.render();
    }

    @Override
    public void dispose () {
    }
}
