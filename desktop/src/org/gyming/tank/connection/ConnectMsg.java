package org.gyming.tank.connection;

public class ConnectMsg {
    public String type;
    public String name;
    public String room;

    public ConnectMsg(String type_, String name_, String room_) {
        type = type_;
        name = name_;
        room = room_;
    }
}
