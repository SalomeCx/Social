import java.util.StringTokenizer;

public class Traitement {
	
	
	
	public static void traiterListeAmisRecu(String listeAmis) {
		String[] tab = new String [10];
		int i = 0;
		StringTokenizer data = new StringTokenizer(listeAmis, "_&§&_", false);
		while(data.hasMoreTokens()) {
			tab[i] = data.nextToken("_&§&_");
			i++;
		}
		//afficher liste amis
	}
	
	public static String[] traiterDemandeAmisRecu(String amis){
		String[] tmp2 = new String [2];
		int i = 0;
		StringTokenizer data2 = new StringTokenizer(amis, "_&§&_", false);
		while(data2.hasMoreTokens()) {
			tmp2[i] = data2.nextToken("_&§&_");
			i++;
		}
		System.out.println(tmp2[0]);
		System.out.println(tmp2[1]);
		//return true;
		return tmp2;
		
	}

	public static void traiterListeStatusAmisRecu(String listeAmis) {

	}
	
	public static void traiterStatusAmisRecu(String status){
		StringTokenizer st = new StringTokenizer(status, "_&§&_");
		String tmp = st.nextToken(); 
		Start.printStatus("[" + st.nextToken()+"]"	+ tmp + ">" +st.nextToken());

	}
	
	
}
