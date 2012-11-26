import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.*;
import java.util.Iterator;
import java.util.Set;
import java.io.*;

public class Start{
	public static final int port = 5234; 
	private String newStatus;
	//private static int port;
	private static InetAddress[] address;
	public Interface ex;
    private static int nb;
	
    private Charset charset = Charset.forName("UTF-8");
    private CharsetEncoder encoder = charset.newEncoder();
    private CharsetDecoder decoder = charset.newDecoder();

	public static void main(String[] args){
	    nb = args.length;
		if(nb > 0){
			try{
			    for (int i = 0; i < nb; i++)
				{
				    address[i] = InetAddress.getByName(args[i]);
				//port = Integer.parseInt(args[1]);
				}
				}catch(Exception e){}
		}
		else{
			//port=0;
			address=null;
			System.err.println("pas le bon nombre d'arguments.");
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
		    for (int i = 0; i < nb; i++)
			{
			    s = new Socket(address[i], port);
			    try{
				OutputStream os = s.getOutputStream();
				PrintStream ps = new PrintStream(os, false, "utf-8");
				ps.println(status);
				ps.flush();
				ps.close();
				s.close();
			    }catch(Exception e){}
			}
		}catch (Exception e){}
	}

	public void listener (){
		
		try{
			ServerSocketChannel ssc = ServerSocketChannel.open();
			ServerSocket server = ssc.socket();
			Selector selector = Selector.open();
			
			server.bind(new InetSocketAddress(port));
			ssc.configureBlocking(false);
			ssc.register(selector, SelectionKey.OP_ACCEPT);
			
			while(true){
				selector.select();
				Set keys = selector.selectedKeys();
				Iterator it = keys.iterator();
				
				while (it.hasNext()){
					SelectionKey key = (SelectionKey) it.next();
				
					if (key.isAcceptable()){
						Socket listen = server.accept();
						SocketChannel sc = listen.getChannel();
						sc.configureBlocking(false);
						sc.register(selector, SelectionKey.OP_READ);
					}
					
					if(key.isReadable() && key.isValid()){
						ByteBuffer bb = ByteBuffer.allocate(512);
						SocketChannel st = (SocketChannel) key.channel();
						int byteRead = st.read(bb);
						bb.flip();
						if (byteRead == -1){
							key.cancel();
							st.close();
							
						}
						else {
							readStatus(bb);
							key.cancel();
						}
							//System.out.println("truc ="+truc+"\n");
														
	
	
					}
					
					
				}
				keys.clear();

			}
		}catch (Exception e){}
	}


	public String readStatus (ByteBuffer bb){
		try{
			byte[] buff = new byte[bb.remaining()];
			//System.out.println(buff);
			bb.get(buff);
			//System.out.println(bb);
			String newStatus = new String(buff);
			System.out.println(newStatus);
			printStatus(newStatus);

		}catch (Exception e){}
		return newStatus;
	}

	public void printStatus(String newStatus){
		ex.himStatus(newStatus);
	}
}