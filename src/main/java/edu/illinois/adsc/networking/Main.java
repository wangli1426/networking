package edu.illinois.adsc.networking;

import org.apache.commons.lang.SerializationUtils;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    public static void main(String[] args) {
        final int port = 50001;
        final Receiver receiver = new Receiver(port);
        System.out.println("connect....");
        final Sender sender = new Sender("localhost", port);
        System.out.println("Running threads....");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int taskid = 0;
                    int count = 0;
                    while(true) {
                        taskid++;

                        byte[] bytes = SerializationUtils.serialize(new Tuple());
                        Thread.sleep(0);
                        sender.send(taskid, bytes);

                        count++;

                        if(count % 10000 == 0 ) {
                            System.out.println("Send " + count + " tuples!");                        }
//                        System.out.println("Sent!");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    int count = 0;
                    while(true) {

                        receiver.recv();
                        count++;
//                        Iterator<TaskMessage> messages = receiver.recv(0, 0);
//
//                        while(messages.hasNext()) {
//                            TaskMessage taskMessage = messages.next();
//                            count++;
////                            System.out.println("Received task " + taskMessage.task() + " length:" + taskMessage.message().length);
//                        }

                        if(count % 10000 == 0 ) {
                            System.out.println("Received " + count + " tuples!");
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        Thread.sleep(1000);
                        MemoryReport.report();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
