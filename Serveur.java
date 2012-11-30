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

public class Serveur extends Start{

	public static final int port = 5234;
	private String newStatus;
	public Interface ex;

	public Serveur(){}

	public void run(){
		ex = new Interface();
		ex.setVisible(true);
		listener();
	}

	/* Je vais envoyer status, en public si pub est true, en private sinon. */
	public static void postStatus(String status, boolean pub){
		Socket s;
		String data = "10" + status;
		for (int i = 0; i < nb; i++)
		{
			/* Si mon statut est public, je l'envoie à ceux qui me suivent, 
		 sinon, je l'envoie à ceux qui me suivent et que je suis. */
			if ((pub & tfr[i].amFollowed()) | (!(pub) & tfr[i].amFollowed() & tfr[i].doFollow()))
			{
				try {
					s = new Socket(address[i], port);
					OutputStream os = s.getOutputStream();
					PrintStream ps = new PrintStream(os, false, "utf-8");
					ps.println(data);
					ps.flush();
					ps.close();
					s.close();
				} catch (Exception e){}
			}
			/* Si on a une exception, c'est que l'un de nos contacts n'est pas connecté. 
		 On continue quand même, parce qu'on est des fous. */
		}
	}

	public static void demandeAmis(InetAddress address){
		String data = "20";
		creationSocket(address, data);
	}
	
	

	public static void demandeListeAmis(InetAddress address){
		String data = "22";
		creationSocket(address, data);
	}

	public static void reponseAmis(InetAddress address){
		String data = "21";
		creationSocket(address, data); //ajoter status + commentaires
	}

	public static void demandeStatus(InetAddress address){
		String data = "30";
		creationSocket(address, data);
	}


	public static void postCommentaire(InetAddress address, String commentaire){
		String data = "40" + commentaire;
		creationSocket(address, data);
	}
	
	public static void reponseAmis(InetAddress address, boolean reponse){
		if (reponse){
			//String data = status public + status privé + liste amis
			//creationSocket(address, data);
		}
		else {
			//String data = status public + liste amis
			//creationSocket(address,data);
		}
	}


	/*7public static void postImage(InetAddress address, String commentaire){
		String data = "40" + commentaire;
		creationSocket(address, data);
	}*/

	public static void creationSocket(InetAddress address, String data){
		Socket s;
		try{
			s = new Socket(address, port);
			OutputStream os = s.getOutputStream();
			PrintStream ps = new PrintStream(os, false, "utf-8");
			ps.println(data);
			ps.flush();
			ps.close();
			s.close();
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
							analyseData(bb);
							key.cancel();
						}
					}
				}
				keys.clear();
			}
		}catch (Exception e){}
	}

	public void analyseData(ByteBuffer bb){
		byte[] buff = new byte[bb.remaining()];
		bb.get(buff);
		String receiveData = new String(buff);

		String so1 = receiveData.substring(0, 1);
		int o1 = Integer.parseInt(so1);
		switch(o1){
		case 1 :
			System.out.println("ici");
			newStatus = receiveData.substring(2);
			printStatus(newStatus);
			break;
		case 2 :
			String so2 = receiveData.substring(1, 2);
			int o2 = Integer.parseInt(so2);
			switch(o2){
			case 0 :
				//Demande d'amis
				break;
			case 1 :
				//Réponse amis + envoi liste d'amis + status
				break;
			case 2 :
				//Demande liste d'amis
				break;
			}
			break;
		case 3 :
			char o3 = receiveData.charAt(1);
			switch(o3){
			case 0 :
				//Demande status
				break;
			case 2 :
				//Envoi status
				break;
			}
			break;
		case 4 :
			//commentaire
			break;
		case 5 :
			//Image
			break;   
		}
	}


	/*public String readStatus (ByteBuffer bb){
		try{
			byte[] buff = new byte[bb.remaining()];
			bb.get(buff);
			String newStatus = new String(buff);
			printStatus(newStatus);

		}catch (Exception e){}
		return newStatus;
	}*/

	public void printStatus(String newStatus){
		ex.himStatus(newStatus);
	}
}