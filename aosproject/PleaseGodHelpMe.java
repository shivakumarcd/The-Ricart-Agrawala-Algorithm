//package aosproject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class PleaseGodHelpMe {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/* Section: to setup initial TCS connections bw all pair with end product socketArray*/
		int hostCounts = 0;
		int port = 0;
		ArrayList<String> ips= new ArrayList<String>();
		ArrayList<SocketAndIfItShouldBeAsked> socketArray = new ArrayList<SocketAndIfItShouldBeAsked>();
	//	ArrayList<Integer>	socketPort = new ArrayList<Integer>();
		
		
		int varUsedToSetCurrentHostId=1;
		
		//reading current IPs on file to whom request is to be sent
 		try {
			FileReader fReader = new FileReader("/home/004/s/sx/sxd130930/docs/configTry");
			BufferedReader br = new BufferedReader(fReader);
			hostCounts = Integer.parseInt(br.readLine());
			port = Integer.parseInt(br.readLine());

			String tmp;
			while((tmp = br.readLine())!=null)
			{
				ips.add(tmp);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//	System.out.println("at 1");
		//	String input = System.console().readLine();	
		//sending request to each read IP as they are already up
		if((ips.size()>0))
		{
			int lastHostId=0;
			for (String ipHost : ips) {
				try {
					String[] tokens = ipHost.split("=");
					ipHost = tokens[0];
					lastHostId = Integer.parseInt(tokens[1]);
				//	System.out.println("Printing as clinet: trying to connect to server: " + ipHost +" with port: " + port);
					Socket socket = new Socket(ipHost, port);
					SocketAndIfItShouldBeAsked s = new SocketAndIfItShouldBeAsked(socket); 
					socketArray.add(s);
				//	System.out.println("printing as client: connected with server");
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			varUsedToSetCurrentHostId = lastHostId+1;
		}
		//	System.out.println("at 2");
		//	input = System.console().readLine();
		//getting current host name
		String currentHostName=null;
		InetAddress current_addr=null;
		try {
			current_addr = InetAddress.getLocalHost();
			currentHostName = current_addr.getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//writing current host name to file and also Creating HostStatus Object
		HostStatus hostStatus = new HostStatus(varUsedToSetCurrentHostId, hostCounts);
		try {
			FileWriter fw = new FileWriter("/home/004/s/sx/sxd130930/docs/configTry", true);
			fw.write(currentHostName + "=" + varUsedToSetCurrentHostId +"\n");
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//	System.out.println("at 3");
		//	input = System.console().readLine();
		//becoming server and waiting to receive new server connection
		System.out.println("Server at port :"+ port +" established");
		//	input = System.console().readLine();
		try {
			ServerSocket ss = new ServerSocket(port);
			while(socketArray.size() != (hostCounts-1))
			{
			//	System.out.println("printing as server: waiting for new connection request from cliet");
				Socket socket = ss.accept();
				SocketAndIfItShouldBeAsked s = new SocketAndIfItShouldBeAsked(socket); 
				socketArray.add(s);
			//	System.out.println("printing as server: new connection request from cliet added");
			}
		//	ss.close();/////////////*******************************DONT EVER, ever ever ever, ever, FORGET THIS***********************************************
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		
		
		
		
		
		
		
		
		/*Section: to start listener threads and corresponding Object creations required */
		// each thread will add msg and socket on which they are listening, to below list
		RequestList requestList = new RequestList();
		RequestList deferredRequestList =  new RequestList();
		
		for(SocketAndIfItShouldBeAsked s : socketArray)
		{
			MessageListenerThread mlt = new MessageListenerThread(requestList, deferredRequestList, s, hostStatus);
		//	System.out.println("starting each listenres");
			mlt.start();
		}
	
		//Section: Ack sender thread
		AckSenderThread ackThread = new AckSenderThread(requestList, hostStatus);
		ackThread.start();

		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CSCompetingThread csCompThread = new CSCompetingThread(socketArray, deferredRequestList, hostStatus, requestList);
		csCompThread.start();
/*
		try {
			Thread.currentThread().wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//closing all socket connections
		/*int i=0;
		for (Socket socket : socketArray) {
			try {
				System.out.println("closing connection, till closed :" + ++i);
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
	}
}

