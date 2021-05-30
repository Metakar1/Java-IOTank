package org.gyming.tank.server;

import org.gyming.tank.connection.ConnectMsg;
import org.gyming.tank.connection.User;

public class RoomInfo {
    public ConnectMsg connectMsg;
    public User user;

    RoomInfo(ConnectMsg _connectMsg, User _user) {
        connectMsg = _connectMsg;
        user = _user;
    }
}