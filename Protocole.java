
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;



public class Protocole extends Start {

	private Protocole(){}
	//Boucle qui fait appel aux fonctions de traitement en fonction de l'en-tete de début.
	public static void treatmentProtocol(int reqcode, String dataReceived){
		for(;;){
			if(connect(reqcode, dataReceived)) break;

			if(statusReceived(reqcode, dataReceived)) break;

			if(commentReceived(reqcode, dataReceived)) break;

			if(friendRequest(reqcode, dataReceived)) break;

			if(friendYesAnswer(reqcode, dataReceived)) break;

			if(friendNoAnswer(reqcode, dataReceived)) break;

			if(imageStatus(reqcode, dataReceived)) break;

			if(imageProfile(reqcode, dataReceived)) break;

			if(errorMessage(reqcode, dataReceived)) break;

			break;
		}
	}

	private static boolean connect(int reqcode, String dataReceived){
		if( reqcode != 00)
			return false;
		Hashtable<String, String> dataTable = Splitter.connect(dataReceived);
		System.out.println("ici");
		ex.connecter.setText(dataTable.get("Name") + " est connecté");
		ex.connect.setSize(400, 250);
		ex.connect.setVisible(true);
		System.out.println("la");
		return true;
	}


	private static boolean statusReceived(int reqcode, String dataReceived){
		if (reqcode != 10)
			return false;
		Hashtable<String, String> dataTable = Splitter.status(dataReceived);
		String newStatus = dataTable.get("Date") + " " + dataTable.get("Name") + " " + ">" + " " + dataTable.get("Status");
		Start.printStatus(newStatus);
		return true;
	}

	private static boolean commentReceived(int reqcode, String dataReceived){
		if (reqcode != 11)
			return false;
		Hashtable<String, String> dataTable = Splitter.comment(dataReceived);
		//Affichage commentaires

		return true;
	}

	private static boolean friendRequest(int reqcode, String dataReceived){
		if (reqcode != 20)
			return false;
		final Hashtable<String, String> dataTable = Splitter.friendRequest(dataReceived);
		ex.demande.setText(dataTable.get("NewFriendName") + " vous a ajouté comme ami(e). Voulez-vous?");
		ex.demandeAmi.setSize(400, 250);
		ex.demandeAmi.setVisible(true);
    	ex.ouiOui.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Start.ajouterAmisDansListe(dataTable.get("NewFriendName"));
				System.out.println(dataTable.get("NewFriendHost"));
				
				String reponse = "21" + System.getProperty("user.name");

				ex.demandeAmi.setVisible(false);
				try{
					System.out.println(InetAddress.getByName(dataTable.get("NewFriendHost")));
					Serveur.creationSocket(InetAddress.getByName(dataTable.get("NewFriendHost")), reponse);
				}catch (Exception e){}
			
				XmlTreat.treatFriend(dataTable.get("NewFriendName"), "true", "true");
				
			}});
    	
    	ex.ouiNon.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent event) {
    			String reponse = "22" + System.getProperty("user.name");
    			try{
    				System.out.println(dataTable.get("NewFriendHost"));
    				Serveur.creationSocket(InetAddress.getByName(dataTable.get("NewFriendHost")), reponse);
    			}catch (Exception e){}
    			ex.demandeAmi.setVisible(false);
    			XmlTreat.treatFriend(dataTable.get("NewFriendName"), "false", "true");
    		}
    	});
    	
    	ex.nonNon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				ex.demandeAmi.setVisible(false);
				XmlTreat.treatFriend(dataTable.get("NewFriendName"), "false", "false");
					}
		});
		return true;
	}

	private static boolean friendYesAnswer(int reqcode, String dataReceived){
		if (reqcode != 21)
			return false;
		Hashtable<String, String> dataTable = Splitter.friendResponse(dataReceived);
		//ajouter liste d'amis + status + commentaires
		return true;
	}

	private static boolean friendNoAnswer(int reqcode, String dataReceived){
		if (reqcode != 22)
			return false;
		//ajouter liste d'amis
		Hashtable<String, String> dataTable = Splitter.friendResponse(dataReceived);
		return true;
	}

	private static boolean imageStatus(int reqcode, String dataReceived){
		if (reqcode != 40)
			return false;
		Hashtable<String, String> dataTable = Splitter.image(dataReceived);
		return true;
	}

	private static boolean imageProfile(int reqcode, String dataReceived){
		if (reqcode != 41)
			return false;
		Hashtable<String, String> dataTable = Splitter.image(dataReceived);
		return true;
	}

	private static boolean errorMessage(int reqcode, String dataReceived){
		if (reqcode != 99)
			return false;
		Hashtable<String, String> dataTable = Splitter.errorMessage(dataReceived);
		return true;
	}



}