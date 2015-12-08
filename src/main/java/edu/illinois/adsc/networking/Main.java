package edu.illinois.adsc.networking;

import org.apache.commons.lang.SerializationUtils;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    public static void main(String[] args) {
        MyContext myContext = new MyContext();
        System.out.println("bind....");
        final IConnection receiver = myContext.bind("recevier", 50000);
        System.out.println("connect....");
        final IConnection sender = myContext.connect("sender","192.168.0.183", 50000);
        System.out.println("Running threads....");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int taskid = 0;
                    while(true) {
                        taskid++;

                        byte[] bytes = SerializationUtils.serialize(new Tuple());
                        Thread.sleep(0);
                        sender.send(taskid, bytes);
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
                    while(true) {
                        Iterator<TaskMessage> messages = receiver.recv(0, 0);
                        while(messages.hasNext()) {
                            TaskMessage taskMessage = messages.next();
//                            System.out.println("Received task " + taskMessage.task() + " length:" + taskMessage.message().length);
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
