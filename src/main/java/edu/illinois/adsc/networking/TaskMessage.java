package edu.illinois.adsc.networking;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * Created by robert on 12/8/15.
 */
public class TaskMessage implements Serializable {
    private int _task;
    private byte[] _message;

    public TaskMessage(int task, byte[] message) {
        _task = task;
        _message = message;
    }

    public int task() {
        return _task;
    }

    public byte[] message() {
        return _message;
    }

    public ByteBuffer serialize() {
        ByteBuffer bb = ByteBuffer.allocate(_message.length+2);
        bb.putShort((short)_task);
        bb.put(_message);
        return bb;
    }

    public void deserialize(ByteBuffer packet) {
        if (packet==null) return;
        _task = packet.getShort();
        _message = new byte[packet.limit()-2];
        packet.get(_message);
    }

}
