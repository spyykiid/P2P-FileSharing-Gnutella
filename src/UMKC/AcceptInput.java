package UMKC;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
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
				System.out.print("$>> ");
				cmd= br.readLine();
				i= cmd.length();
				tokenize(cmd,i);
				}
			}
		catch (IOException e) {
			System.out.println("exception occured");
			}
	}
			void tokenize(String cmd, int i) {
				try{
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
					System.out.println("inside download");
					if(QueryHitList[Integer.parseInt(token[1])-1]!= null){	
						System.out.println("List: "+QueryHitList[Integer.parseInt(token[1])]);
						st= new StringTokenizer(QueryHitList[Integer.parseInt(token[1])-1],";");	
						j=0;
						while (st.hasMoreElements()) {
							token [j]=	st.nextToken();
							j++;
						}
						UDPrecv.filename = token[0];
						//System.out.println("file Name: " +"download "+token[0].split(" ")[0]);
						//System.out.println("Port No: "+Integer.parseInt(token[2]));
						Messaging.send_udp(token[2], Integer.parseInt(token[3]),"download "+token[0]);
						//QueryHitList= new String[20];
					}
				}
				else if (token[0].equalsIgnoreCase("queryallfiles")) {
					//System.out.println("I: "+Integer.parseInt(token[1])+ " " + token[0]);
					new Messaging(ConnectionList.tcpClient[Integer.parseInt(token[1])], token[0]);
					
				}
				else if (token[0].equalsIgnoreCase("disconnect")) {
					ConnectionList.disconnect(Integer.parseInt(token[1]));
				}
				else if (token[0].equalsIgnoreCase("exit")) {
					System.exit(-1);
				}
				
				else {
					System.out.println("No such Commands available, valid commands:");
					System.out.println("connect <ip-address> <tcp-port>");
					System.out.println("listuser");
					System.out.println("ping <ip-address>");
					System.out.println("queryallfiles <id>");
					System.out.println("queryfile <filename>");
					System.out.println("download <Query_id>");
					System.out.println("disconnect <id>");
					System.out.println("exit");
					System.out.println();
				}
				}
				catch(Exception e){
					System.out.println("Invalid input acceptInput()");					
				}
		}
			
			
}