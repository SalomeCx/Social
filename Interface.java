import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.Date;
import java.util.Calendar;

import java.io.*;
import java.net.InetAddress;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.Box.Filler;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

public class Interface extends JFrame {
	public static boolean decision;
	static JPanel panel, me, him;
	static JTextField postText;
	static JTextField textAmis;
	

	public Interface() {
		initInterface();
	}

	public void himStatus(String status) {
		JLabel label = new JLabel(status);
		him.add(label);
		panel.revalidate();
	}
	
	public static void fenetreAmis(){
		JFrame fenetreAmis = new JFrame();
		JPanel panelAmis = new JPanel();
	    fenetreAmis.setLocationRelativeTo(null);
		
		fenetreAmis.setContentPane(panelAmis);               

	    
		JButton buttonYes = new JButton("Oui");
		JButton buttonNo = new JButton("Non");
		panelAmis.add(buttonYes);
		panelAmis.add(buttonNo);


	    panelAmis.validate();
	    fenetreAmis.setTitle("Amis");
	    fenetreAmis.setSize(400, 200);
	    fenetreAmis.setLocationRelativeTo(null);
	    fenetreAmis.setDefaultCloseOperation(EXIT_ON_CLOSE);
	    fenetreAmis.setVisible(true);
	    
	    
		buttonYes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				decision = true;
				panel.validate();
			}
		});
		
		
		buttonNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				decision = false;
				panel.validate();
			}
		});
	    
	    
	    
	    
	}

	public final void initInterface() {
		JLabel label;

		final String user = System.getProperty("user.name") + " > ";

		final JRadioButton publ = new JRadioButton("public");
		final JRadioButton priv = new JRadioButton("private");
		ButtonGroup pp = new ButtonGroup();

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(300, 100);
		setLayout(new GridLayout());

		panel = new JPanel();
		getContentPane().add(panel);
		/* Afficher d'abord la zone de post, puis les gens */
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		/* Une zone de texte et un bouton pour poster */
		textAmis = new JTextField(40);
		postText = new JTextField(40);
		textAmis.setMaximumSize(new Dimension(Integer.MAX_VALUE, textAmis.getMinimumSize().height));
		postText.setMaximumSize(new Dimension(Integer.MAX_VALUE, postText.getMinimumSize().height));
		

		

		
		JButton addAmis = new JButton("ADD");
		JButton postButton = new JButton("Post");
		getRootPane().setDefaultButton(postButton);


		pp.add(publ);
		pp.add(priv);

		publ.setSelected(true);

		getContentPane().add(publ);
		getContentPane().add(priv);

		postButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				/* Ajoute le statut */
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
				Date date = new Date();
				JLabel label = new JLabel(user + postText.getText() + " - ["
						+ dateFormat.format(date) + "]");
				Serveur.postStatus(user + postText.getText() + " - ["
						+ dateFormat.format(date) + "]", publ.isSelected());
				postText.setText("");
				me.add(label);
				/* Et redessine */
				panel.validate();
			}
		});
		
		
		addAmis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
				//envoie demande d'amis
				try{
				Serveur.demandeAmis(InetAddress.getByName(textAmis.getText()));
				}catch(Exception e){}
				textAmis.setText("");

				

				panel.validate();
			}
		});

		panel.add(postText);
		postButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(postButton);
		
		panel.add(textAmis);
		addAmis.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(addAmis);


		/* Un panel horizontal pour les gens */
		JPanel people = new JPanel();
		panel.add(people);
		people.setAlignmentX(Component.LEFT_ALIGNMENT);
		/* Les personnes sont affichées de gauche à droite */
		people.setLayout(new BoxLayout(people, BoxLayout.X_AXIS));

		/* Moi */
		me = new JPanel();
		me.setBorder(new LineBorder(Color.black));
		/* Mes commentaires sont affichés de haut en bas */
		me.setLayout(new BoxLayout(me, BoxLayout.Y_AXIS));
		me.setAlignmentY(Component.TOP_ALIGNMENT);
		people.add(me);

		/* Une petite séparation entre moi et lui */
		people.add(Box.createRigidArea(new Dimension(5, 0)));

		/* Un ami */
		him = new JPanel();
		him.setBorder(new LineBorder(Color.black));
		him.setLayout(new BoxLayout(him, BoxLayout.Y_AXIS));
		him.setAlignmentY(Component.TOP_ALIGNMENT);
		people.add(him);

		/* De la place pour les autres */
		people.add(Box.createHorizontalGlue());

		/* Le reste de l'interface */
		setTitle("Social");
		setSize(500, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}