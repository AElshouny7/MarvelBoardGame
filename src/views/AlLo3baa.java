package views;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.AttributeSet.ColorAttribute;

public class AlLo3baa extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel lo3ba;
	private ArrayList<JButton> cells;
	private JLabel roundCounter;
	private int rounds;
	private JTextPane turns;
	private JButton endTurnButton;

	private JButton ability1;
	private JButton ability2;
	private JButton ability3;
	private JButton disarmAbility;

	private JButton moveUP;
	private JButton moveDOWN;
	private JButton moveLEFT;
	private JButton moveRIGHT;

	private JPanel abilitiesPanel;
	private ArrayList<JRadioButton> singleTargetList;
	private ArrayList<JRadioButton> directionalList;

	private JPanel stats1;
	private JLabel player1Name;
	private JTextPane champion1Things;
	private JLabel leader1Used;
	private JButton leader1Ability;

	private JPanel stats2;
	private JLabel player2Name;
	private JTextPane champion2Things;
	private JLabel leader2Used;
	private JButton leader2Ability;

	private JTextPane appliedEffectsPane;
	
	private JPanel blackPanel;

	/**
	 * @return the cells
	 */
	public final ArrayList<JButton> getCells() {
		return cells;
	}

	public AlLo3baa() throws HeadlessException {

		// setting the frame to full screen
		setLayout(null);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBackground(Color.BLACK);
		setUndecorated(true);

		blackPanel = new JPanel(null);
		blackPanel.setBounds(260 , 0 , 1920 , 1080);
		blackPanel.setBackground(Color.BLACK);
		
		
		// setting the board
		lo3ba = new JPanel(new GridLayout(5, 5, 5, 5), true);
		lo3ba.setBounds(280, 120, 960, 540);
		lo3ba.setBackground(Color.BLACK);
		cells = new ArrayList<JButton>();
		int x = 4;
		int y = 0;
		for (int i = 0; i < 25; i++) {
			JButton cell = new JButton(" ");
			cell.setToolTipText(x + "," + y);
			y++;
			if (y == 5) {
				x--;
				y = 0;
			}
			cell.setFocusable(false);
			cell.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.WHITE));
			cell.setForeground(Color.WHITE);
			cell.setContentAreaFilled(false);
			cell.setVisible(true);
			cells.add(cell);
			lo3ba.add(cell);

		}

		// Round Counter
		rounds = 1;
		roundCounter = new JLabel("Round " + rounds);
		roundCounter.setForeground(Color.WHITE);
		roundCounter.setBackground(Color.BLACK);
		roundCounter.setBounds(280, 0, 960, 120);
		roundCounter.setHorizontalAlignment(JLabel.CENTER);
		roundCounter.setVerticalAlignment(JLabel.CENTER);
		roundCounter.setFont(new Font("Fira Code", Font.ITALIC, 100));

		// turns area
		turns = new JTextPane();
		turns.setBounds(610, 700, 300, 100);
		turns.setText("Example" + "'s Turn" + '\n' + "Next: " + "Example Array");
		turns.setForeground(Color.WHITE);
		turns.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		turns.setBackground(Color.BLACK);
		turns.setEditable(false);
		turns.setOpaque(false);

		StyledDocument docCenter = turns.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		docCenter.setParagraphAttributes(0, docCenter.getLength(), center, false);

		// MISC Buttons

		endTurnButton = new JButton("End Turn");
		endTurnButton.setBounds(660, 780, 200, 50);
		endTurnButton.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.WHITE));
		endTurnButton.setForeground(Color.WHITE);
		endTurnButton.setContentAreaFilled(false);
		endTurnButton.setFocusable(false);
		endTurnButton.setVisible(true);

		ability1 = new JButton("ability 1");
		ability1.setBounds(280, 680, 200, 40);
		ability1.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.WHITE));
		ability1.setForeground(Color.WHITE);
		ability1.setContentAreaFilled(false);
		ability1.setFocusable(false);
		ability1.setVisible(true);

		ability2 = new JButton("ability 2");
		ability2.setBounds(280, 730, 200, 40);
		ability2.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.WHITE));
		ability2.setForeground(Color.WHITE);
		ability2.setContentAreaFilled(false);
		ability2.setFocusable(false);
		ability2.setVisible(true);

		ability3 = new JButton("ability 3");
		ability3.setBounds(280, 780, 200, 40);
		ability3.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.WHITE));
		ability3.setForeground(Color.WHITE);
		ability3.setContentAreaFilled(false);
		ability3.setFocusable(false);
		ability3.setVisible(true);
		
		disarmAbility = new JButton("Punch");
		disarmAbility.setBounds(280, 830, 200, 40);
		disarmAbility.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.WHITE));
		disarmAbility.setForeground(Color.WHITE);
		disarmAbility.setContentAreaFilled(false);
		disarmAbility.setFocusable(false);
		disarmAbility.setVisible(false);

		// Abilities
		abilitiesPanel = new JPanel();
