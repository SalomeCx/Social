import java.net.*;
import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Attr; 

public class Friend
{
    private String name;
    private String address;
    private boolean ismyfriend; /* Je suis l'autre? (Non, je suis moi.) */
    private boolean amherfriend; /* L'autre me suit? */

    public Friend[] tfriends;

    // Constructeur d'ami. 
    public Friend(String name, String address, boolean ismyfriend, boolean amherfriend)
    {
	this.name = name;
	this.address= address;
	this.ismyfriend = ismyfriend;
	this.amherfriend = amherfriend;
    }
    
    // Les accesseurs.
    public boolean amFollowed()
    {
	return this.amherfriend;
    }

    public boolean doFollow()
    {
	return this.ismyfriend;
    }

    public String getAddress()
    {
	return this.address;
    }

    public String getName()
    {
	return this.name;
    }

    // Ne plus suivre quelqu'un.
    public void unfollow()
    {
	this.ismyfriend = false;
    }
    
    // Suivre quelqu'un.
    public void follow()
    {
	this.ismyfriend = true;
    }

    protected static String getValue(String tag, Element element) 
    {
	NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
	Node node = (Node) nodes.item(0);
	return node.getNodeValue();
    }
    
    /* La fonction crée un tableau Friend[] selon le nombre de "friends" dans le fichier annuaire.xml,
     * Si je ne throws pas d'exceptions, je dois faire un try catch dans la fonction, MAIS
     * - Soit je mets l'intégralité de la fonction dans le try catch, et je ne compile pas, parce que le return est dans le try catch.
     * - Soit je mets l'intégralité de la fonction dans le try catch, moins le return, et je ne compile pas parce que mon tableau n'est peut-être pas initialisé.
     * - Soit je mets les deux lignes de code concernées dans le try catch, et comme on les utilise après, ça ne compile pas parce qu'elles peuvent ne pas avoir été initialisées.
     * DONC la solution la plus simple est d'envoyer l'exception au main, parce qu'il fait pas grand chose, il peut quand même s'occuper de ça. */
    public static Friend[] initFriends(String filename) throws Exception
    {
	Document doc;
	
	File annuaire = new File(filename);
	//try {
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	DocumentBuilder db = dbf.newDocumentBuilder();
	doc = db.parse(annuaire);
	    
	doc.getDocumentElement().normalize();
	
	NodeList nodes = doc.getElementsByTagName("friend");
	
	int nbfriends = nodes.getLength();
	Friend[] tfriends = new Friend[nbfriends];
	
	for (int i = 0; i < nbfriends; i++)
	    {
		Node node = nodes.item(i);
		if (node.getNodeType() == Node.ELEMENT_NODE)
		    {
			Element el = (Element) node;
			Attr a = el.getAttributeNode("name");
			String name = a.getValue();
			tfriends[i] = new Friend(name,
						 getValue("address", el),
						 Boolean.parseBoolean(getValue("ismyfriend", el)),
						 Boolean.parseBoolean(getValue("amherfriend", el)));
		    }
	    }
	    //} catch (Exception e) { System.err.println("erreur: " +e);}
	return tfriends;
    }

    public static void addFriend(Friend[] ftab, String name)
    {
	int taille = ftab.length;
	for (int i = 0; i < taille; i++)
	    {
		if (name.equals(ftab[i].getName()))
		    {
		    }
	    }
    }

}
