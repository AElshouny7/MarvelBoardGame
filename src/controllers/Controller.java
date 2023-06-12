package controllers;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.*;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import engine.Game;
import engine.Player;
import exceptions.AbilityUseException;
import exceptions.ChampionDisarmedException;
import exceptions.GameActionException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.CrowdControlAbility;
import model.abilities.HealingAbility;
import model.effects.Effect;
import model.effects.EffectType;
import model.world.Champion;
import model.world.Condition;
import model.world.Cover;
import model.world.Damageable;
import model.world.Direction;
import views.AlLo3baa;
import views.BigView;

public class Controller implements KeyListener, MouseListener, ActionListener {
	private Game model;
	private BigView selectView;
	private Player player1;
	private Player player2;
	private AlLo3baa lo3ba;
	private Ability abilityPressed;
	@SuppressWarnings("unused")
	private boolean stillSameRound;

	public Controller() throws IOException {

		selectView = new BigView();
		selectView.getStartGame().addActionListener(this);
		selectView.getChampsSelected().addActionListener(this);
		selectView.getPlayersNamesButton().addActionListener(this);

	}

	private void startGame() {

		lo3ba = new AlLo3baa();

		lo3ba.addKeyListener(this);
		lo3ba.getEndTurnButton().addActionListener(this);
		lo3ba.getLeader1Ability().addActionListener(this);
		lo3ba.getLeader2Ability().addActionListener(this);

		for (JButton button : lo3ba.getCells()) {

			button.addMouseListener(this);
			button.addActionListener(this);
		}

		for (JRadioButton radio : lo3ba.getSingleTargetList())
			radio.addActionListener(this);

		for (JRadioButton radio : lo3ba.getDirectionalList())
			radio.addActionListener(this);

		lo3ba.getMoveUP().addActionListener(this);
		lo3ba.getMoveDOWN().addActionListener(this);
		lo3ba.getMoveLEFT().addActionListener(this);
		lo3ba.getMoveRIGHT().addActionListener(this);

		lo3ba.getAbility1().addMouseListener(this);
		lo3ba.getAbility2().addMouseListener(this);
		lo3ba.getAbility3().addMouseListener(this);
		lo3ba.getDisarmAbility().addMouseListener(this);

		model.placeChampions();
		model.prepareChampionTurns();
		stillSameRound = false;
		updateStats();
		updateTurns();

	}

