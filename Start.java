import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

public class Start{
	public static Friend friend;
    public static InetAddress[] address;
    //public static boolean[] connected; //true si la personne est connectée, false sinon.
    public static int nb;
    public static Friend[] tfr;
    public static Interface ex;
    
    
    
    //On cherche l'addresse de l'utilisateur dans le fichier xml 
    //pour pouvoir lui envoyer un demande d'amis.
    public static String searchAddress(String name){
    	for (int i =0; i < tfr.length; i++)
    	{
    		if (name.equals(tfr[i].getName()))
    			{		
    				return tfr[i].getAddress();
    				
    			}
    	}
    	return "failed";
    }
    
    
    //Affichage des status recu dans l'interface.
    public static void printStatus(String newStatus) {
    	JLabel label = new JLabel(newStatus);
    	ex.them.add(label);
    	ex.them.setLayout(new BoxLayout(ex.them, BoxLayout.Y_AXIS));
		ex.them.setAlignmentY(Component.TOP_ALIGNMENT);
		ex.them.validate();
    	
    }
    

	//Affichage des amis dans l'interface.
	public static void ajouterAmisDansListe(String s){
		JLabel label = new JLabel(s);
		ex.myFriends.add(label);
		ex.myFriends.setLayout(new BoxLayout(ex.myFriends, BoxLayout.Y_AXIS));
		ex.myFriends.setAlignmentY(Component.TOP_ALIGNMENT);
		ex.myFriends.validate();
		
	}
        
    
    

    public static void main(String[] args){
    	
    	ex = new Interface();
    	ex.setVisible(true);
    	final String user = System.getProperty("user.name") + " > ";
    	
  
    	
    	//Affichage de mes propres status dans l'interface.
    	//Ajout d'action pour le bouton Publier qui envoie mon status aux autres 
    	//utilisateur.
    	ex.post.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent event) {
    			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
    			Date date = new Date();
    			JLabel label = new JLabel(user + ex.statut.getText() + " - ["
    					+ dateFormat.format(date) + "]");
    			Serveur.postStatus(ex.statut.getText(), ex.pub.isSelected());
    			ex.statut.setText("");
    			ex.me.add(label);
    			ex.me.setLayout(new BoxLayout(ex.me, BoxLayout.Y_AXIS));
    			ex.me.setAlignmentY(Component.TOP_ALIGNMENT);
    			ex.me.validate();
    		}
    	});
		
		
    	//Bouton ajout d'amis qui ajoute les amis à la liste d'amis
    	//et envoie la demande d'amis.
    	ex.add.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent event) {
    			String friend = ex.addFriends.getText();
    			try{
    				Serveur.demandeAmis(InetAddress.getByName(searchAddress(friend)));
    			}catch(Exception e){}
    			ex.addFriends.setText("Nom d'utilisateur");
    			ex.myFriends.setLayout(new BoxLayout(ex.myFriends, BoxLayout.Y_AXIS));
    			ex.myFriends.setAlignmentY(Component.TOP_ALIGNMENT);
    			ex.myFriends.validate();
    		}
    	});
    	
    	

	try {
	    tfr = Friend.initFriends("annuaire.xml");
	} catch (Exception e) { System.err.println("Parser or SAX exception : " + e); }
	nb = tfr.length;

	address = new InetAddress[nb];
	try{
	    for (int i = 0; i < nb; i++){
		address[i] = InetAddress.getByName(tfr[i].getAddress());
	    }
	}catch(Exception e){ System.err.println(e); }
	Serveur serv = new Serveur();
	serv.run();
    }
}



