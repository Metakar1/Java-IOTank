package org.gyming.tank.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import org.gyming.tank.connection.ConnectMsg;
import org.gyming.tank.connection.GameFrame;
import org.gyming.tank.connection.MsgIO;
import org.gyming.tank.object.GameObject;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class TankGame extends Game {
    public static int test1 = 0, test2 = 0;
    public int playerID;
    public int nowFrame, lastFireFrame;
    StartScreen startScreen;
    MainScreen mainScreen;
    GameOverScreen gameOverScreen;
    MsgIO S2C, C2S;
    LinkedBlockingQueue<GameFrame> download;
    LinkedBlockingQueue<String> queue;
    public ActionGroup actionGroup;
    private String userName, roomName;
    private String serverAddress;
    private int port;
    public LinkedBlockingQueue<GameObject> toBeDeleted;


    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

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
    public void create() {
        actionGroup = new ActionGroup();
        startScreen = new StartScreen(this);
        mainScreen = new MainScreen(this);
        gameOverScreen = new GameOverScreen(this);
        download = new LinkedBlockingQueue<>();
        queue = new LinkedBlockingQueue<>();
        toBeDeleted = new LinkedBlockingQueue<GameObject>();
        setScreen(startScreen);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
    }
}