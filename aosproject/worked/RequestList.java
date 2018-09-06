//package aosproject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import org.omg.CORBA.Request;

public class RequestList {
	ArrayList<MessageAndSocket> requestList;
	
	public RequestList() {
		// TODO Auto-generated constructor stub
		requestList = new ArrayList<MessageAndSocket>();
	}
	
	public synchronized ArrayList<MessageAndSocket> getDeferredRequestListAndSetCurrentToNew() {
		ArrayList<MessageAndSocket> tmp = this.requestList;
		this.requestList = new ArrayList<MessageAndSocket>();
		return tmp;
	}
	
	public synchronized void addRequest(MessageAndSocket msgAndsckt)
	{
	//	System.out.println("adding object to queue");
		requestList.add(msgAndsckt);
	}
	
	public synchronized void sendAck(HostStatus hostStatus)
	{
		String debugMsg = "";
		if(requestList.size()>0)
		{
			//increasing local clock before timestamping the message
			long hostClock = hostStatus.incrementHostClock();
			debugMsg = "ACK message sent from hostId= "+hostStatus.getHostId() + " with Local Clock=" + hostClock;//System.console().readLine();
			for(MessageAndSocket msgAndSocket : requestList)
			{					
				Message msg = new Message("ACK", hostClock, debugMsg, hostStatus.getHostId());
				SocketAndIfItShouldBeAsked s = msgAndSocket.getSocket();
				//Socket socket = s.getSocket();
				//ObjectOutputStream out = s.getOut();
				DataOutputStream out = s.getOut();
				s.setToBeAsked(true);// considering optimization rules
				try {
			//		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
					//out.writeObject(msg);
					out.writeUTF(msg.toString());
					out.flush();
			//		out.close();
			//		System.out.println("Ack sent");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}	
			requestList = new ArrayList<MessageAndSocket>();
		}
		
	}
	
/*	public synchronized void setRequestListToNewArrayList()
	{
		requestList = new ArrayList<MessageAndSocket>();
	}*/
	public int getSize()
	{
		return requestList.size(); 
	}
}
