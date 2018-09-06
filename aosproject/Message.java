//package aosproject;

import java.io.Serializable;

public class Message implements Serializable {
	private String messageType;
	private long messageTimeStamp;
	private String someDebugInfo;
	private int messageSenderId;
	
	
	Message(String type,long time, String info, int id)
	{
		this.messageType = type;
		this.messageTimeStamp = time;
		this.messageSenderId = id;
		this.someDebugInfo = info;
	}
	
	Message(String s) {
		// TODO Auto-generated constructor stub
		String tokens[];
		tokens = s.split(",");
		this.messageType = tokens[0];
		this.messageTimeStamp = Long.parseLong(tokens[1]);
		this.messageSenderId = Integer.parseInt(tokens[2]);
		this.someDebugInfo = tokens[3];
	}
	
	public String toString()
	{
		return (messageType +","+messageTimeStamp + ","+ messageSenderId + "," + someDebugInfo );
	}
	
	public int getMessageSenderId() {
		return messageSenderId;
	}

/*	public void setMessageSenderId(int messageSenderId) {
		this.messageSenderId = messageSenderId;
	}

	public void setMessageTimeStamp(long messageTimeStamp) {
		this.messageTimeStamp = messageTimeStamp;
	}
*/	
	public String getMessageType() {
		return messageType;
	}
/*	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
*/
	public long getMessageTimeStamp() {
		return messageTimeStamp;
	}
/*	
	public void setMessageTimeStamp(int messageTimeStamp) {
		this.messageTimeStamp = messageTimeStamp;
	}
*/
	public String getSomeDebugInfo() {
		return someDebugInfo;
	}
	public void setSomeDebugInfo(String someDebugInfo) {
		this.someDebugInfo = someDebugInfo;
	}	
}
