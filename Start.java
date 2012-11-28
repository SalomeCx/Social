import java.net.InetAddress;

public class Start extends Interface{

    public static InetAddress[] address;
    public static int nb;
    public static Friend[] tfr;

    public static void main(String[] args){
	try {
	    tfr = Friend.initFriends("annuaire.xml");
	} catch (Exception e) { System.err.println("Parser or SAX exception : " + e); }
	nb = args.length;
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



