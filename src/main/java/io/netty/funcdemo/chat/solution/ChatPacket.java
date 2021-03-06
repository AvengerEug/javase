package io.netty.funcdemo.chat.solution;

import java.io.Serializable;

public class ChatPacket implements Serializable {

    private int length;

    private byte[] data;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public ChatPacket(int length, byte[] data) {
        this.length = length;
        this.data = data;
    }
}
