import java.net.InetAddress;

public class Start extends Interface{

public static InetAddress[] address;
public static int nb;

public static void main(String[] args){
nb = args.length;
address = new InetAddress[nb];
try{
for (int i = 0; i < nb; i++){
address[i] = InetAddress.getByName(args[i]);
}
}catch(Exception e){}
Serveur serv = new Serveur();
serv.run();
}
}



