package edu.illinois.adsc.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by robert on 12/8/15.
 */
public class Receiver implements IConnection {

    ServerSocket server;

    Map<ObjectInputStream, Socket> inputStreamSocketMap = new HashMap<ObjectInputStream, Socket>();

    Map<ObjectInputStream, Thread> inputStreamThreadMap = new HashMap<>();

    LinkedBlockingQueue<TaskMessage> inputData = new LinkedBlockingQueue<>(10);
    final int maxBatchSize = 1000;

    public Receiver(int port) {
        try {
            server = new ServerSocket(port);

        } catch (Exception e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Socket newsocket = server.accept();
                        System.out.println("Receiver: New connection established!");
                        final ObjectInputStream newInputStream = new ObjectInputStream(newsocket.getInputStream());
                        inputStreamSocketMap.put(newInputStream, newsocket);

                        Thread receivingThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (true) {
                                    try {

                                        inputData.put((TaskMessage) (newInputStream.readObject()));

                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException ee) {
                                        break;
                                    } catch (InterruptedException inter) {
                                        inter.printStackTrace();
                                        break;
                                    }
                                }
                            }
                        });
                        receivingThread.start();
                        inputStreamThreadMap.put(newInputStream, receivingThread);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    public TaskMessage recv() throws InterruptedException{
        return inputData.take();
    }

    @Override
    public Iterator<TaskMessage> recv(int flags, int clientId) {

        ArrayList<TaskMessage> taskMessages = new ArrayList<>();
        inputData.drainTo(taskMessages, maxBatchSize);
        return taskMessages.iterator();
    }

    @Override
    public void send(int taskId, byte[] payload) {
        throw new UnsupportedOperationException("send is not supported by Receiver");
    }

    @Override
    public void send(Iterator<TaskMessage> msgs) {
        throw new UnsupportedOperationException("send is not supported by Receiver");
    }

    @Override
    public void close() {
        try {
            server.close();
            for (Socket socket : inputStreamSocketMap.values()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
