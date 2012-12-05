import java.net.InetAddress;


public class Commentaire {
	
	
	private String commentaire;
	private String exp;
	private String dest;
	public static String[] data2 = new String[10];



	public Commentaire(){
		this.commentaire = "";
		this.dest = "";
		this.exp = "";
	}
	
	public Commentaire(String commentaire, String Dest, String Exp){
		this.commentaire = commentaire;
		this.dest = Dest;
		this.exp = Exp;
	}
	
	
	
	public static String getCommentaire(Commentaire comm){
		return comm.commentaire;
	}
	
	public static String getExp(Commentaire comm){
		return comm.exp;
	}
	
	
	public static String getDest(Commentaire comm){
		return comm.dest;
	}
	
	public static void setCommentaire(Commentaire comm, String commentaire){
		comm.commentaire = commentaire;
	}
	
	public static void setExp(Commentaire comm, String Exp){
		comm.exp = Exp;
	}
	
	public static void setDest(Commentaire comm, String Dest){
		comm.dest = Dest;
	}
	
	public static void postCommentaire(Commentaire comm){
		try{
		String data = "40" + comm.dest + comm.exp + comm.commentaire ;
		Serveur.creationSocket(InetAddress.getByName(comm.dest), data);
		}catch(Exception e){}
	}

	public static void commentaireRecu(String data, int pos, int num){
		//a finir
		String stop = "";
		if (data.length() == 0){
			//System.out.println("fini");

		}

		else
		{for (int i = 0; i<pos; i++){
			//System.out.println("fini");
			stop.concat(String.valueOf(data.charAt(i)));
			if (data.charAt(i) == ':'){
				commentaireRecu(data.substring(i),i,num);
				data2[num] = stop;
			}
		}
		}


	}}
	
	

