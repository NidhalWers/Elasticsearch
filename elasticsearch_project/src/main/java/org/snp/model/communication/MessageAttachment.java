package org.snp.model.communication;

import java.io.Serializable;

/**
 * @author Olivier Pitton <olivier@indexima.com> on 16/12/2020
 */

public class MessageAttachment<T> extends Message {

	private T attachment;

	public MessageAttachment()	{}

	public MessageAttachment(MessageAttachment<T> messageAttachment) {
		super(messageAttachment);
		this.attachment = messageAttachment.getAttachment();
	}

	public MessageAttachment(int code, T attachment) {
		super(code);
		this.attachment = attachment;
	}

	@Override
	public boolean hasAttachment() {
		return true;
	}

	public T getAttachment() {
		return attachment;
	}

	@Override
	public String toString() {
		return "MessageAttachment{" +
				"attachment=" + attachment +
				", code=" + code +
				'}';
	}
}
