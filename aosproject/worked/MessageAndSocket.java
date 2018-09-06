//package aosproject;

import java.net.Socket;

public class MessageAndSocket
{
	private SocketAndIfItShouldBeAsked socket;
	private Message message;
	
	public MessageAndSocket(Message msg, SocketAndIfItShouldBeAsked s) {
		// TODO Auto-generated constructor stub
		message = msg;
		socket = s;
	}
	
	public SocketAndIfItShouldBeAsked getSocket() {
		return socket;
	}
/*	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
*/
	public Message getMessage() {
		return message;
	}
/*
	public void setMessage(Message message) {
		this.message = message;
	}
*/
}
