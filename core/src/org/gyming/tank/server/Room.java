package org.gyming.tank.server;

import org.gyming.tank.connection.GameAction;
import org.gyming.tank.connection.GameFrame;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Room implements Runnable {
    public ArrayList<GameFrame> totFrame;
    String roomID;
    BlockingQueue<Client> clients;
    int frameID = 0;
    ArrayList<GameAction> curFrame;
    int nums = 0;
    boolean endState = false;
    boolean startState = false;
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            boolean hasClient = false;
            for (Client client : clients) {
                if (client.endState) {
                    continue;
                }
                startState = true;
                hasClient = true;
                while (!client.upload.isEmpty()) {
                    curFrame.add(client.upload.peek());
                    client.upload.poll();
                    nums++;
                }
            }
            GameFrame sumFrame = new GameFrame(frameID);
            for (GameAction action : curFrame) sumFrame.add(action);
//            System.out.println(nums);
            curFrame.clear();
            for (Client client : clients) {
                client.download.offer(sumFrame);
            }
            frameID++;
            totFrame.add(sumFrame);
            if (startState&&(!hasClient))  {
                System.out.println("Room Stop");
                endState = true;
                timerTask.cancel();
            }
        }
    };

    public Room(String _roomID) {
        roomID = _roomID;
        clients = new LinkedBlockingQueue<>();
        curFrame = new ArrayList<>();
        totFrame = new ArrayList<>();
    }

    @Override
    public void run() {
        Timer timer = new Timer(true);
        timer.schedule(timerTask, 1, 20);
    }
}
