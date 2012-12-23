import java.net.InetAddress;
import java.util.StringTokenizer;


public class Commentaire {
	
	
	private String dest;		//Le destinataire
	private String dateStatus;  //On prend la date du status pour lequelle on veut envoyer un commentaire
	private String userName;	//Nom de l'expediteure
	private String commentaire; //Le commentaire



	//Constructeur
	public Commentaire(){
		this.commentaire = "";
		this.dest = "";
		this.userName = "";
		this.dateStatus = "";
	}
	
	
	//Constructeur
	public Commentaire(String UserName, String DateStatus, String Dest, String Comm){
		this.commentaire = Comm;
		this.dest = Dest;
		this.userName = UserName;
		this.dateStatus = DateStatus;
	}
	
	
	//4 Accesseurs
	public String getCommentaire(){
		return this.commentaire;
	}

	public String getDateStatus(){
		return this.dateStatus;
	}
	
	public String getExp(){
		return this.userName;
	}
	
	
	public String getDest(){
		return this.dest;
	}
	
	
	//4 Mutateurs
	public static void setCommentaire(Commentaire comm, String commentaire){
		comm.commentaire = commentaire;
	}
	
	public static void setDateStatus(Commentaire comm, String DateStatus){
		comm.dateStatus= DateStatus;
	}
	
	public static void setExp(Commentaire comm, String Exp){
		comm.userName = Exp;
	}
	
	public static void setDest(Commentaire comm, String Dest){
		comm.dest = Dest;
	}
	
	
	//Fonction qui permet l'envoie de commentaires.
	public void postCommentaire(){
		try{
		String data = "11" + this.userName + "##" + this.dateStatus + "##" + this.dest + "##" + this.commentaire;
		System.out.println(data);
		Serveur.creationSocket(InetAddress.getByName(this.dest), data);
		}catch(Exception e){}
	}
		
}
	
	

