package edu.illinois.adsc.networking;

import java.util.Iterator;

/**
 * Created by robert on 12/8/15.
 */
public interface IConnection {

    /**
     * receive a batch message iterator (consists taskId and payload)
     * @param flags 0: block, 1: non-block
     * @return
     */
    public Iterator<TaskMessage> recv(int flags, int clientId);

    /**
     * send a message with taskId and payload
     * @param taskId task ID
     * @param payload
     */
    public void send(int taskId, byte[] payload);

    /**
     * send batch messages
     * @param msgs
     */

    public void send(Iterator<TaskMessage> msgs);

    /**
     * close this connection
     */
    public void close();
}
