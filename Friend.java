import java.net.*;
import java.io.*;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Friend
{
    private String name;
    private String address;
    private boolean ismyfriend; // Si l'autre est connecte a moi.
    private boolean amherfriend; // Si je suis connecte a l'autre.

    public Friend[] tfriends;

    public Friend(String name, String address, boolean ismyfriend, boolean amherfriend)
    {
	this.name = name;
	this.address= address;
	this.ismyfriend = ismyfriend;
	this.amherfriend = amherfriend;
    }

    /*public Friend()
    {
	this.name = null;
	this.address = null;
	this.ismyfriend = false;
	this.amherfriend = false;
	}*/
    
    /* retourne si l'autre me suit */
    public boolean doesFollow()
    {
	return this.amherfriend;
    }
    
    /* retourne si je suis l'autre */
    public boolean amFollowed()
    {
	return this.ismyfriend;
    }

    /* Pour suivre quelqu'un */
    public void followHer()
    {
	this.amherfriend = true;
    }
    
    /* pour ne plus suivre quelqu'un */
    public void unfollowHer()
    {
	this.amherfriend = false;
    }

    private static String getValue(String tag, Element element) 
    {
	NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
	Node node = (Node) nodes.item(0);
	return node.getNodeValue();
    }
    
    public void initFriends(String filename)
    {
	Document doc;

	File annuaire = new File(filename);
	try {
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    doc = db.parse(annuaire);
	    
	    doc.getDocumentElement().normalize();
	
	    NodeList nodes = doc.getElementsByTagName("annuaire");
	
	    int nbfriends = nodes.getLength();
	
	    tfriends = new Friend[nbfriends];
	
	    for (int i = 0; i < nbfriends; i++)
		{
		    Node node = nodes.item(i);
		    if (node.getNodeType() == Node.ELEMENT_NODE)
			{
			    Element el = (Element) node;
			    tfriends[i] = new Friend(getValue("name", el),
						     getValue("address", el),
						     Boolean.parseBoolean(getValue("ismyfriend", el)),
						     Boolean.parseBoolean(getValue("amherfriend", el)));
			}
		}
	} catch (Exception e) { e.printStackTrace();}
	    
	//	return tfriends;
    }

    private Friend[] createFriend(int n)
    {
	return new Friend[n];
    }
}
