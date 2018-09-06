//package aosproject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class SocketAndIfItShouldBeAsked
{
	private Socket socket;
	private boolean toBeAsked;
	//public ObjectInputStream inputObjectReader;
	//public ObjectOutputStream out;
	public DataOutputStream out;
	
	public SocketAndIfItShouldBeAsked(Socket s) {
		// TODO Auto-generated constructor stub
		socket = s;
		toBeAsked = true; 
		out=null;
		/*try {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			inputObjectReader = new ObjectInputStream(socket.getInputStream());
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
		
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public synchronized boolean isToBeAsked() {
		return toBeAsked;
	}

	public synchronized void setToBeAsked(boolean toBeAsked) {
		this.toBeAsked = toBeAsked;
	}

	public synchronized DataOutputStream getOut() {
		if(out==null)
		{
			try {
				out = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return out;
	}

}