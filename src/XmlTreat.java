import java.io.*;

import java.net.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Attr;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlTreat
{
    /* Permet de modifier le fichier de configuration relativement aux liens d'amitié.
    * Ce code a été fortement inspiré de la page suivante:
    * http://www.mkyong.com/java/how-to-modify-xml-file-in-java-dom-parser/
    * Soit je reçois une demande d'ami à laquelle je réponds deux fois positivement (nous devenons amis dans les deux sens) -> je passe à cette fonction "true" et "true" 
    * Soit j'accepte qu'il lise mes status publics mais pas privés -> je passe "false" et "true" 
    * Soit je n'accepte pas la demande -> je passe "false" et "false" (Car j'estime ne pas avoir le droit de suivre quelqu'un si l'on ne le laisse pas nous suivre)
    * Soit je veux le supprimer de mes amis -> je passe à nouveau "false" et "false", pour les mêmes raisons que ci-dessus. */
    static void treatFriend(String friendName, String ismy, String amher)
    {
	try {
	    String filepath = "annuaire.xml";
	    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	    Document doc = docBuilder.parse(filepath);
	    
	    Node annuaire = doc.getFirstChild();
  
	    NodeList friend = doc.getElementsByTagName("friend");
 
	    for (int i = 0; i < friend.getLength(); i++) 
		{
		    Node node = friend.item(i);
		    if (node.getNodeType() == Node.ELEMENT_NODE)
			{
			    Element el = (Element) node;
			    Attr a = el.getAttributeNode("name");
			    String name = a.getValue();
			    if (friendName.equals(name))
				{
				    node.getFirstChild().getNextSibling().getNextSibling().getNextSibling().setTextContent(ismy);
				    node.getLastChild().getPreviousSibling().setTextContent(amher);
				}
			}   
		}
	    // write the content into xml file
	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    Transformer transformer = transformerFactory.newTransformer();
	    DOMSource source = new DOMSource(doc);
	    StreamResult result = new StreamResult(new File(filepath));
	    transformer.transform(source, result);
 
	} catch (ParserConfigurationException pce) {
	    pce.printStackTrace();
	} catch (TransformerException tfe) {
	    tfe.printStackTrace();
	} catch (IOException ioe) {
	    ioe.printStackTrace();
	} catch (SAXException sae) {
	    sae.printStackTrace();
	}
    }

}