//		abilitiesPanel.setBackground(Color.GREEN);
		abilitiesPanel.setBounds(490, 660, 150, 200);
		abilitiesPanel.setBackground(Color.BLACK);
		

		singleTargetList = new ArrayList<JRadioButton>();
		ButtonGroup singleTargetGroup = new ButtonGroup();

		for (int i = 0; i < 8; i++)
			if (i < 3) {
				JRadioButton button = new JRadioButton("Champion " + i);
				button.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.WHITE));
				button.setForeground(Color.WHITE);
				button.setContentAreaFilled(false);
				button.setVisible(false);
				singleTargetList.add(button);
				singleTargetGroup.add(button);

			} else {
				JRadioButton button = new JRadioButton("Cover " + i);
				button.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.WHITE));
				button.setForeground(Color.WHITE);
				button.setContentAreaFilled(false);
				button.setVisible(false);
				singleTargetList.add(button);
				singleTargetGroup.add(button);

			}

		directionalList = new ArrayList<JRadioButton>();
		ButtonGroup directionalGroup = new ButtonGroup();

		JRadioButton button = new JRadioButton("UP");
		button.setVisible(false);
		button.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE));
		button.setForeground(Color.WHITE);
		directionalList.add(button);
		directionalGroup.add(button);

		button = new JRadioButton("DOWN");
		button.setVisible(false);
		button.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE));
		button.setForeground(Color.WHITE);
		directionalList.add(button);
		directionalGroup.add(button);

		button = new JRadioButton("LEFT");
		button.setVisible(false);
		button.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE));
		button.setForeground(Color.WHITE);
		directionalList.add(button);
		directionalGroup.add(button);

		button = new JRadioButton("RIGHT");
		button.setVisible(false);
		button.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE));
		button.setForeground(Color.WHITE);
		directionalList.add(button);
		directionalGroup.add(button);

		abilitiesPanel.add(singleTargetList.get(0));
		abilitiesPanel.add(singleTargetList.get(1));
		abilitiesPanel.add(singleTargetList.get(2));
		abilitiesPanel.add(singleTargetList.get(3));
		abilitiesPanel.add(singleTargetList.get(4));
		abilitiesPanel.add(singleTargetList.get(5));
		abilitiesPanel.add(singleTargetList.get(6));
		abilitiesPanel.add(singleTargetList.get(7));

		abilitiesPanel.add(directionalList.get(0));
		abilitiesPanel.add(directionalList.get(1));
		abilitiesPanel.add(directionalList.get(2));
		abilitiesPanel.add(directionalList.get(3));

		moveUP = new JButton("Up");
		moveUP.setFont(new Font("Fira Code", Font.BOLD, 8));
		moveUP.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.WHITE));
		moveUP.setForeground(Color.WHITE);
		moveUP.setContentAreaFilled(false);
		moveUP.setBounds(1110, 680, 60, 60);
		moveUP.setFocusable(false);
		moveUP.setVisible(true);

		moveDOWN = new JButton("Down");
		moveDOWN.setFont(new Font("Fira Code", Font.BOLD, 8));
		moveDOWN.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.WHITE));
		moveDOWN.setForeground(Color.WHITE);
		moveDOWN.setContentAreaFilled(false);
		moveDOWN.setBounds(1110, 800, 60, 60);
		moveDOWN.setFocusable(false);
		moveDOWN.setVisible(true);

		moveLEFT = new JButton("Left");
		moveLEFT.setFont(new Font("Fira Code", Font.BOLD, 8));
		moveLEFT.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.WHITE));
		moveLEFT.setForeground(Color.WHITE);
		moveLEFT.setContentAreaFilled(false);
		moveLEFT.setBounds(1040, 740, 60, 60);
		moveLEFT.setFocusable(false);
		moveLEFT.setVisible(true);

		moveRIGHT = new JButton("Right");
		moveRIGHT.setFont(new Font("Fira Code", Font.BOLD, 8));
		moveRIGHT.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.WHITE));
		moveRIGHT.setForeground(Color.WHITE);
		moveRIGHT.setContentAreaFilled(false);
		moveRIGHT.setBounds(1180, 740, 60, 60);
		moveRIGHT.setFocusable(false);
		moveRIGHT.setVisible(true);

		// Player 1 Statistics Panel
		stats1 = new JPanel(null, true);
		stats1.setBounds(0, 0, 270, 1080);
		stats1.setBackground(Color.BLUE);

		// Player 1 Name area
		player1Name = new JLabel("Player 1");
		player1Name.setBounds(60,40, 150, 100);
		player1Name.setOpaque(false);