	private void updateStats() {
		// Board Stats
		Object[][] board = model.getBoard();
		for (int x = 0; x < board.length; x++)
			// Loop through the rows in the 2D array.
			for (int y = 0; y < board[x].length; y++) { // Loop through the columns in the 2D array.

				if (board[x][y] instanceof Cover) {

					Cover cover = (Cover) board[x][y];
					String location = x + "," + y;

					for (JButton button : lo3ba.getCells()) {
						String buttonText = button.getToolTipText();
						if (location.equals(buttonText)) {
							String info = cover.toString();
							button.setText(info);
						}

					}

				}

				else if (board[x][y] instanceof Champion) {
					Champion champion = (Champion) board[x][y];
					String location = x + "," + y;

					for (JButton button : lo3ba.getCells()) {
						String buttonText = button.getToolTipText();
						if (location.equals(buttonText)) {

							String info = champion.nameHPMana();
							button.setText(info);
							if (player1.getTeam().contains(champion))
								button.setForeground(Color.BLUE);
							else
								button.setForeground(Color.RED);
						}
					}
				} else
					for (JButton button : lo3ba.getCells()) {
						String buttonText = button.getToolTipText();
						String location = x + "," + y;
						if (location.equals(buttonText)) {
							button.setText("");
						}

					}

			}

		// Remove Dead Targets
		for (Cover cover : model.getCovers())
			if (cover.getCurrentHP() <= 0) {
				int x = (int) cover.getLocation().getX();
				int y = (int) cover.getLocation().getY();
				String location = x + "," + y;
				for (JButton cell : lo3ba.getCells()) {
					String cellLocation = cell.getToolTipText();
					if (location.equals(cellLocation)) {
						cell.setText("");
					}
				}
				model.getCovers().remove(cover);
				break;
			}

		for (Champion dead : model.getDeadChampion()) {
			int x = (int) dead.getLocation().getX();
			int y = (int) dead.getLocation().getY();
			String location = x + "," + y;
			for (JButton cell : lo3ba.getCells()) {
				String cellLocation = cell.getToolTipText();
				if (location.equals(cellLocation)) {
					cell.setText("");
				}
			}

			model.getDeadChampion().remove(dead);
			break;
		}

		// Player 1 Stats

		
		lo3ba.getPlayer1Name().setText(player1.getName());

		String player1Info = "";
		for (Champion champion : player1.getTeam())
			if (champion == player1.getLeader())
				player1Info +=  "LEADER" + '\n' + champion.toString();
			else if (champion.getCondition() == Condition.INACTIVE)
			{
				System.out.println(champion);
			}
			else
				player1Info += champion.toString();
		
		lo3ba.getChampion1Things().setText(player1Info);
		
		if (model.isFirstLeaderAbilityUsed())
			lo3ba.getLeader1Used().setText("Ability Used");
		else
			lo3ba.getLeader1Used().setText("Ability not Used");

		// Player 2 Stats

		lo3ba.getPlayer2Name().setText(player2.getName());
		String player2Info = "";
		for (Champion champion : player2.getTeam())
			if (champion == player2.getLeader())
				player2Info +=  "LEADER" + '\n' + champion.toString();
			else if (champion.getCondition() == Condition.INACTIVE)
			{
				System.out.println(champion);
			}
			else
				player2Info += champion.toString();
		
		lo3ba.getChampion2Things().setText(player2Info);
		
		if (model.isSecondLeaderAbilityUsed())
			lo3ba.getLeader2Used().setText("Ability Used");
		else
			lo3ba.getLeader2Used().setText("Ability not Used");

//		updateTurns();

		// Abilities
		lo3ba.getAbility1().setText(model.getCurrentChampion().getAbilities().get(0).getName());
		lo3ba.getAbility1().setToolTipText(model.getCurrentChampion().getAbilities().get(0).toStringHTML());

		lo3ba.getAbility2().setText(model.getCurrentChampion().getAbilities().get(1).getName());
		lo3ba.getAbility2().setToolTipText(model.getCurrentChampion().getAbilities().get(1).toStringHTML());

		lo3ba.getAbility3().setText(model.getCurrentChampion().getAbilities().get(2).getName());
		lo3ba.getAbility3().setToolTipText(model.getCurrentChampion().getAbilities().get(2).toStringHTML());

		if (model.getCurrentChampion().getAbilities().size() == 4) {
			lo3ba.getDisarmAbility().setText("Punch");
			lo3ba.getDisarmAbility().setToolTipText(model.getCurrentChampion().getAbilities().get(3).toStringHTML());
			lo3ba.getDisarmAbility().setVisible(true);
		} else
			lo3ba.getDisarmAbility().setVisible(false);

	}

