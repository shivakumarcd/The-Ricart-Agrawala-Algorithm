//package aosproject;

public class AckSenderThread extends Thread {
	RequestList requestList;
	HostStatus hostStatus;
	
	public AckSenderThread(RequestList rl, HostStatus hs) {
		// TODO Auto-generated constructor stub
		this.requestList = rl;
		this.hostStatus = hs;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
	//	System.out.println("@Ack sender Thread:entering listening mode");
		while(true)
		{
			//System.out.println("@Ack sender Thread:in loop");
			requestList.sendAck(this.hostStatus);				
		}
	}
}
