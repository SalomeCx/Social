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
    private static int nb;
    private static InetAddress[] address;
    public Interface ex;

    public static void main(String[] args){
	nb = args.length;
	address = new InetAddress[nb];
	try{
	    for (int i = 0; i < nb; i++)
		{
		    address[i] = InetAddress.getByName(args[i]);
		}
	}catch(Exception e){}
	Start test = new Start();
       	test.run();
    }
    
    public Start(){}
    
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
		    OutputStream os = s.getOutputStream();
		    PrintStream ps = new PrintStream(os, false, "utf-8");
		    ps.println(status);
		    ps.flush();
		    ps.close();
		    s.close();
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
		    }	    
		}
		keys.clear();
	    }
	}catch (Exception e){}
    }
    
    
    public String readStatus (ByteBuffer bb){
	try{
	    byte[] buff = new byte[bb.remaining()];
	    bb.get(buff);
	    String newStatus = new String(buff);
	    printStatus(newStatus);
	    
	}catch (Exception e){}
	return newStatus;
    }
    
    public void printStatus(String newStatus){
	ex.himStatus(newStatus);
    }
}