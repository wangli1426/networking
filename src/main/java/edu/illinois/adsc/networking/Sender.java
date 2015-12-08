package edu.illinois.adsc.networking;

import org.apache.commons.lang.SerializationUtils;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;

/**
 * Created by robert on 12/8/15.
 */
public class Sender implements IConnection {

    Socket socket;
    ObjectOutputStream out;

    public Sender(String ip, int port) {

        try {
            socket = new Socket(ip, port);
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Iterator<TaskMessage> recv(int flags, int clientId) {
        throw new UnsupportedOperationException("recv() is unsupported in Sender!");
//        return null;
    }

    @Override
    public void send(int taskId, byte[] payload) {
        TaskMessage taskMessage = new TaskMessage(taskId, payload);
        try {
            out.writeObject(taskMessage);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(Iterator<TaskMessage> msgs) {
        while(msgs.hasNext()) {
            try {
                out.writeObject(msgs.next());
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void close() {
        try {
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
