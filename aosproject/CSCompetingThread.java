//package aosproject;

import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class CSCompetingThread extends Thread {
	private ArrayList<SocketAndIfItShouldBeAsked> socketList;
	private RequestList deferedRequestList; 
	private RequestList requestList;
	HostStatus hostStatus;
	int i;
	int totalNumberOfMessagesExchanged=0;
	int minNoOfMsgsForCS=20;
	int maxNoOfMsgsForCS=0;

	CSCompetingThread(ArrayList<SocketAndIfItShouldBeAsked> sl, RequestList deferredList ,HostStatus hs, RequestList rl)
	{
		this.socketList = sl;
		this.deferedRequestList = deferredList;
		this.requestList = rl;
		this.hostStatus = hs;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
	
		while(i<40)
		{
		//	System.out.println("Host Status:::near begin of CSCompeteThread\n" + hostStatus.printHostStatus());
		//	System.out.println("\n\t\t num of requests in request List : "+ requestList.getSize());
		//	System.out.println("\n\t\t num of requests in deferred List : "+ deferedRequestList.getSize());
			i++;
			if(i<20 || (hostStatus.getHostId()%2)==1 ){
				int min = 10;
				int max = 100;
				Random r = new Random();
				try {
					sleep((r.nextInt(max-min+1)+min)  );
				} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else
			{
				int min = 200;
				int max = 500;
				Random r = new Random();
				try {
					sleep((r.nextInt(max-min+1)+min)  );
				} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			//increasing local clock only once per request... not per each sending request
			long requestClock = hostStatus.prepareForCCS();
			int tmpUsedToSendHostId = hostStatus.getHostId();
			
			String debugMsg = "Request message sent from hostId= "+hostStatus.getHostId() + " with Local Clock=" + requestClock;//System.console().readLine();
			Message msg = new Message("request", requestClock , debugMsg, tmpUsedToSendHostId );
			
			int messageCount=0;
			for(SocketAndIfItShouldBeAsked s: socketList)
			{
				if(s.isToBeAsked())// considering optimization rules
				{
					// considering optimization rules
					messageCount++;
					hostStatus.incrementResponseCount();// increase number of responses that should be expected
					DataOutputStream out = s.getOut();
					//System.out.println(debugMsg);
					try {
						out.writeUTF(msg.toString());
						out.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			
			messageCount = 2 * messageCount;
			if(minNoOfMsgsForCS > messageCount)
				minNoOfMsgsForCS = messageCount;
			if(maxNoOfMsgsForCS < messageCount)
				maxNoOfMsgsForCS = messageCount;
			totalNumberOfMessagesExchanged = totalNumberOfMessagesExchanged + messageCount;
			
			
			long waitStartTime = System.currentTimeMillis();
			
			
			//System.out.println("Completed task:" + debugMsg);
			//System.out.println(" NO OF RESPONSES EXPECTED AT THIS MOMENT:" + hostStatus.getResponseCount());
			
			//***************************************************************************************************
			//Now waiting for permissions from those to whom request is sent and  and then entering CS
			while(hostStatus.getResponseCount()>0)// considering optimization rules
			{
			}
			hostStatus.setHostInCS(true);
			long waitEndTime = System.currentTimeMillis();
			long waitTime = waitEndTime - waitStartTime;
			//System.out.println("coming out of waiting while loop");
			//entering CS
			{
				System.out.println(": **********ENTERING critical section for: " + i + " th time" );
				System.out.println("number of message exchanged                            =" + messageCount);
				System.out.println("elapsed time b/w request and entering Critical Section =" + waitTime);
				try {
					FileWriter fw = new FileWriter("/home/004/s/sx/sxd130930/docs/configTry", true);
					fw.write(hostStatus.getHostId() + "\t Entering" +"\n");
					fw.close();
					sleep(20);
					fw = new FileWriter("/home/004/s/sx/sxd130930/docs/configTry", true);
					fw.write(hostStatus.getHostId() + "\t Leaving" +"\n");
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				
				}
				//System.out.println(": **********LEAVING  critical section for: " + i  + " th time" );
			}
			//Adjusting host condition so that susquent reqs are put in Request list and ACKD immediately & no new req added to deferred list at this point
			hostStatus.prepareForCSEnd();
			
			//seding ACK to all in the deferred list			
			deferedRequestList.sendAck(hostStatus);
			
			//System.out.println("reaching flush ");
			//System.out.println("Host Status:::near end of CSCompeteThread\n" + hostStatus.printHostStatus());
			//System.out.println("\n\t\t num of requests in request List : "+ requestList.getSize());
			//System.out.println("\n\t\t num of requests in deferred List : "+ deferedRequestList.getSize());
		}		
		System.out.println("************************ Node"+ hostStatus.getHostId() +"*****************************");
		System.out.println("Total number of messages exchanged	      :" + totalNumberOfMessagesExchanged );
		System.out.println("Minimum number of messages a node had to exchange to enter its critical section:" + minNoOfMsgsForCS);
		System.out.println("Maximum number of messages a node had to exchange to enter its critical section:" + maxNoOfMsgsForCS);
	}
}
