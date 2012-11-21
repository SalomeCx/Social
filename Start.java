import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class Start{

	private String newStatus;
	private static int port;
	private static InetAddress address;
	public Interface ex;

	public static void main(String[] args){
		if(args.length > 0){
		    try{
			port = Integer.parseInt(args[1]);
			address = InetAddress.getByName(args[0]);}catch(Exception e){}
		}
		else{
			port=0;
			address=null;
		}
		Start test = new Start();
		test.run();
	}

	public Start(){	}

	public void run(){
		ex = new Interface();
		ex.setVisible(true);
		listener();
	}

	public static void postStatus (String status){
		Socket s;
		try{
			s = new Socket(address, port);
			try{
				OutputStream os = s.getOutputStream();
				PrintStream ps = new PrintStream(os, false, "utf-8");
				ps.println(status);
				ps.flush();
				ps.close();
				s.close();
			}catch(Exception e){}
		}catch (Exception e){
			System.out.println("Socket");}
	}

	public void listener (){
		try{
			ServerSocket server = new ServerSocket(5234);
			server.setReuseAddress(true);
			while(true){
				Socket listen = server.accept();
				InputStream is = listen.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));		
				readStatus(br);
				listen.close();
			}
		}catch (Exception e){}
	}


	public void readStatus (BufferedReader br){
		try{
			newStatus = br.readLine();
			printStatus();
			
		}catch (Exception e){}
	}
	
	public void printStatus(){
		ex.himStatus(newStatus);
	}
}