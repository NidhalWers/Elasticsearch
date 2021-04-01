package org.snp.model.communication;

public class Message {

    protected int code;

    public Message(){}

    public Message(Message message) {
        this.code = message.getCode();
    }

    public Message(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public boolean hasAttachment() {
        return false;
    }

}
