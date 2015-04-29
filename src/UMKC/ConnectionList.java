package UMKC;
import java.net.InetAddress;
import java.net.Socket;

class ConnectionList extends Thread implements Runnable {
	static int index=0;
	static String msg= null;
	static Socket[] tcpClient= new Socket[20];
	static int a=0;
	static void AcceptConn (String ip_addr,int port) {
		new P2pGnutella().Client(ip_addr,port);
	}
	static void Msg (String msg1) {
		
		for (Socket tc : tcpClient) {
			if (tc != null) {
				new Messaging(tc, msg1);
			}	
		}		
	}
	
	
static void MsgForward (String msg1, Socket reply) {
		
		for (Socket tc : tcpClient) {
			if (tc != null && tc!= reply) {
				new Messaging(tc, reply, msg1);
			}	
		}		
	}

	public ConnectionList(Socket sock3) {
		//P2pGnutella.isConnect= sock3.isConnected();
		tcpClient[a]= sock3; //recvd client sock from accept now update array
		System.out.println(a+ " "+tcpClient[a]);
		a++;
	}
	static void maintainList(Socket sock4){
		System.out.println("now were onset");
		index++;
		tcpClient[index]=sock4;
	}
	static void show() {
		int p=1;
		if (tcpClient[p] != null) {
			System.out.println("Conn ID |	IP	|   Hostname Port |  Local Port | Remote |");
			for (; p<20; p++) {
				if (tcpClient[p] != null) {
						System.out.println(p + "     |     "+ tcpClient[p].getInetAddress().getHostAddress() + "   |    " + tcpClient[p].getInetAddress().getHostName() + "  | " + " " + tcpClient[p].getPort() + "  | " + " " + tcpClient[p].getLocalPort() );
				}
			}
		}
	}
	static void disconnect (int k) {
		int id;
		System.out.println("index to delete-"+k);
		for (id=k; id<19; id++) {
			try{
				if (tcpClient[id+1] != null) {
					tcpClient[id]=tcpClient[id+1];
				} 
				else {
					tcpClient[id]=null;
				}
			}
			catch (Exception e) {
				System.out.println("Exception disconnect()");
			}
		}
	index--;
	}
	static void info()
	   {
		   try
		   {
			  // String serverIp = InetAddress.getLocalHost().getHostAddress();
			   String hostName = InetAddress.getLocalHost().getHostName();
			
			   System.out.println("IP Address     |  Host Name 	         |    TCP Port  |   UDP Port");
			   System.out.println(P2pGnutella.ipAddr + "  |   " + hostName + "      |   " + P2pGnutella.tcpPort + "  |    " +P2pGnutella.udpPort);
		   }
		   catch(Exception e)
		   {
			   System.out.println("Exception at info()");
		   }
		   }
	public void run () {
		
	}
}