	private void updateTurns() {
		// Order

		String turnText;
		if (model.nextTurn() == null ) {
			turnText = model.getCurrentChampion().getName() + "'s Turn" + '\n' + "Next: " + "Round Ended";
			lo3ba.setRounds(lo3ba.getRounds() + 1);
			lo3ba.getRoundCounter().setText("Round " + lo3ba.getRounds());
			stillSameRound = true;
		}
		else
		{
			turnText = model.getCurrentChampion().getName() + "'s Turn" + '\n' + "Next: " + model.nextTurn().getName();
			
		}
		lo3ba.getTurns().setText(turnText);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// select view buttons
		if (e.getSource() == selectView.getPlayersNamesButton()) {

			player1 = new Player(selectView.getField1().getText());
			player2 = new Player(selectView.getField2().getText());
			model = new Game(player1, player2);

			try {
				Game.loadAbilities("Abilities.csv");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				Game.loadChampions("Champions.csv");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			int i = 0;
			for (JButton button : selectView.getChampionsButtons()) {
				String info = Game.getAvailableChampions().get(i).getName();
				button.setText(info);
				info = Game.getAvailableChampions().get(i).toStringHTML();
				button.setToolTipText(info);
				button.setFont(new Font("Fira Code", Font.BOLD, 12));
				button.addActionListener(this);
//				button.addMouseListener(this);
				i++;
				// ADD Mouse listener for the info better
			}
		}

		// CHAMPION SELECTION BUTTONS
		else if (selectView.getChampionsButtons().contains(e.getSource())) {
			JButton b = (JButton) e.getSource();
			for (Champion champion : Game.getAvailableChampions()) {
				if (champion.getName().equals(b.getText())) {
					b.setEnabled(false);
					if (player1.getTeam().size() != 3) {
						player1.getTeam().add(champion);
						String newText = selectView.getDisplay1().getText() + champion.getName() + '\n';
						selectView.getDisplay1().setText(newText);

					} else if (player2.getTeam().size() != 3) {
						player2.getTeam().add(champion);
						String newText = selectView.getDisplay2().getText() + champion.getName() + '\n';
						selectView.getDisplay2().setText(newText);
					}

					if (player1.getTeam().size() == 3 && player2.getTeam().size() == 3)
						for (JButton button : selectView.getChampionsButtons())
							button.setEnabled(false);

					Game.getAvailableChampions().remove(champion);
					break;
				}

			}

		} else if (e.getSource() == selectView.getChampsSelected()) {
			int i = 0;
			if (player1.getTeam().size() == 3) {
				for (JButton button : selectView.getChamps1Buttons()) {
					String info = player1.getTeam().get(i).getName();
					String tooltipInfo = player1.getTeam().get(i).myLeaderAbilityInfo();
					button.setText(info);
					button.setToolTipText(tooltipInfo);
					button.setFont(new Font("Fira Code", Font.BOLD, 12));
					button.addActionListener(this);
					i++;
				}
			}
			i = 0;
			if (player2.getTeam().size() == 3) {
				for (JButton button : selectView.getChamps2Buttons()) {
					String info = player2.getTeam().get(i).getName();
					String tooltipInfo = player2.getTeam().get(i).myLeaderAbilityInfo();
					button.setText(info);
					button.setToolTipText(tooltipInfo);
					button.setFont(new Font("Fira Code", Font.BOLD, 12));
					button.addActionListener(this);
					i++;
				}
			}
		}

		else if (selectView.getChamps1Buttons().contains(e.getSource())) {
			JButton b = (JButton) e.getSource();
			for (JButton button : selectView.getChamps1Buttons())
				button.setEnabled(false);
			for (Champion champion : model.getFirstPlayer().getTeam()) {
				if (champion.getName().equals(b.getText())) {
					player1.setLeader(champion);
					String newText = selectView.getDisplay3().getText() + champion.getName();
					selectView.getDisplay3().setText(newText);
					break;
				}
			}
		}

		else if (selectView.getChamps2Buttons().contains(e.getSource())) {
			JButton b = (JButton) e.getSource();
			for (JButton button : selectView.getChamps2Buttons())
				button.setEnabled(false);
			for (Champion champion : model.getSecondPlayer().getTeam()) {

				if (champion.getName().equals(b.getText())) {
					player2.setLeader(champion);
					String newText = selectView.getDisplay4().getText() + champion.getName();
					selectView.getDisplay4().setText(newText);
					break;
				}
			}
		}

		else if (e.getSource() == selectView.getStartGame()) {
			selectView.dispose();
			startGame();
		}

		// BOARD BUTTONS

		// Move Buttons
		else if (e.getSource() == lo3ba.getMoveUP()) {

			try {
				model.move(Direction.UP);
				updateStats();

			} catch (UnallowedMovementException | NotEnoughResourcesException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);

			}
		} else if (e.getSource() == lo3ba.getMoveDOWN()) {

			try {
				model.move(Direction.DOWN);
				updateStats();

			} catch (UnallowedMovementException | NotEnoughResourcesException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getSource() == lo3ba.getMoveLEFT()) {
			try {

				model.move(Direction.LEFT);
				updateStats();

			} catch (UnallowedMovementException | NotEnoughResourcesException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getSource() == lo3ba.getMoveRIGHT()) {
			try {
				model.move(Direction.RIGHT);
				updateStats();

			} catch (UnallowedMovementException | NotEnoughResourcesException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
		// SIngle Target and Directional
		else if (lo3ba.getSingleTargetList().contains(e.getSource())) {

			String name = lo3ba.getSingleTargetList().get(lo3ba.getSingleTargetList().indexOf(e.getSource())).getText();

			Object[][] board = model.getBoard();
			for (int x = 0; x < board.length; x++)
				// Loop through the rows in the 2D array.
				for (int y = 0; y < board[x].length; y++) {
					if (board[x][y] instanceof Champion && name.equals(((Champion) board[x][y]).getName())) {
						try {
							model.castAbility(abilityPressed, x, y);
							updateStats();
							abilityPressed = null;
							for (JRadioButton button : lo3ba.getSingleTargetList())
								button.setVisible(false);
						} catch (GameActionException | CloneNotSupportedException e1) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
							abilityPressed = null;
							for (JRadioButton button : lo3ba.getSingleTargetList())
								button.setVisible(false);
						}

					} else if (board[x][y] instanceof Cover) {

						int hp = ((Cover) board[x][y]).getCurrentHP();
						String hpString = Integer.toString(hp);

						if (name.contains(hpString)) {
							try {
								model.castAbility(abilityPressed, x, y);
								updateStats();
								abilityPressed = null;
								for (JRadioButton button : lo3ba.getSingleTargetList())
									button.setVisible(false);
							} catch (GameActionException | CloneNotSupportedException e1) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR",
										JOptionPane.ERROR_MESSAGE);
								abilityPressed = null;
								for (JRadioButton button : lo3ba.getSingleTargetList())
									button.setVisible(false);
							}
						}

					}

				}
		}

		else if (lo3ba.getDirectionalList().contains(e.getSource())) {
			String direction = lo3ba.getDirectionalList().get(lo3ba.getDirectionalList().indexOf(e.getSource()))
					.getText();
			if (direction.equals("UP")) {
				try {
					model.castAbility(abilityPressed, Direction.UP);
					updateStats();
					abilityPressed = null;
					for (JRadioButton button : lo3ba.getDirectionalList())
						button.setVisible(false);
				} catch (NotEnoughResourcesException | AbilityUseException | CloneNotSupportedException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
					abilityPressed = null;
					for (JRadioButton button : lo3ba.getDirectionalList())
						button.setVisible(false);
				}
			} else if (direction.equals("DOWN")) {

				try {
					model.castAbility(abilityPressed, Direction.DOWN);
					updateStats();
					abilityPressed = null;
					for (JRadioButton button : lo3ba.getDirectionalList())
						button.setVisible(false);
				} catch (NotEnoughResourcesException | AbilityUseException | CloneNotSupportedException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
					abilityPressed = null;
					for (JRadioButton button : lo3ba.getDirectionalList())
						button.setVisible(false);
				}
			}

			else if (direction.equals("LEFT")) {

				try {
					model.castAbility(abilityPressed, Direction.LEFT);
					updateStats();
					abilityPressed = null;
					for (JRadioButton button : lo3ba.getDirectionalList())
						button.setVisible(false);
				} catch (NotEnoughResourcesException | AbilityUseException | CloneNotSupportedException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
					abilityPressed = null;
					for (JRadioButton button : lo3ba.getDirectionalList())
						button.setVisible(false);
				}
			}

			else if (direction.equals("RIGHT")) {

				try {
					model.castAbility(abilityPressed, Direction.RIGHT);
					updateStats();
					abilityPressed = null;
					for (JRadioButton button : lo3ba.getDirectionalList())
						button.setVisible(false);
				} catch (NotEnoughResourcesException | AbilityUseException | CloneNotSupportedException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
					abilityPressed = null;
					for (JRadioButton button : lo3ba.getDirectionalList())
						button.setVisible(false);
				}
			}

		}

		// Leaders Abilites

		else if (e.getSource() == lo3ba.getLeader1Ability()) {
			try {
				model.useLeaderAbility();
				lo3ba.getLeader1Ability().setEnabled(false);
				updateStats();
			} catch (GameActionException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getSource() == lo3ba.getLeader2Ability()) {
			try {
				model.useLeaderAbility();
				lo3ba.getLeader2Ability().setEnabled(false);
				updateStats();
			} catch (GameActionException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}

		// Attack Buttons
		else if (lo3ba.getCells().contains(e.getSource())) {
			String location = ((JButton) e.getSource()).getToolTipText();
			int x = Character.getNumericValue(location.charAt(0));
			int y = Character.getNumericValue(location.charAt(2));

			int championX = (int) model.getCurrentChampion().getLocation().getX();
			int championY = (int) model.getCurrentChampion().getLocation().getY();

			if (x > championX && y == championY) // UP
			{
				try {
					model.attack(Direction.UP);
					updateStats();
				} catch (ChampionDisarmedException | NotEnoughResourcesException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				}

			}

			else if (x < championX && y == championY) // DOWN
			{

				try {
					model.attack(Direction.DOWN);
					updateStats();
				} catch (ChampionDisarmedException | NotEnoughResourcesException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}

			else if (y < championY && x == championX) // LEFT
			{
				try {
					model.attack(Direction.LEFT);
					updateStats();
				} catch (ChampionDisarmedException | NotEnoughResourcesException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}

			else if (y > championY && x == championX) // RIGHT
			{
				try {
					model.attack(Direction.RIGHT);
					updateStats();
				} catch (ChampionDisarmedException | NotEnoughResourcesException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		// End Turn
		else if (e.getSource() == lo3ba.getEndTurnButton()) {
			model.endTurn();
			Player winner = model.checkGameOver();
			if (winner != null) {

				JOptionPane.showMessageDialog(null, winner.getName() + " 7at 3aleeh", "YOU WON!",
						JOptionPane.INFORMATION_MESSAGE);
				lo3ba.dispose();
			}
			updateStats();
			updateTurns();
		}

	}

	public JButton championButton(int x, int y) {

		String location = x + "," + y;

		for (JButton cell : lo3ba.getCells())
			if (cell.getToolTipText().equals(location))
				return cell;
		return null;

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		

		if (e.getKeyCode() == 38) {
			
			try {
				model.move(Direction.UP);

			} catch (UnallowedMovementException | NotEnoughResourcesException e1) {

				JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}

		else if (e.getKeyCode() == 40) {
			try {
				model.move(Direction.DOWN);
			} catch (UnallowedMovementException | NotEnoughResourcesException e1) {

				JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}

		else if (e.getKeyCode() == 37) {
			try {
				model.move(Direction.LEFT);
			} catch (UnallowedMovementException | NotEnoughResourcesException e1) {

				JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}

		else if (e.getKeyCode() == 39) {
			try {
				model.move(Direction.RIGHT);

			} catch (UnallowedMovementException | NotEnoughResourcesException e1) {

				JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == lo3ba.getAbility1()) {
			Ability ability = model.getCurrentChampion().getAbilities().get(0);
			abilityPressed = ability;
			if (ability.getCastArea() == AreaOfEffect.SINGLETARGET) {
				if (ability instanceof HealingAbility || (ability instanceof CrowdControlAbility
						&& ((CrowdControlAbility) ability).getEffect().getType() == EffectType.BUFF)) {

					for (int i = 0; i < 3; i++) {
						JRadioButton radio = lo3ba.getSingleTargetList().get(i);
						if (player1.getTeam().contains(model.getCurrentChampion()))
							radio.setText(player1.getTeam().get(i).getName());

						radio.setVisible(true);
					}

				}

				else {
					for (int i = 0; i < 3; i++) {
						JRadioButton radio = lo3ba.getSingleTargetList().get(i);

						if (player1.getTeam().contains(model.getCurrentChampion())) {
							radio.setText(player2.getTeam().get(i).getName());

						}

						if (player2.getTeam().contains(model.getCurrentChampion())) {
							radio.setText(player1.getTeam().get(i).getName());

						}

						radio.setVisible(true);

					}
					int j = 3;
					for (Cover cover : model.getCovers()) {
						JRadioButton radio = lo3ba.getSingleTargetList().get(j);
						radio.setText("Cover HP: " + cover.getCurrentHP());
						radio.setVisible(true);
						j++;
					}
				}
			} else if (ability.getCastArea() == AreaOfEffect.DIRECTIONAL) {

				for (JRadioButton radio : lo3ba.getDirectionalList())

					radio.setVisible(true);

			}

			else if (ability.getCastArea() == AreaOfEffect.TEAMTARGET
					|| ability.getCastArea() == AreaOfEffect.SELFTARGET
					|| ability.getCastArea() == AreaOfEffect.SURROUND) {
				try {
					model.castAbility(abilityPressed);
					updateStats();
				} catch (NotEnoughResourcesException | AbilityUseException | CloneNotSupportedException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}

		} else if (e.getSource() == lo3ba.getAbility2()) {
			Ability ability = model.getCurrentChampion().getAbilities().get(1);
			abilityPressed = ability;
			if (ability.getCastArea() == AreaOfEffect.SINGLETARGET) {
				if (ability instanceof HealingAbility || (ability instanceof CrowdControlAbility
						&& ((CrowdControlAbility) ability).getEffect().getType() == EffectType.BUFF)) {

					for (int i = 0; i < 3; i++) {
						JRadioButton radio = lo3ba.getSingleTargetList().get(i);

						if (player1.getTeam().contains(model.getCurrentChampion()))
							radio.setText(player1.getTeam().get(i).getName());

						radio.setVisible(true);
					}

				}

				else {

					for (int i = 0; i < 3; i++) {
						JRadioButton radio = lo3ba.getSingleTargetList().get(i);

						if (player1.getTeam().contains(model.getCurrentChampion())) {
							radio.setText(player2.getTeam().get(i).getName());

						}

						if (player2.getTeam().contains(model.getCurrentChampion())) {
							radio.setText(player1.getTeam().get(i).getName());

						}

						radio.setVisible(true);

					}
					int j = 3;
					for (Cover cover : model.getCovers()) {
						JRadioButton radio = lo3ba.getSingleTargetList().get(j);
						radio.setText("Cover HP: " + cover.getCurrentHP());
						radio.setVisible(true);
						j++;
					}
				}
			} else if (ability.getCastArea() == AreaOfEffect.DIRECTIONAL) {

				for (JRadioButton radio : lo3ba.getDirectionalList())

					radio.setVisible(true);

			}

			else if (ability.getCastArea() == AreaOfEffect.TEAMTARGET
					|| ability.getCastArea() == AreaOfEffect.SELFTARGET
					|| ability.getCastArea() == AreaOfEffect.SURROUND) {
				try {
					model.castAbility(abilityPressed);
					updateStats();
				} catch (NotEnoughResourcesException | AbilityUseException | CloneNotSupportedException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}

		} else if (e.getSource() == lo3ba.getAbility3()) {
			Ability ability = model.getCurrentChampion().getAbilities().get(2);
			abilityPressed = ability;
			if (ability.getCastArea() == AreaOfEffect.SINGLETARGET) {
				if (ability instanceof HealingAbility || (ability instanceof CrowdControlAbility
						&& ((CrowdControlAbility) ability).getEffect().getType() == EffectType.BUFF)) {

					for (int i = 0; i < 3; i++) {
						JRadioButton radio = lo3ba.getSingleTargetList().get(i);

						if (player1.getTeam().contains(model.getCurrentChampion()))
							radio.setText(player1.getTeam().get(i).getName());

						radio.setVisible(true);
					}

				}

				else {
					for (int i = 0; i < 3; i++) {
						JRadioButton radio = lo3ba.getSingleTargetList().get(i);

						if (player1.getTeam().contains(model.getCurrentChampion())) {
							radio.setText(player2.getTeam().get(i).getName());

						}

						if (player2.getTeam().contains(model.getCurrentChampion())) {
							radio.setText(player1.getTeam().get(i).getName());

						}

						radio.setVisible(true);

					}

					int j = 3;
					for (Cover cover : model.getCovers()) {

						JRadioButton radio = lo3ba.getSingleTargetList().get(j);
						radio.setText("Cover HP: " + cover.getCurrentHP());
						radio.setVisible(true);
						j++;
					}
				}
			} else if (ability.getCastArea() == AreaOfEffect.DIRECTIONAL) {

				for (JRadioButton radio : lo3ba.getDirectionalList())

					radio.setVisible(true);

			}

			else if (ability.getCastArea() == AreaOfEffect.TEAMTARGET
					|| ability.getCastArea() == AreaOfEffect.SELFTARGET
					|| ability.getCastArea() == AreaOfEffect.SURROUND) {
				try {
					model.castAbility(abilityPressed);
					updateStats();
				} catch (NotEnoughResourcesException | AbilityUseException | CloneNotSupportedException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}

		} else if (e.getSource() == lo3ba.getDisarmAbility()) {
			Ability ability = model.getCurrentChampion().getAbilities().get(3);
			abilityPressed = ability;

			for (int i = 0; i < 3; i++) {
				JRadioButton radio = lo3ba.getSingleTargetList().get(i);

				if (player1.getTeam().contains(model.getCurrentChampion())) {
					radio.setText(player2.getTeam().get(i).getName());

				}

				if (player2.getTeam().contains(model.getCurrentChampion())) {
					radio.setText(player1.getTeam().get(i).getName());

				}

				radio.setVisible(true);

			}

			int j = 3;
			for (Cover cover : model.getCovers()) {

				JRadioButton radio = lo3ba.getSingleTargetList().get(j);
				radio.setText("Cover HP: " + cover.getCurrentHP());
				radio.setVisible(true);
				j++;
			}
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (selectView.getChampionsButtons().contains(e.getSource())) {
			JButton cell = (JButton) e.getSource();
			String name = cell.getText();
			for (Champion champion : Game.getAvailableChampions()) {
				if (champion.getName().equals(name)) {
					cell.setText(champion.toStringHTML());
					cell.setFont(new Font("Fira Code", Font.BOLD, 9));
				}
			}
		}

		else if (lo3ba != null) {
			if (lo3ba.getCells().contains(e.getSource())) {

				JButton cell = (JButton) e.getSource();
				String location = cell.getToolTipText();
				int x = Character.getNumericValue(location.charAt(0));
				int y = Character.getNumericValue(location.charAt(2));

				Champion champion = null;
				for (Champion c : player1.getTeam()) {
					int championX = (int) c.getLocation().getX();
					int championY = (int) c.getLocation().getY();

					if (x == championX && y == championY) {
						champion = c;
						break;
					}
				}
				for (Champion c : player2.getTeam()) {
					int championX = (int) c.getLocation().getX();
					int championY = (int) c.getLocation().getY();

					if (x == championX && y == championY) {
						champion = c;
						break;
					}
				}

				if (champion == null)
					return;

				String infoButton = "Current Action Points: " + champion.getCurrentActionPoints() + "/"
						+ champion.getMaxActionPointsPerTurn();
				cell.setText(infoButton);
				cell.setFont(new Font("Fira Code", Font.BOLD, 9));
				cell.setHorizontalTextPosition(JButton.LEFT);

				String infoPane = "Applied Effect" + '\n' + champion.getAppliedEffects().toString();
				lo3ba.getAppliedEffectsPane().setText(infoPane);
				lo3ba.getAppliedEffectsPane().setVisible(true);

			} else if (e.getSource() == lo3ba.getAbility1()) {
				Ability ability = model.getCurrentChampion().getAbilities().get(0);
				lo3ba.getAbility1()
						.setText("Cool Down: " + ability.getCurrentCooldown() + "/" + ability.getBaseCooldown());

			} else if (e.getSource() == lo3ba.getAbility2()) {
				Ability ability = model.getCurrentChampion().getAbilities().get(1);
				lo3ba.getAbility2()
						.setText("Cool Down: " + ability.getCurrentCooldown() + "/" + ability.getBaseCooldown());

			} else if (e.getSource() == lo3ba.getAbility3()) {

				Ability ability = model.getCurrentChampion().getAbilities().get(2);
				lo3ba.getAbility3()
						.setText("Cool Down: " + ability.getCurrentCooldown() + "/" + ability.getBaseCooldown());
			} else if (e.getSource() == lo3ba.getDisarmAbility()) {
				Ability ability = model.getCurrentChampion().getAbilities().get(3);
				lo3ba.getDisarmAbility()
						.setText("Cool Down: " + ability.getCurrentCooldown() + "/" + ability.getBaseCooldown());
			}
		}

	}

	@Override
	public void mouseExited(MouseEvent e) {

		if (selectView.getChampionsButtons().contains(e.getSource())) {
			JButton cell = (JButton) e.getSource();
			String name = cell.getText();

			for (Champion champion : Game.getAvailableChampions()) {

				if (name.contains(champion.getName())) {
					cell.setText(champion.getName());
					cell.setFont(new Font("Fira Code", Font.BOLD, 12));
				}
			}
		}

		if (lo3ba != null) {
			if (lo3ba.getCells().contains(e.getSource())) {
				JButton cell = (JButton) e.getSource();
				String location = cell.getToolTipText();
				int x = Character.getNumericValue(location.charAt(0));
				int y = Character.getNumericValue(location.charAt(2));

				Champion champion = null;
				for (Champion c : player1.getTeam()) {
					int championX = (int) c.getLocation().getX();
					int championY = (int) c.getLocation().getY();

					if (x == championX && y == championY) {
						champion = c;
						break;
					}
				}
				for (Champion c : player2.getTeam()) {
					int championX = (int) c.getLocation().getX();
					int championY = (int) c.getLocation().getY();

					if (x == championX && y == championY) {
						champion = c;
						break;
					}
				}

				if (champion == null)
					return;

				cell.setText(champion.nameHPMana());
				cell.setFont(new Font("Fira Code", Font.BOLD, 12));
				cell.setHorizontalTextPosition(JButton.CENTER);

				lo3ba.getAppliedEffectsPane().setText("Applied Effect" + '\n');
				lo3ba.getAppliedEffectsPane().setVisible(false);

			} else if (e.getSource() == lo3ba.getAbility1()) {
				Ability ability = model.getCurrentChampion().getAbilities().get(0);
				lo3ba.getAbility1().setText(ability.getName());

			} else if (e.getSource() == lo3ba.getAbility2()) {
				Ability ability = model.getCurrentChampion().getAbilities().get(1);
				lo3ba.getAbility2().setText(ability.getName());

			} else if (e.getSource() == lo3ba.getAbility3()) {

				Ability ability = model.getCurrentChampion().getAbilities().get(2);
				lo3ba.getAbility3().setText(ability.getName());
			} else if (e.getSource() == lo3ba.getDisarmAbility()) {
				Ability ability = model.getCurrentChampion().getAbilities().get(3);
				lo3ba.getDisarmAbility().setText(ability.getName());
			}
		}

	}

	/**
	 * @return the model
	 */
	public final Game getModel() {
		return model;
	}

	/**
	 * @return the view
	 */
	public final BigView getView() {
		return selectView;
	}

	/**
	 * @return the player1
	 */
	public final Player getPlayer1() {
		return player1;
	}

	/**
	 * @return the player2
	 */
	public final Player getPlayer2() {
		return player2;
	}

//	public static void main(String[] args) throws IOException {
//		new Controller();
//
//	}

	/**
	 * @return the lo3ba
	 */
	public final AlLo3baa getLo3ba() {
		return lo3ba;
	}

}