//		player1Name.setBackground(Color.BLUE);
		player1Name.setHorizontalAlignment(JLabel.CENTER);
		player1Name.setVerticalAlignment(JLabel.CENTER);
		player1Name.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
		player1Name.setForeground(Color.WHITE);
		

		// Champions 1 Stats

		champion1Things = new JTextPane();
		champion1Things.setText("");
		champion1Things.setBounds(10, 150, 270, 540);
//		champion1Things.setBackground(Color.BLUE);
		champion1Things.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		champion1Things.setForeground(Color.WHITE);
		champion1Things.setEditable(false);
		champion1Things.setOpaque(false);

		JScrollPane champion1ThingsScroll = new JScrollPane(champion1Things);

		StyledDocument docLeft1 = champion1Things.getStyledDocument();
		SimpleAttributeSet left = new SimpleAttributeSet();
		StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
		docLeft1.setParagraphAttributes(0, docLeft1.getLength(), left, false);

		// Leader 1 Ability Usage

		leader1Used = new JLabel("Leader Ability Used");
		leader1Used.setBounds(10, 700, 270, 150);
		leader1Used.setFont(new Font("Fira Code", Font.PLAIN, 20));
		leader1Used.setForeground(Color.WHITE);
		leader1Used.setHorizontalTextPosition(JLabel.RIGHT);
//		leader1Used.setBackground(Color.GREEN);
		leader1Used.setOpaque(false);

		leader1Ability = new JButton("Leader Ability");
		leader1Ability.setBounds(10, 800, 200, 50);
		leader1Ability.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.WHITE));
		leader1Ability.setForeground(Color.WHITE);
		leader1Ability.setContentAreaFilled(false);
		leader1Ability.setFocusable(false);
		leader1Ability.setVisible(true);

		stats1.add(player1Name);
		stats1.add(champion1Things);
		stats1.add(champion1ThingsScroll);
		stats1.add(leader1Used);
		stats1.add(leader1Ability);

		// Player 2 Statistics Panel
		stats2 = new JPanel(null, true);
		stats2.setBounds(1260, 0, 270, 1080);
		stats2.setBackground(Color.RED);

		// Player 2 Name area
		player2Name = new JLabel("Player 2");
		player2Name.setBounds(60,40, 150, 100);
		player2Name.setOpaque(true);
		player2Name.setBackground(Color.RED);
		player2Name.setHorizontalAlignment(JLabel.CENTER);
		player2Name.setVerticalAlignment(JLabel.CENTER);
		player2Name.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
		player2Name.setForeground(Color.WHITE);

		// Champions 2 Stats

		champion2Things = new JTextPane();
		champion2Things.setText("");
		champion2Things.setBounds(10, 150, 270, 540);
//		champion2Things.setBackground(Color.BLUE);
		champion2Things.setFont(new Font("Fira Code", Font.PLAIN, 15));
		champion2Things.setForeground(Color.WHITE);
		champion2Things.setEditable(false);
		champion2Things.setOpaque(false);

		JScrollPane champion2ThingsScroll = new JScrollPane(champion2Things);

		StyledDocument docLeft2 = champion2Things.getStyledDocument();
		SimpleAttributeSet left2 = new SimpleAttributeSet();
		StyleConstants.setAlignment(left2, StyleConstants.ALIGN_LEFT);
		docLeft2.setParagraphAttributes(0, docLeft2.getLength(), left2, false);

		// Leader 1 Ability Usage

		leader2Used = new JLabel("Leader Ability Used");
		leader2Used.setBounds(10, 700, 270, 150);
		leader2Used.setFont(new Font("Fira Code", Font.PLAIN, 20));
		leader2Used.setHorizontalTextPosition(JLabel.RIGHT);
		leader2Used.setForeground(Color.WHITE);
//		leader2Used.setBackground(Color.GREEN);
		leader2Used.setOpaque(false);

		leader2Ability = new JButton("Leader Ability");
		leader2Ability.setBounds(10, 800, 200, 50);
		leader2Ability.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.WHITE));
		leader2Ability.setForeground(Color.WHITE);
		leader2Ability.setContentAreaFilled(false);
		leader2Ability.setFocusable(false);
		leader2Ability.setVisible(true);

		stats2.add(player2Name);
		stats2.add(champion2Things);
		stats2.add(champion2ThingsScroll);
		stats2.add(leader2Used);
		stats2.add(leader2Ability);

		appliedEffectsPane = new JTextPane();
