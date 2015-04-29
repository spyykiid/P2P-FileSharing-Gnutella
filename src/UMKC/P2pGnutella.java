package UMKC;
import java.net.*;
import java.io.*;
import java.util.*;


public class P2pGnutella implements Runnable {
	int i,numOfConnections;
	public static int tcpPort, udpPort= 0;
	public Socket tcpClient= null;
	public ServerSocket tcpSocket= null;
	public DatagramSocket udpSocket=null;
	public static String ipAddr= null;
	static boolean isConnect= false;
	static BufferedWriter fileOut= null;
	public P2pGnutella(int tcpPort, int udpPort, int numOfConnections) {
			try {
				if((tcpPort >= 1) && (tcpPort <= 60000) &&(udpPort >= 1)&&(udpPort <= 60000)) {
					
					tcpSocket = new ServerSocket(tcpPort, numOfConnections);
					udpSocket = new DatagramSocket(udpPort);
					Socket testSocket= new Socket("8.8.8.8",53);
					ipAddr= testSocket.getLocalAddress().getHostAddress();
					P2pGnutella.tcpPort= tcpPort;
					P2pGnutella.udpPort= udpPort;
					System.out.println("Listening on "+ ipAddr + " TCP port- " + tcpPort+" and UDP port- "+ udpPort);
					this.numOfConnections= numOfConnections;
					testSocket.close();
					listen(tcpSocket);
				}
				else {
					System.out.println("Port Numbers should be between 1 and 60000");
				}
			}
			catch (SocketTimeoutException s) {
				System.out.println("timeout");
			}
			catch (IOException ioe) {
				System.out.println("could not listen on port "+ tcpPort);
				System.exit(-1);
			}
	}
	public P2pGnutella (ServerSocket s) {
		this.tcpSocket= s;
	}
	public P2pGnutella () {
	
	}
	void listen(ServerSocket s){
			try {
					//i++;
					System.out.println();
					
					Thread t1 = new Thread((Runnable) new P2pGnutella(s));
					t1.start();
					Thread t2= new Thread((Runnable) new AcceptInput());
					t2.start();
					Thread t3= new Thread((Runnable) new UDPrecv(udpSocket));
					t3.start();
					//start udp listen thread
				}
				catch (Exception e) {
					System.out.println("Cannot accept connection");
				}
			}
	
	public void Client(String addr, int port) 
	{
		try {
			
			tcpClient = new Socket(addr,port);
			fileOut = new BufferedWriter(new FileWriter("neighbours.txt", true));
			fileOut.write(addr.toString()+ "; " + port + "\n" );
			fileOut.close();
			ConnectionList.maintainList(tcpClient);
			
			//maintain a list here now to have index ipaddr, hostaddr, localport, remoteport;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void run() {
		while (true) {
			try {
				isConnect=false;
				Socket sock1= tcpSocket.accept();
	
				System.out.println("got Conn. request from "+sock1.getInetAddress().toString());
				Thread t4= new Thread ((Runnable)new Messaging(sock1));
				t4.start();
				//new ConnectionList(sock1);
			}
			catch (IOException o) {
				System.out.println("Cannot connect: Read Failed");
				}
			}
		}

	public static void main(String[] args) {
		
	//	Scanner in = new Scanner(System.in);
	//	System.out.println("Enter the 1st argument");
	//	int i1 = in.nextInt();
	//	System.out.println("Enter the 2nd argument");
	//	int i2 = in.nextInt();
		//new P2pGnutella(Integer.parseInt(args[0]),Integer.parseInt(args[1]),5);
		new P2pGnutella(7000,8000,5);
		//in.close();
	}
}


	



