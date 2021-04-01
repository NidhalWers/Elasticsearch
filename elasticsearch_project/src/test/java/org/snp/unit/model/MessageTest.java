package org.snp.unit.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;

public class MessageTest {

    @Test
    public void testGetCode(){
        Message message = new Message(200);

        int code = message.getCode();
        Assertions.assertEquals(200, code);
    }

    @Test
    public void testHasAttachmentMessageAttachment(){
        Message message = new MessageAttachment<>(500, null);

        boolean hasAtt = message.hasAttachment();
        Assertions.assertTrue(hasAtt);
    }

    @Test
    public void testHasAttachmentMessage(){
        Message message = new Message(200);

        boolean hasAtt = message.hasAttachment();
        Assertions.assertFalse(hasAtt);
    }

    @Test
    public void testGetAttachment(){
        Double d = Double.valueOf(2);
        Message message = new MessageAttachment<>(500, d);

        Double att = (Double) ((MessageAttachment)message).getAttachment();

        Assertions.assertEquals(2, att);
    }
}
