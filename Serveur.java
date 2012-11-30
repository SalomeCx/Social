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

public class Serveur extends Start {

	public static final int port = 5234;
	private String newStatus;
	public Interface ex;
	public boolean decision;
	private String newCommentaire;

	public Serveur() {
	}

	public void run() {
		ex = new Interface();
		ex.setVisible(true);
		listener();
	}

	/* Je vais envoyer status, en public si pub est true, en private sinon. */
	public static void postStatus(String status, boolean pub) {
		Socket s;
		String data = "10" + status;
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

	public static void demandeAmis(InetAddress address) {
		try {
			String data = "20" + InetAddress.getLocalHost().toString();
			creationSocket(address, data);
		} catch (Exception e) {
		}
	}

	public static void demandeListeAmis(InetAddress address) {
		try {
			String data = "22" + InetAddress.getLocalHost().toString();
			creationSocket(address, data);
		} catch (Exception e) {
		}
	}

	public static void envoieListeAmis(InetAddress address) {
		String data = "24"; // + liste amis
		creationSocket(address, data);
	}

	public static void envoieStatus(InetAddress address, String status) {
		String data = "31"; // + status
		creationSocket(address, data);
	}

	public static void demandeStatus(InetAddress address) {
		try {
			String data = "30" + InetAddress.getLocalHost().toString();
			creationSocket(address, data);
		} catch (Exception e) {
		}
	}

	public static void postCommentaire(InetAddress address, String commentaire) {
		String data = "40" + commentaire;
		creationSocket(address, data);
	}

	public static void reponseAmis(InetAddress address, boolean reponse) {
		if (reponse) {
			// String data = "status public + status privé + liste amis +
			// commentaires
			// creationSocket(address, data);
		} else {
			// String data = status public + liste amis
			// creationSocket(address,data);
		}
	}

	public static void traiterListeAmisRecu(String listeAmis) {

	}

	public static void traiterStatusAmisRecu(String listeAmis) {

	}

	public static void postImage(InetAddress address, String image) {
		String data = "40" + image;
		creationSocket(address, data);
	}

	public static void traiterData20(String data) {
		// On envoie status (public et privé) + liste amis
	}

	public static void traiterData21(String data) {
		// On envoie status privé + liste amis
	}

	// public static boolean decisionAmis

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
		} catch (Exception e) {
		}
	}

	public void analyseData(ByteBuffer bb) {
		byte[] buff = new byte[bb.remaining()];
		bb.get(buff);
		String receiveData = new String(buff);
		String so1 = receiveData.substring(0, 1);
		int o1 = Integer.parseInt(so1);
		switch (o1) {
		case 1:
			String s001 = receiveData.substring(1, 2);
			int o11 = Integer.parseInt(s001);
			switch (o11) {
			case 0:
				newStatus = receiveData.substring(2);
				printStatus(newStatus);
				break;
			case 1:
				newCommentaire = receiveData.substring(2);
				printCommentaire(newCommentaire); // envoi commentaire
				break;
			}
		case 2:
			String so2 = receiveData.substring(1, 2);
			String s002 = receiveData.substring(1, 2);
			int o2 = Integer.parseInt(so2);
			switch (o2) {
			case 0:
				try {
					reponseAmis(InetAddress.getByName(s002), decision); // Demande
					// d'amis
				} catch (Exception e) {
				}
				break;
			case 1:
				String s005 = receiveData.substring(2);
				traiterData20(s005); // Réponse (+) amis, envoi liste d'amis +
				// status (public et privé) +
				// commentaires
				break;
			case 2:
				String s006 = receiveData.substring(2);
				traiterData21(s006); // Réponse (-) amis, envoi liste amis +
				// status public
				break;
			case 3:
				try {
					String s003 = receiveData.substring(2);
					envoieListeAmis(InetAddress.getByName(s003)); // Demande
					// Liste
					// Amis, on
					// envoie la
					// liste
					// d'amis
				} catch (Exception e) {
				}
			case 4:
				String s = "";
				traiterListeAmisRecu(s); // On recoit la liste d'un amis et on
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
					envoieStatus(InetAddress.getByName(s004), status); // Demande
					// status
					// ->
					// envoie
					// status
				} catch (Exception e) {
				}
				break;
			case 1:
				String s = "";
				traiterStatusAmisRecu(s);// On recoit les status de l'ami
				break;
			}
			break;
		case 4:
			// Image
			break;
		}
	}

	/*
	 * public String readStatus (ByteBuffer bb){ try{ byte[] buff = new
	 * byte[bb.remaining()]; bb.get(buff); String newStatus = new String(buff);
	 * printStatus(newStatus);
	 * 
	 * }catch (Exception e){} return newStatus; }
	 */

	public void printStatus(String newStatus) {
		ex.himStatus(newStatus);
	}

	public void printCommentaire(String Commentaire) {

	}
}