//		appliedEffectsPane.setBackground(Color.RED);
		appliedEffectsPane.setOpaque(false);
		appliedEffectsPane.setVisible(true);
		appliedEffectsPane.setBounds(870, 670, 150, 200);
		appliedEffectsPane.setEditable(false);
		appliedEffectsPane.setFont(new Font("Fira Code", Font.BOLD, 10));
		appliedEffectsPane.setForeground(Color.WHITE);

		StyledDocument docCenter1 = appliedEffectsPane.getStyledDocument();
		SimpleAttributeSet center1 = new SimpleAttributeSet();
		StyleConstants.setAlignment(center1, StyleConstants.ALIGN_CENTER);
		docCenter1.setParagraphAttributes(0, docCenter1.getLength(), center1, false);

		
		add(lo3ba);
		add(roundCounter);
		add(turns);
		add(endTurnButton);
		add(stats1);
		add(stats2);
		add(ability1);
		add(ability2);
		add(ability3);
		add(disarmAbility);
		add(moveUP);
		add(moveDOWN);
		add(moveLEFT);
		add(moveRIGHT);
		add(abilitiesPanel);
		add(appliedEffectsPane);
		add(blackPanel);

//		revalidate();	
		setVisible(true);

	}

	/**
	 * @return the player1Name
	 */
	public final JLabel getPlayer1Name() {
		return player1Name;
	}

	/**
	 * @return the champion1Things
	 */
	public final JTextPane getChampion1Things() {
		return champion1Things;
	}

	/**
	 * @return the leader1Used
	 */
	public final JLabel getLeader1Used() {
		return leader1Used;
	}

	/**
	 * @return the player2Name
	 */
	public final JLabel getPlayer2Name() {
		return player2Name;
	}

	/**
	 * @return the champion2Things
	 */
	public final JTextPane getChampion2Things() {
		return champion2Things;
	}

	/**
	 * @return the leader2Used
	 */
	public final JLabel getLeader2Used() {
		return leader2Used;
	}

	/**
	 * @return the turns
	 */
	public final JTextPane getTurns() {
		return turns;
	}

	/**
	 * @param rounds the rounds to set
	 */
	public final void setRounds(int rounds) {
		this.rounds = rounds;
	}

	/**
	 * @return the lo3ba
	 */
	public final JPanel getLo3ba() {
		return lo3ba;
	}

	/**
	 * @return the moveUP
	 */
	public final JButton getMoveUP() {
		return moveUP;
	}

	/**
	 * @return the moveDOWN
	 */
	public final JButton getMoveDOWN() {
		return moveDOWN;
	}

	/**
	 * @return the moveLEFT
	 */
	public final JButton getMoveLEFT() {
		return moveLEFT;
	}

	/**
	 * @return the moveRIGHT
	 */
	public final JButton getMoveRIGHT() {
		return moveRIGHT;
	}

	/**
	 * @return the leader1Ability
	 */
	public final JButton getLeader1Ability() {
		return leader1Ability;
	}

	/**
	 * @return the leader2Ability
	 */
	public final JButton getLeader2Ability() {
		return leader2Ability;
	}

	/**
	 * @return the ability1
	 */
	public final JButton getAbility1() {
		return ability1;
	}

	/**
	 * @return the ability2
	 */
	public final JButton getAbility2() {
		return ability2;
	}

	/**
	 * @return the ability3
	 */
	public final JButton getAbility3() {
		return ability3;
	}

	/**
	 * @return the singleTargetList
	 */
	public final ArrayList<JRadioButton> getSingleTargetList() {
		return singleTargetList;
	}

	/**
	 * @return the directionalList
	 */
	public final ArrayList<JRadioButton> getDirectionalList() {
		return directionalList;
	}

	/**
	 * @return the endTurnButton
	 */
	public final JButton getEndTurnButton() {
		return endTurnButton;
	}

	/**
	 * @return the rounds
	 */
	public final int getRounds() {
		return rounds;
	}

	/**
	 * @return the roundCounter
	 */
	public final JLabel getRoundCounter() {
		return roundCounter;
	}

	/**
	 * @return the appliedEffectsPane
	 */
	public final JTextPane getAppliedEffectsPane() {
		return appliedEffectsPane;
	}

	/**
	 * @return the disarmAbility
	 */
	public final JButton getDisarmAbility() {
		return disarmAbility;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		new AlLo3baa();
	}

}
