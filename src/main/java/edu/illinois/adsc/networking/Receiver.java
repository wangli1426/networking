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

//    ObjectInputStream in;
    ServerSocket server;
//    Socket socket;
    Map<ObjectInputStream, Socket> inputStreamSocketMap = new HashMap<ObjectInputStream, Socket>();

    Map<ObjectInputStream, Thread> inputStreamThreadMap = new HashMap<>();

    LinkedBlockingQueue<TaskMessage> inputData = new LinkedBlockingQueue<>();
    final int maxBatchSize = 1000;

    public Receiver(int port) {
        try {
        server = new ServerSocket(port);



//        socket = server.accept();
//        in = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Socket newsocket = server.accept();
                        System.out.println("Receiver: New connection established!");
                        final ObjectInputStream newInputStreawm = new ObjectInputStream(newsocket.getInputStream());
                        inputStreamSocketMap.put(newInputStreawm, newsocket);

                        Thread receivingThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(true) {
                                    try {

                                        inputData.put((TaskMessage) (newInputStreawm.readObject()));

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
                        inputStreamThreadMap.put(newInputStreawm, receivingThread);
                    } catch (IOException e ) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

//    Object recv() {
//
//
//
//
//        try {
//             return in.readObject();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    @Override
    public Iterator<TaskMessage> recv(int flags, int clientId) {

//        ArrayList<TaskMessage> taskMessages = new ArrayList<TaskMessage>();
//        int received = 0;
//        while(received < maxBatchSize) {
//
//            boolean read = false;
//            for(ObjectInputStream inputStream: inputStreamSocketMap.keySet() ) {
//                try {
//                    taskMessages.add((TaskMessage) inputStream.readObject());
//                    read = true;
//                } catch (IOException e) {
//                    inputStreamSocketMap.remove(inputStream);
//                    System.out.println("An input stream is removed!");
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//            if(!read) {
//                break;
//            }
//        }
//
//        return taskMessages.iterator();
//        ArrayList<TaskMessage> taskMessages = new ArrayList<TaskMessage>();
//        for(ObjectInputStream inputStream: inputStreamSocketMap.keySet() ) {
//            try {
//                taskMessages.add((TaskMessage) inputStream.readObject());
//
//            } catch (IOException e) {
//                inputStreamSocketMap.remove(inputStream);
//                System.out.println("An input stream is removed!");
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        }

        ArrayList<TaskMessage> taskMessages = new ArrayList<TaskMessage>();
//        int received = 0;
//        while(received < maxBatchSize) {
//            taskMessages.add(inputData.drainTo());
//        }
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
            for(Socket socket: inputStreamSocketMap.values()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
