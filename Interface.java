import java.awt.GridLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.Date;
import java.util.Calendar;

import java.io.*;

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

public class Interface extends JFrame {

	static JPanel panel, me, him;
	static JTextField postText;

	public Interface() {
		initInterface();
	}

	public void himStatus(String status){
		JLabel label = new JLabel("New status > " + status);
		him.add(label);	
		panel.revalidate();
	}
	
	public final void initInterface() {
		JLabel label;

		panel = new JPanel();
		getContentPane().add(panel);
		/* Afficher d'abord la zone de post, puis les gens */
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		/* Une zone de texte et un bouton pour poster */
		postText = new JTextField(40);
		postText.setMaximumSize(new Dimension(Integer.MAX_VALUE, postText.getMinimumSize().height));
		panel.add(postText);
		JButton postButton = new JButton("Post");

		postButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				/* Ajoute le statut */
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
				Date date = new Date();
				JLabel label = new JLabel(System.getProperty("user.name") + " > " + postText.getText() + " - [" + dateFormat.format(date) + "]");
				Start.postStatus(postText.getText());
				postText.setText("");
				me.add(label);
				/* Et redessine */
				panel.validate();
			}	
		});

		postButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(postButton);

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
		people.add(Box.createRigidArea(new Dimension(5,0)));

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