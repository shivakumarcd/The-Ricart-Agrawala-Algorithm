//package aosproject;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class MessageListenerThread extends Thread {
	private RequestList requestList;
	private RequestList deferedRequestList;
	private SocketAndIfItShouldBeAsked socketAndIfItShouldBeAsked;
	private Socket socket;
	HostStatus hostStatus;

	public MessageListenerThread(RequestList requestList, RequestList deferedList, SocketAndIfItShouldBeAsked socket, HostStatus hs) {
		// TODO Auto-generated constructor stub
		this.requestList = requestList;
		this.deferedRequestList = deferedList;
		this.socketAndIfItShouldBeAsked = socket;
		this.socket = this.socketAndIfItShouldBeAsked.getSocket();  
		this.hostStatus = hs;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		//System.out.println("@Message Listener established");
		//socket.get
		//ObjectInputStream inputObjectReader = null;
		DataInputStream inputObjectReader=null;
		try {
			inputObjectReader = new DataInputStream(socket.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true)
		{
			//System.out.println("Host Status:::near beginning of Message Listener\n" + hostStatus.printHostStatus());
			//System.out.println("\n\t\t num of requests in request List : "+ requestList.getSize());
			//System.out.println("\n\t\t num of requests in deferred List : "+ deferedRequestList.getSize());
			try {
				//System.out.println("Reading");
				//inputObjectReader = new ObjectInputStream(socket.getInputStream());
				String in = null;
				in = inputObjectReader.readUTF();
				String inString = String (in);
				Message msgReceived = new Message(in);
				//Message msgReceived = (Message)socketAndIfItShouldBeAsked.inputObjectReader.readObject();
				
				
				
			//	while(hostStatus.isHostMListenerBlocked())
			//		System.out.println("stuck as MListener is locked");
				//inputObjectReader.close();
				
				//increasing local clock according to Lamport clock:::::::below code will give error as 9 threads running and they have to do sequentially
				long msgClock=msgReceived.getMessageTimeStamp();
				long varUsedToPrintLater = hostStatus.incrementAccordingToLamport(msgClock);
				//long varUsedToPrintLater = hostStatus.getHostClock();
				
				MessageAndSocket msgAndSocket = new MessageAndSocket(msgReceived, socketAndIfItShouldBeAsked);
				//	System.out.println("message recived " + msgReceived.getMessageType() );
				if(msgReceived.getMessageType().equals("request"))
				{					
					// have to check timestamp conditions
				//	while(hostStatus.isHostMListenerBlocked())
					//	System.out.println("stuck as MListener is locked");
					//System.out.print("REQUEST " );
					//System.out.println("Message received at HostId="+ hostStatus.getHostId() + "msg content="+ "\"" + msgReceived.getSomeDebugInfo() + "\"");
					//System.out.println("New local clock value after above message:"+ varUsedToPrintLater);
					
					
					if(!hostStatus.isHostCompetingForCS())
						requestList.addRequest(msgAndSocket);
					else
					{
						if(hostStatus.isHostInCS())
						{
							//System.out.println("host in critical section-->adding to deferred list");
							this.deferedRequestList.addRequest(msgAndSocket);
						}
						else if(!socketAndIfItShouldBeAsked.isToBeAsked())
						{
							//System.out.println("host in critical section AND AND this socket is not asked-->adding to deferred list");
							this.deferedRequestList.addRequest(msgAndSocket);
						}
						else if(msgReceived.getMessageTimeStamp() > hostStatus.getClockOfRecentCSRequestMade())
						{
							//System.out.println("request time > host request time-->adding to deferred list");
							this.deferedRequestList.addRequest(msgAndSocket);
						}
						else if(msgReceived.getMessageTimeStamp() < hostStatus.getClockOfRecentCSRequestMade())
							this.requestList.addRequest(msgAndSocket);
						else if(msgReceived.getMessageTimeStamp() == hostStatus.getClockOfRecentCSRequestMade())
						{
							if(msgReceived.getMessageSenderId() < hostStatus.getHostId())
								this.requestList.addRequest(msgAndSocket);
							else if(msgReceived.getMessageSenderId() > hostStatus.getHostId())
							{
								//System.out.println("(request time = host request time && Host Id< sender Id) -->adding to deferred list");
								this.deferedRequestList.addRequest(msgAndSocket);
							}
						}
					}
				}
				else if(msgReceived.getMessageType().equals("ACK"))
				{
					socketAndIfItShouldBeAsked.setToBeAsked(false);;
					hostStatus.decreaseResponseCount();
					//hostStatus.incrementResponseCount();
				//	System.out.print("ACK ");
				//	System.out.println("Message received at HostId="+ hostStatus.getHostId() + "msg content="+ "\"" + msgReceived.getSomeDebugInfo() + "\"");
				//	System.out.println("New local clock value after above message:"+ varUsedToPrintLater);
				}
				else
					System.out.println(":((((((((((((((((((((this is too much... I may give up");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("PLS GOD... APPO ME");
				e.printStackTrace();
				//String i = System.console().readLine();
			}
			//System.out.println("Host Status:::near end of Message Listener\n" + hostStatus.printHostStatus());
			//System.out.println("\n\t\t num of requests in request List : "+ requestList.getSize());
			//System.out.println("\n\t\t num of requests in deferred List : "+ deferedRequestList.getSize());
		}
	}

	private String String (Object in) {
		// TODO Auto-generated method stub
		return null;
	}

}
