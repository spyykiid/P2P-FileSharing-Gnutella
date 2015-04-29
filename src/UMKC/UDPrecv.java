package UMKC;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.StringTokenizer;

class UDPrecv implements Runnable 
{
	public DatagramSocket udpSock;
	public static String filename ="";
	DatagramPacket receivePacket = null;
	UDPrecv(DatagramSocket udpSock) throws IOException
	{
		this.udpSock = udpSock;
	}
	UDPrecv()
	{	
	}
	public void run()
	{
		while(true)
		{
			try
			{
				byte[] receiveData = new byte[1024];
				 receivePacket = new DatagramPacket(receiveData, receiveData.length);
				udpSock.receive(receivePacket); 
				String message = new String(receivePacket.getData());
				
				if(message.contains("ping")){
					Messaging.send_udp(receivePacket.getAddress().toString().substring(1),8000,"pong");
					System.out.println("Ping from IP" +receivePacket.getAddress());
				}else if(message.contains("pong")){
					System.out.println("PONG from "+receivePacket.getAddress().toString());	
					
				}else if(message.contains("download")){
					int i=5, j=0;
					String[] token= new String[i];
					StringTokenizer	st= new StringTokenizer(message," ");	
					while (st.hasMoreElements()) {
						token [j]=	st.nextToken();
						j++;
					}
					
					PacketSend(token[1]);
					System.out.println("Download will be started");
				}
				else {
					  byte b[]=new byte[3072];
                      FileOutputStream f=new FileOutputStream("D:\\sharedFolder\\"+filename);                
                                  System.out.println("server waiting to recieve packet");
                                  System.out.println("pacet recieved");
                                  System.out.println(new String(receivePacket.getData(),0,receivePacket.getLength()));  
                                  f.write(receivePacket.getData(), 0, receivePacket.getLength());
                     
				}
			}
			catch(IOException e)
			{
				System.out.println("IOException run() UDPrecv");
				//e.printStackTrace();
			}
		}
	}
	
	void PacketSend(String filename) throws IOException{
		String sharedFolder = "D:/sharedFolder/"+filename;
		 byte b[]=new byte[1024];
		 System.out.println("Searching for the file at "+sharedFolder+filename);
         FileInputStream f=new FileInputStream("D:/sharedFolder/sample.txt");
        // DatagramSocket dsoc=new DatagramSocket(2000);
         int i=0;
         while(f.available()!=0)
         {
                     b[i]=(byte)f.read();
                     i++;
         }                     
         f.close();
         udpSock.send(new DatagramPacket(b,i,receivePacket.getAddress(),8000));
         System.out.println("Download Packet sent");
	}
}