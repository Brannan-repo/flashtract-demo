package com.flashtract.billing.contracts.http;

public class Message {

	private String severity;
	private String messageText;
	private String messageCode;
	public static String MessageError = "ERROR";

	public Message(String severity, String messageText) {
		this.severity = severity;
		this.messageText = messageText;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public static Message userNotFoundMessage(int userId) {
		return new Message(MessageError, String.format("User with ID {%s} was not found.", userId));
	}

}
