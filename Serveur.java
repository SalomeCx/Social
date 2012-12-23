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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Serveur extends Start {

	public static final int port = 5234; //port par défaut
	private String newStatus;
	private String newCommentaire;
    
	public Serveur() {
	}

	//Lancer la socket d'écoute et dire aux autres utilisateurs que je suis connecté.
	public void run() {
		conect();
		listener();
	}
	


	/* Je vais envoyer status, en public si pub est true, en private sinon. */
	public static void postStatus(String status, boolean pub) {
		Socket s;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
		Date date = new Date();
		String data = "10" + System.getProperty("user.name") + "##" + dateFormat.format(date) + "##" + status;
		for (int i = 0; i < nb; i++) {
			/*
			 * Si mon statut est public, je l'envoie à ceux qui me suivent,
			 * sinon, je l'envoie à ceux qui me suivent et que je suis.
			 */
			
			if ((pub & tfr[i].amFollowed())
					| (!(pub) & tfr[i].amFollowed() & tfr[i].doFollow())) {
		
				try {
					s = new Socket(address[i], port);
					OutputStream os = s.getOutputStream();
					PrintStream ps = new PrintStream(os, false, "utf-8");
					ps.println(data);
					ps.flush();
					ps.close();
					s.close();
					

				} catch (Exception e) {
				}
			}
			/*
			 * Si on a une exception, c'est que l'un de nos contacts n'est pas
			 * connecté. On continue quand même, parce qu'on est des fous.
			 */
		}
	}
	
	//Fonction pour dire que je suis connecté aux autres utilisateurs.
	public static void conect(){
		try{
			String data = "00" + System.getProperty("user.name") + "##" + InetAddress.getLocalHost().toString();

			for (int i = 0; i < nb; i++) {
				creationSocket(address[i], data);

			}
		}catch(Exception e){}
	}

	
	/* Supprimer un ami. ! Suppression de toutes les liaisons, il n'est pas possible de l'empêcher
	de voir vos statuts mais de l'espionner quand même. */
	public static void supprAmi(String ami)
	{
		XmlTreat.treatFriend(ami, "false", "false");
		for (int i = 0; i < tfr.length; i++)
		{
			if (ami.equals(tfr[i].getName()))
			{
				tfr[i].beUnfollowed();
				tfr[i].unfollow();
				break;
			}
		}
	}




	//post de Commentaire
	public static void postCommentaire(InetAddress address, String commentaire) {
		String data = "11" + commentaire;
		creationSocket(address, data);
	}



	//Demande d'amis
	public static void demandeAmis(InetAddress address){
		String data = "20" + System.getProperty("user.name") + "##" + address.getHostName();
		creationSocket(address, data);
		
	}
	
	//Post d'image
	public static void postImage(InetAddress address, String image) {
		String data = "40" + "##" + image;
		creationSocket(address, data);
	}



	//Création d'une socket pour envoyer le String "data" à l'addresse "address"
	public static void creationSocket(InetAddress address, String data) {
		Socket s;
		try {
			s = new Socket(address, port);
			OutputStream os = s.getOutputStream();
			PrintStream ps = new PrintStream(os, false, "utf-8");
			ps.println(data);
			ps.flush();
			ps.close();
			s.close();
		} catch (Exception e) {
		}
	}

	//Socket d'écoute: select.
	//On envoit le String reçu à la class de traitement "Protocole" et "Splitter"
	//puis on fait appel aux fonctions d'affichage pour afficher dans l'interface.
	public void listener() {
		try {
			ServerSocketChannel ssc = ServerSocketChannel.open();
			ServerSocket server = ssc.socket();
			Selector selector = Selector.open();

			server.bind(new InetSocketAddress(port));
			ssc.configureBlocking(false);
			ssc.register(selector, SelectionKey.OP_ACCEPT);

			while (true) {
				selector.select();
				Set keys = selector.selectedKeys();
				Iterator it = keys.iterator();

				while (it.hasNext()) {
					SelectionKey key = (SelectionKey) it.next();

					if (key.isAcceptable()) {
						Socket listen = server.accept();
						SocketChannel sc = listen.getChannel();
						sc.configureBlocking(false);
						sc.register(selector, SelectionKey.OP_READ);
					}

					if (key.isReadable() && key.isValid()) {
						ByteBuffer bb = ByteBuffer.allocate(512);
						SocketChannel st = (SocketChannel) key.channel();
						int byteRead = st.read(bb);
						bb.flip();
						if (byteRead == -1) {
							key.cancel();
							st.close();

						} else {
							byte[] buff = new byte[bb.remaining()];
							bb.get(buff);
							String receiveData = new String(buff);
							System.out.println(receiveData);
							int reqcode = Integer.parseInt(receiveData.substring(0, 2));
							String data = receiveData.substring(2);
							Protocole.treatmentProtocol(reqcode, data);
							key.cancel();
						}
					}
				}
				keys.clear();
			}
		} catch (Exception e) {}
	}

}