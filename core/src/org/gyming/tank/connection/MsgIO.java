package org.gyming.tank.connection;

import java.io.*;
import java.net.Socket;

public class MsgIO {
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

    public MsgIO(Socket _socket) throws IOException {
        socket = _socket;
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        dataOutputStream = new DataOutputStream(outputStream);
        dataInputStream = new DataInputStream(inputStream);
    }

    public String receive() throws IOException {
        return dataInputStream.readUTF();
    }

    public void send(String msg) throws IOException {
        dataOutputStream.writeUTF(msg);
    }

    public boolean isConnect() {
        return !socket.isClosed();
    }
}
