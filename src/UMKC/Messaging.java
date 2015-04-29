package UMKC;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.StringTokenizer;


class Messaging implements Runnable {
	Socket s1,s2;
	String msg;
	StringTokenizer st;
	static boolean found = false;
	String directory = "C:\\Users\\Rahul\\Desktop\\sharedFolder"; 
	static String files ="";
	public Messaging (Socket s1, String msg) {
		this.s1=s1;
		this.msg= msg;
		send();
	}
	public Messaging (Socket s1, Socket s2, String msg) {
		this.s1=s1;
		this.s2 = s2;
		this.msg= msg;
		sendForward();
	}
	
	public Messaging (Socket s2) {
		this.s2=s2;
	}
	void send(){
		try {
			DataOutputStream dout= new DataOutputStream(s1.getOutputStream());
			dout.writeUTF(msg);
			dout.flush();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		boolean b = true;
		while (b) {
			DataInputStream din;
			try {
				din = new DataInputStream(s1.getInputStream()); 
				String str= din.readUTF();
				if (str.contains("filefound")) {
					System.out.println(str);
					AcceptInput.QueryHitList[AcceptInput.hitindex] = str;
					AcceptInput.hitindex++;
				}
				if(str.contains("FilesList"))
				{
					System.out.println(str);
				}
				//if (str==null) {
				din.close();
				//}
			}
			catch (IOException e) {
				e.printStackTrace();
				b=false;
				//din.close();
			}
			}
	}
	
	void sendForward(){
		try {
			DataOutputStream dout= new DataOutputStream(s1.getOutputStream());
			dout.writeUTF(msg);
			dout.flush();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		boolean b = true;
		while (b) {
			DataInputStream din;
			try {
				din = new DataInputStream(s1.getInputStream()); 
				String str= din.readUTF();
				if(str.contains("filefound")){
				DataOutputStream dout= new DataOutputStream(s2.getOutputStream());
				dout.writeUTF(str);
				dout.flush();
				}
				
				//if (str==null) {
				din.close();
				//}
			}
			catch (IOException e) {
				e.printStackTrace();
				b=false;
				//din.close();
			}
			}	
	}
	
	 static void send_udp(String ipaddr, int udPort, String message)
	   {
		   try
		   {
			   DatagramSocket clientSocket = new DatagramSocket();
			   byte[] sendData = new byte[1024];
			   sendData = message.getBytes();
			   DatagramPacket sendPacket =  new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ipaddr), udPort);
			   clientSocket.send(sendPacket);
			   clientSocket.close();
		   }
		   catch(SocketTimeoutException e)
		   {
			   System.out.println("Connection not able to established :");
		   }
		   catch(UnknownHostException e)
		   {
			   e.printStackTrace();
		   }
		   catch(IOException e)
		   {
			   e.printStackTrace();
		   }
	   }
	 public static void findFile(String name,File file)
	    {
		 System.out.println("----Directory inside " + file +":::" +"file name: "+ name);
	        File[] list = file.listFiles();
	        if(list!=null)
	        for (File fil : list)
	        { System.out.println("----files in directory:"+fil.getName());
	            if (fil.isDirectory())
	            {
	                findFile(name,fil);
	            }
	            
	            else if (name.equalsIgnoreCase(fil.getName()))
	            {
	                System.out.println("----File Found at Dirctory "+fil.getParentFile());
	                found = true;
	            }
	        }
	    }
	 public static void listOfFiles(File file)
	    {
		 System.out.println("----Directory inside " + file);
	        File[] list = file.listFiles();
	        if(list!=null)
	        for (File fil : list)
	        { System.out.println("----files in directory:"+fil.getName());
	            if (fil.isDirectory())
	            {
	            	listOfFiles(fil);
	            }
	            
	            {
	            	files = files + fil.getName() + "\t";
	            }
	        }
	    }
	public void run() {
		boolean b=true;
		while (b) {
			DataInputStream din;
			try {
				din = new DataInputStream(s2.getInputStream()); 
				String str= din.readUTF();
				System.out.println(str);
				
				int j=0;
				String[] token= new String[str.length()];
				st= new StringTokenizer(str," ");	
				while (st.hasMoreElements()) {
					token [j]=	st.nextToken();
					j++;
				}
				if (token[0].equalsIgnoreCase("queryfile")) {
					System.out.println("In queryfile");
					found = false;
					findFile(token[1],new File(directory));
					
					if(found){
						System.out.println("File found in my local directory");
						//Replying
						DataOutputStream dout= new DataOutputStream(s2.getOutputStream());
						Socket testSocket= new Socket("8.8.8.8",53);
						String ipAddr = testSocket.getLocalAddress().getHostAddress();
						dout.writeUTF(token[1]+"; filefound;"+ ipAddr+";"+ P2pGnutella.udpPort );
						dout.flush();
					}
					else{
						ConnectionList.MsgForward(str, s2);
					}
				}
				else if((token[0].equalsIgnoreCase("queryallfiles"))){
					System.out.println("queryallfiles Request received");
					DataOutputStream dout= new DataOutputStream(s2.getOutputStream());
					listOfFiles(new File(directory));
					System.out.println(files);
					dout.writeUTF("FilesList: "+files);
					dout.flush();
					
				}else{
					
					
				}
				
					
				//if (str==null) {
				//din.close();
				//}
			}
			catch (IOException e) {
				e.printStackTrace();
				b=false;
				//din.close();
			}
		}
	}
}