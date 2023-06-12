package views;

import java.awt.Font;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import engine.Player;

public class BigView extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Image logo;

	private JPanel playersNames;
	private Image playersBG;
	private JLabel label1;
	private JLabel label2;
	private JTextField field1;
	private JTextField field2;
	private String name1;
	private String name2;
	private JButton playersNamesButton;

	private JPanel allChamps;
	private JLabel label3;
	private JPanel champions;
	private ArrayList<JButton> championsButtons;
	private ArrayList<JOptionPane> championsDetails;

	private JTextArea display1;
	private JTextArea display2;
	private JButton ChampsSelected;

	private JPanel leaderSelect;
	private JLabel label4;
	private JPanel champs1;
	private JTextArea display3;
	private ArrayList<JButton> champs1Buttons;
	private JLabel label5;
	private JPanel champs2;
	private JTextArea display4;
	private ArrayList<JButton> champs2Buttons;
	private JButton startTutorial;

	private JPanel tutorial;
	private JTextArea tutorialText;
	private JButton startGame;
	
	
	public BigView() throws HeadlessException {
		// FRAME
		setTitle("Marvel : Ultimate War");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(150, 10, 1200, 800); // x, y, width, height

		// IMAGES
		playersBG = new ImageIcon("clientBG.png").getImage();
		logo = new ImageIcon("headerIcon.png").getImage();

		// Players Panel
		playersNames = new JPanel();
		playersNames.setSize(1200, 800);
		playersNames.setBackground(Color.BLACK);
		playersNames.setLayout(null);

		// Labels
		label1 = new JLabel("Player 1:");
		label1.setFont(new Font("Fira Code", Font.BOLD, 20));
		label1.setForeground(Color.WHITE);
		label1.setHorizontalAlignment(JLabel.CENTER);
		label1.setBounds(450, 100, 250, 40);

		label2 = new JLabel("Player 2:");
		label2.setFont(new Font("Fira Code", Font.BOLD, 20));
		label2.setForeground(Color.WHITE);
		label2.setHorizontalAlignment(JLabel.CENTER);
		label2.setBounds(450, 200, 250, 40);

		// Text Fields
		field1 = new JTextField("Enter Name 1 ");
		field1.setHorizontalAlignment(JTextField.CENTER);
		field1.setBounds(450, 150, 250, 40);

		field2 = new JTextField("Enter Name 2 ");
		field2.setHorizontalAlignment(JTextField.CENTER);
		field2.setBounds(450, 250, 250, 40);
		// Adding Labels and Fields
		playersNames.add(label1);
		playersNames.add(field1);
		playersNames.add(label2);
		playersNames.add(field2);

		// Start Game Button
		playersNamesButton = new JButton("Lets GOOOO");
		playersNamesButton.setForeground(Color.WHITE);
		playersNamesButton.addActionListener(this);
		playersNamesButton.setFocusable(false);
		playersNamesButton.setBounds(450, 340, 250, 40);
		playersNamesButton.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.WHITE));
		playersNamesButton.setContentAreaFilled(false);
		playersNames.add(playersNamesButton);

		// allChamps Panel
		allChamps = new JPanel();
		allChamps.setLayout(null);
		allChamps.setBackground(Color.BLACK);
		allChamps.setBounds(0, 0, 1200, 800); // x, y, width, height

		label3 = new JLabel("Choose 3 Champions");
		label3.setVerticalAlignment(JLabel.CENTER);
		label3.setBounds(200, 0, allChamps.getWidth(), 40);
		label3.setFont(new Font("Fira Code", Font.BOLD, 20));
		label3.setForeground(Color.WHITE);

		
		champions = new JPanel(new GridLayout(3, 5 , 10 , 10));
		champions.setBounds(0, 150, 1000, allChamps.getHeight() / 2);
		champions.setBackground(Color.BLACK);

		championsButtons = new ArrayList<JButton>();
		for (int i = 0; i < 15; i++) {
			JButton b = new JButton("test" + i); // Game.getAvailableChampions().get(j).toString()
			b.setForeground(Color.WHITE);
			b.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.WHITE));
			b.setContentAreaFilled(false);
			b.setVisible(false);
			b.setFocusable(false);
			championsButtons.add(b);
			champions.add(b);
		}
		
		

		display1 = new JTextArea();
		display1.setText("Selected Champions:" + "\n");
		display1.setBounds(1000, 0, 200, allChamps.getHeight() / 2);
		display1.setEditable(false);
		display1.setFont(new Font("Fira Code", Font.PLAIN, 16));
		display1.setForeground(Color.WHITE);
		display1.setBackground(Color.BLUE);

		display2 = new JTextArea();
		display2.setText("Selected Champions:"+ "\n");
		display2.setBounds(1000, 400, 200, allChamps.getHeight() / 2);
		display2.setEditable(false);
		display2.setFont(new Font("Fira Code", Font.PLAIN, 16));
		display2.setForeground(Color.WHITE);
		display2.setBackground(Color.RED);

		// Champion Selection

		ChampsSelected = new JButton("Select Leader");
		ChampsSelected.setBounds(150, 700, 700, 50);
		ChampsSelected.addActionListener(this);
		ChampsSelected.setFocusable(false);
		ChampsSelected.setVisible(false);
		ChampsSelected.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.BLACK));
		ChampsSelected.setForeground(Color.WHITE);
		ChampsSelected.setContentAreaFilled(false);
		ChampsSelected.setFont(new Font("Fira Code", Font.BOLD, 20));

		allChamps.add(label3);
		allChamps.add(champions);
		allChamps.add(display1);
		allChamps.add(display2);
		allChamps.add(ChampsSelected);

		// Leader Selection Panel
		leaderSelect = new JPanel();
		leaderSelect.setSize(1200, 800);
		leaderSelect.setLayout(null);
		leaderSelect.setBackground(Color.BLACK);

		label4 = new JLabel("Choose your leader");
		label4.setVerticalAlignment(JLabel.CENTER);
		label4.setForeground(Color.WHITE);
		label4.setBounds(200, 0, leaderSelect.getWidth(), 40);
		label4.setFont(new Font("Fira Code", Font.BOLD, 20));

		champs1 = new JPanel(new GridLayout(1, 3, 10, 10));
		champs1.setBounds(300, 60, 600, 100);
		champs1.setBackground(Color.BLACK);
		champs1Buttons = new ArrayList<JButton>();
		for (int i = 0; i < 3; i++) {
			JButton b = new JButton("test" + i); // Game.getAvailableChampions().get(j).toString()
			b.setVisible(false);
			b.setFocusable(false);
			b.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.WHITE));
			b.setForeground(Color.WHITE);
			b.setContentAreaFilled(false);
			champs1Buttons.add(b);
			champs1.add(b);
		}

		label5 = new JLabel("Choose your leader");
		label5.setForeground(Color.WHITE);
		label5.setVerticalAlignment(JLabel.CENTER);
		label5.setBounds(200, 300, leaderSelect.getWidth(), 40);
		label5.setFont(new Font("Fira Code", Font.BOLD, 20));

		champs2 = new JPanel(new GridLayout(1, 3, 10, 10));
		champs2.setBounds(300, 400, 600, 100);
		champs2.setBackground(Color.BLACK);
		champs2Buttons = new ArrayList<JButton>();
		for (int i = 0; i < 3; i++) {
			JButton b = new JButton("test" + i); // Game.getAvailableChampions().get(j).toString()
			b.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.WHITE));
			b.setForeground(Color.WHITE);
			b.setContentAreaFilled(false);
			b.setVisible(false);
			b.setFocusable(false);
			champs2Buttons.add(b);
			champs2.add(b);
		}
		
		display3 = new JTextArea();
		display3.setText("Your Leader:" + "\n");
		display3.setBounds(1000, 0, 200, allChamps.getHeight() / 2);
		display3.setEditable(false);
		display3.setFont(new Font("Fira Code", Font.PLAIN, 16));
		display3.setForeground(Color.WHITE);
		display3.setBackground(Color.RED);

		display4 = new JTextArea();
		display4.setText("Your Leader:"+ "\n");
		display4.setBounds(1000, 400, 200, allChamps.getHeight() / 2);
		display4.setEditable(false);
		display4.setFont(new Font("Fira Code", Font.PLAIN, 16));
		display4.setForeground(Color.WHITE);
		display4.setBackground(Color.BLUE);

		startTutorial = new JButton("Start Tutorial");
		startTutorial.setBounds(200, 700, 600, 40);
		startTutorial.addActionListener(this);
		startTutorial.setFocusable(false);
		startTutorial.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.BLACK));
		startTutorial.setForeground(Color.WHITE);
		startTutorial.setContentAreaFilled(false);
		startTutorial.setVisible(false);
		startTutorial.setFont(new Font("Fira Code", Font.BOLD, 20));

		leaderSelect.add(label4);
		leaderSelect.add(champs1);
		leaderSelect.add(label5);
		leaderSelect.add(champs2);
		leaderSelect.add(display3);
		leaderSelect.add(display4);
		leaderSelect.add(startTutorial);

		// Tutorial Panel
		tutorial = new JPanel();
		tutorial.setSize(1200, 800);
		tutorial.setLayout(null);
		tutorial.setBackground(Color.BLACK);

		tutorialText = new JTextArea();
		tutorialText.setVisible(false);
		tutorialText.setBounds(150, 150, 900, 400);
		tutorialText.setEditable(false);
		tutorialText.setForeground(Color.WHITE);
		tutorialText.setOpaque(false);
		tutorialText.setText(
				"Marvel: Ultimate War is a 2 player battle game. Each player picks 3 champions to form his team\r\n"
						+ "and fight the other player’s team. The players take turns to fight the other player’s champions.\r\n"
						+ "The turns will keep going back and forth until a player is able to defeat all of the other player’s\r\n"
						+ "champions which will make him the winner of the battle." + '\n' + '\n' + '\n'
						+ "Press UP , DOWN , LEFT , RIGHT Buttons to Move" + '\n'
						+ "Press Left-Click to Attack" + '\n'
						+ "Player 1 Champions are Blue while Player 2 is Red");
		tutorialText.setFont(new Font("Fira Code", Font.PLAIN, 20));

		startGame = new JButton("Start Game!");
		startGame.setBounds(250, 700, 700, 50);
		startGame.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.BLACK));
		startGame.setForeground(Color.WHITE);
		startGame.setContentAreaFilled(false);
		startGame.setFocusable(false);
		startGame.setVisible(false);
		startGame.setFont(new Font("Fira Code", Font.BOLD, 20));

		tutorial.add(startGame);
		tutorial.add(tutorialText);

		add(playersNames);
		add(allChamps);
		add(leaderSelect);
		add(tutorial);
		
		
		setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == playersNamesButton) {
			name1 = field1.getText();
			name2 = field2.getText();
			
			label3.setText(name1 + " and " + name2 + " : Choose your 3 Champions");

			for (JButton b : championsButtons)
				b.setVisible(true);

			playersNames.setVisible(false);
			ChampsSelected.setVisible(true);
			champions.setVisible(true);
			allChamps.setVisible(true);
		}
		
		if (e.getSource() == ChampsSelected) {
			for (JButton b : championsButtons)
				b.setVisible(false);

			for (JButton b : champs1Buttons)
				b.setVisible(true);

			for (JButton b : champs2Buttons)
				b.setVisible(true);

			ChampsSelected.setVisible(false);
			allChamps.setVisible(false);
			champions.setVisible(false);
			champs1.setVisible(true);
			champs2.setVisible(true);
			leaderSelect.setVisible(true);
			startTutorial.setVisible(true);
		}

		if (e.getSource() == startTutorial) {
			for (JButton b : champs1Buttons)
				b.setVisible(false);

			for (JButton b : champs2Buttons)
				b.setVisible(false);

			champs1.setVisible(false);
			champs2.setVisible(false);
			leaderSelect.setVisible(false);
			startTutorial.setVisible(false);
			tutorialText.setVisible(true);
			tutorial.setVisible(true);
			startGame.setVisible(true);
		}

		
	}

	/**
	 * @return the playersNames
	 */
	public final JPanel getPlayersNames() {
		return playersNames;
	}

	/**
	 * @return the tutorial
	 */
	public final JPanel getTutorial() {
		return tutorial;
	}

	/**
	 * @return the name1
	 */
	public final String getName1() {
		return name1;
	}

	/**
	 * @return the name2
	 */
	public final String getName2() {
		return name2;
	}
	
	/**
	 * @return the championsButtons
	 */
	public final ArrayList<JButton> getChampionsButtons() {
		return championsButtons;
	}

	/**
	 * @return the championsDetails
	 */
	public final ArrayList<JOptionPane> getChampionsDetails() {
		return championsDetails;
	}

	/**
	 * @return the display1
	 */
	public final JTextArea getDisplay1() {
		return display1;
	}

	/**
	 * @return the display2
	 */
	public final JTextArea getDisplay2() {
		return display2;
	}

	/**
	 * @return the champs1Buttons
	 */
	public final ArrayList<JButton> getChamps1Buttons() {
		return champs1Buttons;
	}

	/**
	 * @return the champs2Buttons
	 */
	public final ArrayList<JButton> getChamps2Buttons() {
		return champs2Buttons;
	}

	/**
	 * @return the display3
	 */
	public final JTextArea getDisplay3() {
		return display3;
	}

	/**
	 * @return the display4
	 */
	public final JTextArea getDisplay4() {
		return display4;
	}

	/**
	 * @return the startGame
	 */
	public final JButton getStartGame() {
		return startGame;
	}

	/**
	 * @return the champsSelected
	 */
	public final JButton getChampsSelected() {
		return ChampsSelected;
	}

	/**
	 * @return the playersNamesButton
	 */
	public final JButton getPlayersNamesButton() {
		return playersNamesButton;
	}

	/**
	 * @return the field1
	 */
	public final JTextField getField1() {
		return field1;
	}

	/**
	 * @return the field2
	 */
	public final JTextField getField2() {
		return field2;
	}


	




	

}
