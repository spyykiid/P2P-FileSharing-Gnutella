package UMKC;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;


class AcceptInput implements Runnable {
	static int hitindex=0;
	String cmd;
	int i,j;
	StringTokenizer st= null;
	String[] cl_ip=new String[20];
	String[] cl_host=new String[20];
	int[] cl_port=new int[20];
	int[] cl_remote=new int[20];
	
	static String[] QueryHitList = new String[20];
	
	int index=0;
	public void run () {
		//System.out.println("t2");
		try {
			while (true) {
				BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
				cmd= br.readLine();
				i= cmd.length();
				tokenize(cmd,i);
				}
			}
		catch (IOException e) {
				e.printStackTrace();
			}
	}
			void tokenize(String cmd, int i) {
				j=0;
				String[] token= new String[i];
				st= new StringTokenizer(cmd," ");	
				while (st.hasMoreElements()) {
					token [j]=	st.nextToken();
					j++;
				}
				if (token[0].equalsIgnoreCase("connect")) {
					ConnectionList.AcceptConn(token[1],Integer.parseInt(token[2]));	
				}
				else if (token[0].equalsIgnoreCase("listuser")) {
					ConnectionList.show();
				}
				else if (token[0].equalsIgnoreCase("ping")) {
					Messaging.send_udp(token[1],8000, token[0]);
				}
				else if (token[0].equalsIgnoreCase("queryfile")) {
					int k=0;
					String s= "";
					while (token[k] != null) {
						s = s + token[k]+ " ";
						k++;
					}
					System.out.println("Query request: "+ s);
					ConnectionList.Msg(s); //send()
				}
				else if (token[0].equalsIgnoreCase("download")) {
					
					if(QueryHitList[Integer.parseInt(token[1])]!= null){		
						st= new StringTokenizer(QueryHitList[Integer.parseInt(token[1])],"; ");
						j=0;
						while (st.hasMoreElements()) {
							token [j]=	st.nextToken();
							j++;
						}
						UDPrecv.filename = token[0].split(" ")[0];
						System.out.println("file Name: " +UDPrecv.filename);
						Messaging.send_udp(token [1], Integer.parseInt(token[2]),"download "+token[0].split(" ")[0]);
						QueryHitList= new String[20];
					}
				}
				else if (token[0].equalsIgnoreCase("sendto")) {
					int m=3;
					String s= "";
					while (token[m] != null) {
						s = s + token[m] + " ";
						m++;
					}
					Messaging.send_udp(token[1], Integer.parseInt(token[2]), s);
				}
				else if (token[0].equalsIgnoreCase("disconnect")) {
					ConnectionList.disconnect(Integer.parseInt(token[1]));
				}
				else if (token[0].equalsIgnoreCase("exit")) {
					System.exit(-1);
				}
				else if (token[0].equalsIgnoreCase("info")) {
					ConnectionList.info();
				} 
				else {
					System.out.println("No such Commands available, valid commands:");
					//System.out.println("info");
					System.out.println("connect <ip-address> <tcp-port>");
					//System.out.println("show");
					//System.out.println("queryfile <filename>");
					//System.out.println("sendto <ip-address> <udp-port> <message>");
					//System.out.println("disconnect <conn-id>");
					//System.out.println("exit");
					System.out.println();
				}
		}
}