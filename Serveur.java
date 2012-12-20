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

	//Lancer la socket d'écoute
	public void run() {
		listener();
	}
	


	/* Je vais envoyer status, en public si pub est true, en private sinon. */
	public static void postStatus(String status, boolean pub) {
		Socket s;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
		Date date = new Date();
		String data = "10" + System.getProperty("user.name") + "_&§&_" + dateFormat.format(date) + "_&§&_" + status;
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





	//post de Commentaire
	public static void postCommentaire(InetAddress address, String commentaire) {
		String data = "40" + commentaire;
		creationSocket(address, data);
	}



	//Demande d'amis
	public static void demandeAmis(InetAddress address){
		String data = "20" + System.getProperty("user.name") + "_&§&_" + address.getHostName();
		creationSocket(address, data);
		
	}
	
	//Post d'image
	public static void postImage(InetAddress address, String image) {
		String data = "40" + image;
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
	//On envoit le String reçu à la fonction qui analyse le String: analayseData.
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
							analyseData(bb);
							key.cancel();
						}
					}
				}
				keys.clear();
			}
		} catch (Exception e) {}
	}


	
	//Fonction analyseData qui fait appel aux différentes fonctions de traitement par rapport à l'en-tete du String. 
	public void analyseData(ByteBuffer bb) {
		byte[] buff = new byte[bb.remaining()];
		bb.get(buff);
		String receiveData = new String(buff);
		System.out.println(receiveData);
		String so1 = receiveData.substring(0, 1);
		int o1 = Integer.parseInt(so1);
		switch (o1) {
		case 1:
			String s001 = receiveData.substring(1, 2);
			int o11 = Integer.parseInt(s001);
			switch (o11) {
			case 0:
				newStatus = receiveData.substring(2);
				Traitement.traiterStatusAmisRecu(newStatus);
				break;
			case 1:
				newCommentaire = receiveData.substring(2);
				printCommentaire(newCommentaire); // envoi commentaire
				break;
			}
			break;
		case 2:
			String so2 = receiveData.substring(1, 2);
			String s002 = receiveData.substring(1, 2);
			int o2 = Integer.parseInt(so2);
			switch (o2) {
			case 0:
				try {
					
					final String[] s = Traitement.traiterDemandeAmisRecu(receiveData.substring(2));
					//final Friend friend = new Friend(s[0], s[1], false, false);
					ex.demande.setText(s[0] + " vous a ajouté comme ami(e). Voulez-vous?");
					ex.ajoutAmi.setSize(400, 250);
					ex.ajoutAmi.setVisible(true);
			    	ex.ouiOui.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent event) {
							Start.ajouterAmisDansListe(s[0]);
							ex.ajoutAmi.setVisible(false);
						
							XmlTreat.treatFriend(s[0], "true", "true");
							
						}});
			    	
			    	ex.ouiNon.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent event) {
							ex.ajoutAmi.setVisible(false);
							XmlTreat.treatFriend(s[0], "false", "true");
								}
					});
			    	
			    	ex.nonNon.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent event) {
							ex.ajoutAmi.setVisible(false);
							XmlTreat.treatFriend(s[0], "false", "false");
								}
					});
			    	
				} catch (Exception e) {
				}
				break;
			case 1:
				String s005 = receiveData.substring(2);
				//traiterData20(s005); // Réponse (+) amis, envoi liste d'amis +
				// status (public et privé) +
				// commentaires
				break;
			case 2:
				String s006 = receiveData.substring(2);
				//traiterData21(s006); // Réponse (-) amis, envoi liste amis +
				// status public
				break;
			case 3:
				try {
					String s003 = receiveData.substring(2);
					//envoieListeAmis(InetAddress.getByName(s003)); // Demande
					// Liste
					// Amis, on
					// envoie la
					// liste
					// d'amis
				} catch (Exception e) {
				}
			case 4:
				String s = "";
				//traiterListeAmisRecu(s); // On recoit la liste d'un amis et on
				// la traite
			}
			break;
		case 3:
			char o3 = receiveData.charAt(1);
			switch (o3) {
			case 0:
				try {
					String s004 = receiveData.substring(2);
					String status = "";
					//envoieStatus(InetAddress.getByName(s004), status); // Demande
					// status
					// ->
					// envoie
					// status
				} catch (Exception e) {
				}
				break;
			case 1:
				String s = "";
				//traiterStatusAmisRecu(s);// On recoit les status de l'ami
				break;
			}
			break;
		case 4:
			// Image
			break;
		}
	}



	public void printCommentaire(String Commentaire) {

	}
}