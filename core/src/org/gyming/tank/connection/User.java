package org.gyming.tank.connection;

public class User {
    public MsgIO C2S, S2C;

    public User(MsgIO _C2S, MsgIO _S2C) {
        C2S = _C2S;
        S2C = _S2C;
    }
}
