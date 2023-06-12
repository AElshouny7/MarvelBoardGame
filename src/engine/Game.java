package engine;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import exceptions.AbilityUseException;
import exceptions.ChampionDisarmedException;
import exceptions.GameActionException;
import exceptions.InvalidTargetException;
import exceptions.LeaderAbilityAlreadyUsedException;
import exceptions.LeaderNotCurrentException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.effects.Disarm;
import model.effects.Dodge;
import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Embrace;
import model.effects.PowerUp;
import model.effects.Root;
import model.effects.Shield;
import model.effects.Shock;
import model.effects.Silence;
import model.effects.SpeedUp;
import model.effects.Stun;
import model.world.AntiHero;
import model.world.Champion;
import model.world.Condition;
import model.world.Cover;
import model.world.Damageable;
import model.world.Direction;
import model.world.Hero;
import model.world.Villain;

public class Game {
	private static ArrayList<Champion> availableChampions;
	private static ArrayList<Ability> availableAbilities;
	private Player firstPlayer;
	private Player secondPlayer;
	private Object[][] board;
	private PriorityQueue turnOrder;
	private boolean firstLeaderAbilityUsed;
	private boolean secondLeaderAbilityUsed;
	private final static int BOARDWIDTH = 5;
	private final static int BOARDHEIGHT = 5;
	private ArrayList<Cover> Covers;
	private ArrayList<Champion> deadChampion;

	public Game(Player first, Player second) {
		firstPlayer = first;

		secondPlayer = second;
		availableChampions = new ArrayList<Champion>();
		availableAbilities = new ArrayList<Ability>();
		board = new Object[BOARDWIDTH][BOARDHEIGHT];
		Covers = new ArrayList<Cover>();
		deadChampion = new ArrayList<Champion>();
		turnOrder = new PriorityQueue(6);
		placeChampions();
		placeCovers();
		prepareChampionTurns();
	}

	public static void loadAbilities(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			Ability a = null;
			AreaOfEffect ar = null;
			switch (content[5]) {
			case "SINGLETARGET":
				ar = AreaOfEffect.SINGLETARGET;
				break;
			case "TEAMTARGET":
				ar = AreaOfEffect.TEAMTARGET;
				break;
			case "SURROUND":
				ar = AreaOfEffect.SURROUND;
				break;
			case "DIRECTIONAL":
				ar = AreaOfEffect.DIRECTIONAL;
				break;
			case "SELFTARGET":
				ar = AreaOfEffect.SELFTARGET;
				break;

			}
			Effect e = null;
			if (content[0].equals("CC")) {
				switch (content[7]) {
				case "Disarm":
					e = new Disarm(Integer.parseInt(content[8]));
					break;
				case "Dodge":
					e = new Dodge(Integer.parseInt(content[8]));
					break;
				case "Embrace":
					e = new Embrace(Integer.parseInt(content[8]));
					break;
				case "PowerUp":
					e = new PowerUp(Integer.parseInt(content[8]));
					break;
				case "Root":
					e = new Root(Integer.parseInt(content[8]));
					break;
				case "Shield":
					e = new Shield(Integer.parseInt(content[8]));
					break;
				case "Shock":
					e = new Shock(Integer.parseInt(content[8]));
					break;
				case "Silence":
					e = new Silence(Integer.parseInt(content[8]));
					break;
				case "SpeedUp":
					e = new SpeedUp(Integer.parseInt(content[8]));
					break;
				case "Stun":
					e = new Stun(Integer.parseInt(content[8]));
					break;
				}
			}
			switch (content[0]) {
			case "CC":
				a = new CrowdControlAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), e);
				break;
			case "DMG":
				a = new DamagingAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), Integer.parseInt(content[7]));
				break;
			case "HEL":
				a = new HealingAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), Integer.parseInt(content[7]));
				break;
			}
			availableAbilities.add(a);
			line = br.readLine();
		}
		br.close();
	}
	

	public static void loadChampions(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			Champion c = null;
			switch (content[0]) {
				case "A":
				c = new AntiHero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;

			case "H":
				c = new Hero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;
			case "V":
				c = new Villain(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;
				
			}

			c.getAbilities().add(findAbilityByName(content[8]));
			c.getAbilities().add(findAbilityByName(content[9]));
			c.getAbilities().add(findAbilityByName(content[10]));
			availableChampions.add(c);
			line = br.readLine();
		}
		br.close();
	}

	private static Ability findAbilityByName(String name) {
		for (Ability a : availableAbilities) {
			if (a.getName().equals(name))
				return a;
		}
		return null;
	}

	public void placeCovers() {
		int i = 0;
		while (i < 5) {
			int x = ((int) (Math.random() * (BOARDWIDTH - 2))) + 1;
			int y = (int) (Math.random() * BOARDHEIGHT);

			if (board[x][y] == null) {
				Cover cover = new Cover(x, y);
				board[x][y] = cover;
				Covers.add(cover);
				i++;
			}
		}

	}

	public void placeChampions() {
		int i = 1;
		for (Champion c : firstPlayer.getTeam()) {
			board[0][i] = c;
			c.setLocation(new Point(0, i));
			i++;
		}
		i = 1;
		for (Champion c : secondPlayer.getTeam()) {
			board[BOARDHEIGHT - 1][i] = c;
			c.setLocation(new Point(BOARDHEIGHT - 1, i));
			i++;
		}

	}

	public static ArrayList<Champion> getAvailableChampions() {
		return availableChampions;
	}

	public static ArrayList<Ability> getAvailableAbilities() {
		return availableAbilities;
	}

	public Player getFirstPlayer() {
		return firstPlayer;
	}

	public Player getSecondPlayer() {
		return secondPlayer;
	}

	public Object[][] getBoard() {
		return board;
	}

	public PriorityQueue getTurnOrder() {
		return turnOrder;
	}

	public boolean isFirstLeaderAbilityUsed() {
		return firstLeaderAbilityUsed;
	}

	public boolean isSecondLeaderAbilityUsed() {
		return secondLeaderAbilityUsed;
	}

	public static int getBoardwidth() {
		return BOARDWIDTH;
	}

	public static int getBoardheight() {
		return BOARDHEIGHT;
	}

	/**
	 * @return the covers
	 */
	public final ArrayList<Cover> getCovers() {
		return Covers;
	}

	/**
	 * @return the deadChampion
	 */
	public final ArrayList<Champion> getDeadChampion() {
		return deadChampion;
	}

	public Champion getCurrentChampion() {
		return (Champion) turnOrder.peekMin();
	}

	public Player checkGameOver() {
		if (firstPlayer.getTeam().isEmpty())
			return secondPlayer;

		if (secondPlayer.getTeam().isEmpty())
			return firstPlayer;

		return null;
	}

	public void move(Direction d) throws UnallowedMovementException, NotEnoughResourcesException {
		Champion champion = this.getCurrentChampion();

		if (champion.getCondition() == Condition.ROOTED)
			throw new UnallowedMovementException("Champion is rooted");

		if (champion.getCurrentActionPoints() < 1)
			throw new NotEnoughResourcesException("Not Enough Current Action Points");
		champion.setCurrentActionPoints(champion.getCurrentActionPoints() - 1);

		Point point = new Point();
		int championx = (int) champion.getLocation().getX();
		int championy = (int) champion.getLocation().getY();
		int x = (int) champion.getLocation().getX();
		int y = (int) champion.getLocation().getY();

		if (d.equals(Direction.UP)) {

			if (x >= 4)
				throw new UnallowedMovementException("Out of Bounds");
			x++;
			if (board[x][y] != null)
				throw new UnallowedMovementException("Location is not empty");

			point = new Point(x, y);

		}

		else if (d.equals(Direction.DOWN)) {

			if (x <= 0)
				throw new UnallowedMovementException("Out of Bounds");
			x--;
			if (board[x][y] != null)
				throw new UnallowedMovementException("Location is not empty");

			point = new Point(x, y);

		}

		else if (d.equals(Direction.RIGHT)) {

			if (y >= 4)
				throw new UnallowedMovementException("Out of Bounds");
			y++;
			if (board[x][y] != null)
				throw new UnallowedMovementException("Location is not empty");

			point = new Point(x, y);

		}

		if (d.equals(Direction.LEFT)) {

			if (y <= 0)
				throw new UnallowedMovementException("Out of Bounds");
			y--;
			if (board[x][y] != null)
				throw new UnallowedMovementException("Location is not empty");

			point = new Point(x, y);

		}

		champion.setLocation(point);
		board[championx][championy] = null;
		board[x][y] = champion;

	}

	public void attack(Direction d) throws ChampionDisarmedException, NotEnoughResourcesException {
		Champion attacker = this.getCurrentChampion();
		Damageable target = null;

		ArrayList<Effect> attackerEffects = attacker.getAppliedEffects();

		for (Effect effect : attackerEffects) {
			if (effect instanceof Disarm)
				throw new ChampionDisarmedException("Champion is Disarmed");
		}

		if (attacker.getCurrentActionPoints() < 2)
			throw new NotEnoughResourcesException("Not Enough Current Action Points");
		attacker.setCurrentActionPoints(attacker.getCurrentActionPoints() - 2);

		// Gets the Target
		int championx = (int) attacker.getLocation().getX();
		int championy = (int) attacker.getLocation().getY();
		int targetx = -1;
		int targety = -1;

		if (d == Direction.UP)
			for (int i = 1; i <= attacker.getAttackRange(); i++) {

				if (championx + i > 4)
					return;
				if (board[championx + i][championy] instanceof Damageable) {
					targetx = championx + i;
					targety = championy;
					break;
				}
			}

		else if (d == Direction.DOWN)
			for (int i = 1; i <= attacker.getAttackRange(); i++) {
				if (championx - i < 0)
					return;
				if (board[championx - i][championy] instanceof Damageable) {
					targetx = championx - i;
					targety = championy;
					break;
				}

			}

		else if (d == Direction.RIGHT)

			for (int i = 1; i <= attacker.getAttackRange(); i++) {
				if (championy + i > 4)
					return;
				if (board[championx][championy + i] instanceof Damageable) {
					targetx = championx;
					targety = championy + i;
					break;
				}
			}
		else if (d == Direction.LEFT)
			for (int i = 1; i <= attacker.getAttackRange(); i++) {

				if (championy - i < 0)
					return;

				if (board[championx][championy - i] instanceof Damageable) {
					targetx = championx;
					targety = championy - i;
					break;
				}
			}

		if (targetx == -1 || targety == -1)
			return;

		target = (Damageable) board[targetx][targety];

		// ATTACKS
		if (target instanceof Cover) {
			target.setCurrentHP(target.getCurrentHP() - attacker.getAttackDamage());
			if (target.getCurrentHP() <= 0) {
				target = null;
				board[targetx][targety] = null;
			}

		}

		else if (target instanceof Champion) {

			Player attackerPlayer = whichPlayer(attacker);
			Player targetPlayer = whichPlayer((Champion) target);

			if (attackerPlayer.equals(targetPlayer))
				return;

			int damage = attacker.getAttackDamage();

			if ((attacker instanceof Hero && target instanceof Villain)
					|| (attacker instanceof Villain && target instanceof Hero)
					|| (attacker instanceof AntiHero && (target instanceof Villain || target instanceof Hero))
					|| ((attacker instanceof Villain || attacker instanceof Hero) && target instanceof AntiHero))
				damage = (int) (attacker.getAttackDamage() * 1.5);

			ArrayList<Effect> targetEffects = ((Champion) target).getAppliedEffects();

			for (Effect effect : targetEffects) {

				if (effect instanceof Shield) {
//					Health Remains the same and removes the shield
					damage = 0;
					effect.remove((Champion) target);
					targetEffects.remove(effect);
					break;
				}

				if (effect instanceof Dodge) {

					int dodger = ((int) Math.round(1 + Math.random() * 100));
					if (dodger <= 50)
						damage = 0;
					break;
				}

			}

			target.setCurrentHP(target.getCurrentHP() - damage);

			if (target.getCurrentHP() <= 0) {
				((Champion) target).setCondition(Condition.KNOCKEDOUT);
				board[targetx][targety] = null;
				targetPlayer.getTeam().remove(target);

				if (target instanceof Champion)
					deadChampion.add((Champion) target);

				// REMOVE FORM TURN ORDER
				PriorityQueue temp = new PriorityQueue(turnOrder.size());
				Champion removed = (Champion) turnOrder.remove();

				while (!removed.equals((Champion) target) && !(turnOrder.isEmpty())) {
					temp.insert(removed);
					removed = (Champion) turnOrder.remove();
					;
				}

				while (!temp.isEmpty())
					turnOrder.insert(temp.remove());
			}
		}

	}

	private void dead(ArrayList<Damageable> targets) {
		for (Damageable target : targets) {

			if (target.getCurrentHP() <= 0) {

				int targetx = (int) target.getLocation().getX();
				int targety = (int) target.getLocation().getY();
				if (target instanceof Champion) {

					deadChampion.add((Champion) target);

					((Champion) target).setCondition(Condition.KNOCKEDOUT);
					board[targetx][targety] = null;
					Player targetPlayer = whichPlayer((Champion) target);
					targetPlayer.getTeam().remove(target);

					// REMOVE FORM TURN ORDER
					PriorityQueue temp = new PriorityQueue(turnOrder.size());

					while (!turnOrder.isEmpty()) {
						Champion removed = (Champion) turnOrder.remove();
						if (!removed.equals(target))
							temp.insert(removed);

					}

					while (!temp.isEmpty())
						turnOrder.insert(temp.remove());

				} else {
					target = null;
					board[targetx][targety] = null;
				}

			}
		}
	}

	private ArrayList<Damageable> surroundFriendlyTargetsAdder(Champion champion) {
		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		Player player = whichPlayer(champion);
		int x = (int) champion.getLocation().getX();
		int y = (int) champion.getLocation().getY();
		int xUp = x == 4 ? 4 : x + 1;
		int xDown = x == 0 ? 0 : x - 1;
		int yLeft = y == 0 ? 0 : y - 1;
		int yRight = y == 4 ? 4 : y + 1;

		for (int targetx = xDown; targetx <= xUp; targetx++) {
			for (int targety = yLeft; targety <= yRight; targety++) {
				if (board[targetx][targety] instanceof Champion && player.getTeam().contains(board[targetx][targety])
						&& !(board[x][y].equals(board[targetx][targety])))
					targets.add((Damageable) board[targetx][targety]);
			}

		}

		return targets;
	}

	private ArrayList<Damageable> surroundEnemyTargetsAdder(Champion champion) {
		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		Player player = whichPlayer(champion);
		int x = (int) champion.getLocation().getX();
		int y = (int) champion.getLocation().getY();
		int xUp = x == 4 ? 4 : x + 1;
		int xDown = x == 0 ? 0 : x - 1;
		int yLeft = y == 0 ? 0 : y - 1;
		int yRight = y == 4 ? 4 : y + 1;

		for (int targetx = xDown; targetx <= xUp; targetx++) {
			for (int targety = yLeft; targety <= yRight; targety++) {
				if (board[targetx][targety] instanceof Champion) {
					if (player.equals(firstPlayer) && secondPlayer.getTeam().contains(board[targetx][targety])
							&& !(board[x][y].equals(board[targetx][targety])))
						targets.add((Damageable) board[targetx][targety]);

					if (player.equals(secondPlayer) && firstPlayer.getTeam().contains(board[targetx][targety])
							&& !(board[x][y].equals(board[targetx][targety])))
						targets.add((Damageable) board[targetx][targety]);

				} else if (board[targetx][targety] instanceof Cover)
					targets.add((Damageable) board[targetx][targety]);
			}

		}

		return targets;
	}

	public Player whichPlayer(Champion champion) {
		if (firstPlayer.getTeam().contains(champion))
			return firstPlayer;
		if (secondPlayer.getTeam().contains(champion))
			;
		return secondPlayer;

	}

	public void castAbility(Ability a)
			throws NotEnoughResourcesException, AbilityUseException, CloneNotSupportedException {

		Champion champion = this.getCurrentChampion();
		int championx = (int) champion.getLocation().getX();
		int championy = (int) champion.getLocation().getY();
		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		Player player = whichPlayer(champion);

		if (champion.getMana() < a.getManaCost() || champion.getCurrentActionPoints() < a.getRequiredActionPoints())
			throw new NotEnoughResourcesException("Not Enough Resources");

		if (a.getCurrentCooldown() != 0)
			throw new AbilityUseException("Ability is on Cooldown");

		ArrayList<Effect> effects = champion.getAppliedEffects();
		for (Effect effect : effects)
			if (effect instanceof Silence)
				throw new AbilityUseException("Champion is silenced");

		if (a.getCastArea() == AreaOfEffect.SELFTARGET
				&& (a instanceof HealingAbility || (a instanceof CrowdControlAbility
						&& ((CrowdControlAbility) a).getEffect().getType() == EffectType.BUFF)))
			targets.add(champion);

		else if (a.getCastArea() == AreaOfEffect.TEAMTARGET) {
			if (a instanceof HealingAbility || (a instanceof CrowdControlAbility
					&& ((CrowdControlAbility) a).getEffect().getType() == EffectType.BUFF)) {

				for (Champion target : player.getTeam()) {

					int targetx = (int) target.getLocation().getX();
					int targety = (int) target.getLocation().getY();
					int distance = Math.abs(targetx - championx) + Math.abs(targety - championy);
					if (distance <= a.getCastRange())
						targets.add(target);
				}
			} else if (a instanceof DamagingAbility) {
				if (player.equals(firstPlayer)) {

					for (Champion target : secondPlayer.getTeam()) {
						boolean f = false;
						int targetx = (int) target.getLocation().getX();
						int targety = (int) target.getLocation().getY();
						int distance = Math.abs(targetx - championx) + Math.abs(targety - championy);
						if (distance <= a.getCastRange()) {

							for (Effect effect : target.getAppliedEffects()) {

								if (effect instanceof Shield) {

									effect.remove(target);
									target.getAppliedEffects().remove(effect);
									f = true;
									break;

								}
							}
							if (!f)
								targets.add(target);

						}

					}
				}

				else {

					for (Champion target : firstPlayer.getTeam()) {
						boolean f = false;
						int targetx = (int) target.getLocation().getX();
						int targety = (int) target.getLocation().getY();
						int distance = Math.abs(targetx - championx) + Math.abs(targety - championy);
						if (distance <= a.getCastRange()) {

							for (Effect effect : target.getAppliedEffects()) {

								if (effect instanceof Shield) {

									effect.remove(target);
									target.getAppliedEffects().remove(effect);
									f = true;
									break;

								}
							}
							if (!f)
								targets.add(target);

						}

					}
				}
			}

			else if (a instanceof CrowdControlAbility
					&& ((CrowdControlAbility) a).getEffect().getType() == EffectType.DEBUFF)
				if (player.equals(firstPlayer)) {
					for (Champion target : secondPlayer.getTeam()) {
						int targetx = (int) target.getLocation().getX();
						int targety = (int) target.getLocation().getY();
						int distance = Math.abs(targetx - championx) + Math.abs(targety - championy);
						if (distance <= a.getCastRange())
							targets.add(target);

					}
				}

				else
					for (Champion target : firstPlayer.getTeam()) {
						int targetx = (int) target.getLocation().getX();
						int targety = (int) target.getLocation().getY();
						int distance = Math.abs(targetx - championx) + Math.abs(targety - championy);
						if (distance <= a.getCastRange())
							targets.add(target);

					}

		}

		else if ((a.getCastArea() == AreaOfEffect.SURROUND)
				&& (a instanceof HealingAbility || (a instanceof CrowdControlAbility
						&& ((CrowdControlAbility) a).getEffect().getType() == EffectType.BUFF)))
			targets = surroundFriendlyTargetsAdder(champion);

		else if ((a.getCastArea() == AreaOfEffect.SURROUND) && (a instanceof DamagingAbility)) {

			targets = surroundEnemyTargetsAdder(champion);
			for (int i = 0; i < targets.size(); i++) {

				Damageable target = targets.get(i);
				if (target instanceof Champion) {
					Champion brokenShield = null;
					Effect removed = null;
					ArrayList<Effect> targetEffects = ((Champion) target).getAppliedEffects();

					for (int j = 0; j < targetEffects.size(); j++) {
						Effect effect = targetEffects.get(j);
						if (effect instanceof Shield) {
							removed = effect;
							effect.remove((Champion) target);
							brokenShield = (Champion) target;
							break;
						}

					}

					if (removed != null || brokenShield != null) {

						targetEffects.remove(removed);
						targets.remove(brokenShield);
					}
				}
			}
		} else if ((a.getCastArea() == AreaOfEffect.SURROUND) && (a instanceof CrowdControlAbility
				&& ((CrowdControlAbility) a).getEffect().getType() == EffectType.DEBUFF)) {
			targets = surroundEnemyTargetsAdder(champion);
			ArrayList<Cover> removed = new ArrayList<Cover>();
			for (Damageable target : targets)
				if (target instanceof Cover)
					removed.add((Cover) target);
			for (int i = 0; i < removed.size(); i++)
				targets.remove(removed.get(i));

		}

		if (targets.isEmpty()) {
			champion.setMana(champion.getMana() - a.getManaCost());
			champion.setCurrentActionPoints(champion.getCurrentActionPoints() - a.getRequiredActionPoints());

			return;
		}

		a.execute(targets);
		champion.setMana(champion.getMana() - a.getManaCost());
		champion.setCurrentActionPoints(champion.getCurrentActionPoints() - a.getRequiredActionPoints());

		dead(targets);
	}

	public void castAbility(Ability a, Direction d)
			throws NotEnoughResourcesException, AbilityUseException, CloneNotSupportedException {

		Champion champion = this.getCurrentChampion();
		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		ArrayList<Damageable> allTargets = new ArrayList<Damageable>();
		Player player = whichPlayer(champion);

		if (champion.getMana() < a.getManaCost() || champion.getCurrentActionPoints() < a.getRequiredActionPoints())
			throw new NotEnoughResourcesException("Not Enough Resources");

		if (a.getCurrentCooldown() != 0)
			throw new AbilityUseException("Ability is on Cooldown");

		ArrayList<Effect> championeffects = champion.getAppliedEffects();
		for (Effect effect : championeffects)
			if (effect instanceof Silence)
				throw new AbilityUseException("Champion is silenced");

		if (a.getCastArea() != AreaOfEffect.DIRECTIONAL)
			throw new AbilityUseException("Can not use ability");

		int x = (int) champion.getLocation().getX();
		int y = (int) champion.getLocation().getY();

		if (d == Direction.UP)
			for (int i = 1; i <= a.getCastRange(); i++) {

				if (x + i > 4)
					break;
				if (board[x + i][y] instanceof Damageable)
					allTargets.add((Damageable) board[x + i][y]);
			}

		else if (d == Direction.DOWN)
			for (int i = 1; i <= a.getCastRange(); i++) {

				if (x - i < 0)
					break;
				if (board[x - i][y] instanceof Damageable)
					allTargets.add((Damageable) board[x - i][y]);

			}

		else if (d == Direction.RIGHT)

			for (int i = 1; i <= a.getCastRange(); i++) {

				if (y + i > 4)
					break;
				if (board[x][y + i] instanceof Damageable)
					allTargets.add((Damageable) board[x][y + i]);
			}
		else if (d == Direction.LEFT)
			for (int i = 1; i <= a.getCastRange(); i++) {
				if (y - i < 0)
					break;

				if (board[x][y - i] instanceof Damageable)
					allTargets.add((Damageable) board[x][y - i]);
			}

		for (Damageable target : allTargets) {
			if (a instanceof HealingAbility || (a instanceof CrowdControlAbility
					&& ((CrowdControlAbility) a).getEffect().getType() == EffectType.BUFF)) {
				if (player.getTeam().contains(target)) {

					targets.add(target);
				}

			}

			else if (a instanceof DamagingAbility) {
				if (target instanceof Cover)
					targets.add(target);

				else if (player.equals(firstPlayer)) {
					if (secondPlayer.getTeam().contains(target)) {
						ArrayList<Effect> removed = new ArrayList<Effect>();
						ArrayList<Effect> targetsEffect = ((Champion) target).getAppliedEffects();
						for (int i = 0; i < targetsEffect.size(); i++) {
							Effect effect = targetsEffect.get(i);
							if (effect instanceof Shield) {
								effect.remove((Champion) target);
								removed.add(effect);
							}
						}

						if (removed.isEmpty())
							targets.add(target);
						for (Effect effect : removed)
							targetsEffect.remove(effect);
					}

				} else if (player.equals(secondPlayer)) {
					if (firstPlayer.getTeam().contains(target)) {
						ArrayList<Effect> removed = new ArrayList<Effect>();
						ArrayList<Effect> targetsEffect = ((Champion) target).getAppliedEffects();
						for (int i = 0; i < targetsEffect.size(); i++) {
							Effect effect = targetsEffect.get(i);
							if (effect instanceof Shield) {
								effect.remove((Champion) target);
								removed.add(effect);
							}
						}

						if (removed.isEmpty())
							targets.add(target);
						for (Effect effect : removed)
							targetsEffect.remove(effect);
					}
				}
			}

			else if ((a instanceof CrowdControlAbility
					&& ((CrowdControlAbility) a).getEffect().getType() == EffectType.DEBUFF)) {
				if (player.equals(firstPlayer)) {
					if (secondPlayer.getTeam().contains(target))
						targets.add(target);
				} else if (player.equals(secondPlayer)) {
					if (firstPlayer.getTeam().contains(target))
						targets.add(target);
				}
			}
		}

		if (targets.isEmpty()) {
			champion.setMana(champion.getMana() - a.getManaCost());
			champion.setCurrentActionPoints(champion.getCurrentActionPoints() - a.getRequiredActionPoints());

			return;
		}

		a.execute(targets);
		champion.setMana(champion.getMana() - a.getManaCost());
		champion.setCurrentActionPoints(champion.getCurrentActionPoints() - a.getRequiredActionPoints());

		dead(targets);

	}

	public void castAbility(Ability a, int x, int y)
			throws GameActionException, CloneNotSupportedException, AbilityUseException {
		Champion champion = this.getCurrentChampion();
		int championx = (int) champion.getLocation().getX();
		int championy = (int) champion.getLocation().getY();
		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		Player player = whichPlayer(champion);

		if (champion.getMana() < a.getManaCost() || champion.getCurrentActionPoints() < a.getRequiredActionPoints())
			throw new NotEnoughResourcesException("Not Enough Resources");

		if (a.getCurrentCooldown() != 0)
			throw new AbilityUseException("Ability is on Cooldown");

		ArrayList<Effect> effects = champion.getAppliedEffects();
		for (Effect effect : effects)
			if (effect instanceof Silence)
				throw new AbilityUseException("Champion is silenced");

		if (a.getCastArea() != AreaOfEffect.SINGLETARGET)
			throw new AbilityUseException("Can not use ability");

		if (board[x][y] == null)
			throw new InvalidTargetException("Cell is empty");

		int distance = Math.abs(x - championx) + Math.abs(y - championy);
		if (distance > a.getCastRange())
			throw new AbilityUseException("Out of Bounds");

		if (a instanceof HealingAbility || (a instanceof CrowdControlAbility
				&& ((CrowdControlAbility) a).getEffect().getType() == EffectType.BUFF)) {

			if (board[x][y] instanceof Cover)
				throw new InvalidTargetException("Can not buff covers");

			if (player.getTeam().contains(board[x][y]))
				targets.add((Damageable) board[x][y]);
			else
				throw new InvalidTargetException("Can not buff enemy");

		}

		if (a instanceof DamagingAbility) {
			if ((player.equals(firstPlayer) && firstPlayer.getTeam().contains(board[x][y]))
					|| (player.equals(secondPlayer) && secondPlayer.getTeam().contains(board[x][y])))
				throw new InvalidTargetException("Can not cast damaging ability on friendly targets");

			if (x == championx && y == championy)
				throw new InvalidTargetException("Invalid Target");
			if (board[x][y] instanceof Cover)
				targets.add((Damageable) board[x][y]);
			else if (player.equals(firstPlayer) && secondPlayer.getTeam().contains(board[x][y])) {

				ArrayList<Effect> removed = new ArrayList<Effect>();
				ArrayList<Effect> targetsEffect = ((Champion) board[x][y]).getAppliedEffects();
				for (int i = 0; i < targetsEffect.size(); i++) {
					Effect effect = targetsEffect.get(i);
					if (effect instanceof Shield) {
						effect.remove((Champion) board[x][y]);
						removed.add(effect);

					}
				}

				if (removed.isEmpty())
					targets.add((Damageable) board[x][y]);
				for (Effect effect : removed)
					targetsEffect.remove(effect);

			} else if (player.equals(secondPlayer) && firstPlayer.getTeam().contains(board[x][y])) {

				ArrayList<Effect> removed = new ArrayList<Effect>();
				ArrayList<Effect> targetsEffect = ((Champion) board[x][y]).getAppliedEffects();
				for (int i = 0; i < targetsEffect.size(); i++) {
					Effect effect = targetsEffect.get(i);
					if (effect instanceof Shield) {
						effect.remove((Champion) board[x][y]);
						removed.add(effect);
					}
				}

				if (removed.isEmpty())
					targets.add((Damageable) board[x][y]);
				for (Effect effect : removed)
					targetsEffect.remove(effect);
			}
		}

		if ((a instanceof CrowdControlAbility && ((CrowdControlAbility) a).getEffect().getType() == EffectType.DEBUFF)
				&& (board[x][y] instanceof Damageable)) {

			if (x == championx && y == championy)
				throw new InvalidTargetException("Invalid Target");
			if (board[x][y] instanceof Cover)
				throw new InvalidTargetException("Invalid Target");

			if (player.equals(firstPlayer) && secondPlayer.getTeam().contains(board[x][y]))
				targets.add((Damageable) board[x][y]);
			else if (player.equals(secondPlayer) && firstPlayer.getTeam().contains(board[x][y]))
				targets.add((Damageable) board[x][y]);
		}

		if (targets.isEmpty()) {
			champion.setMana(champion.getMana() - a.getManaCost());
			champion.setCurrentActionPoints(champion.getCurrentActionPoints() - a.getRequiredActionPoints());

			return;
		}

		a.execute(targets);
		champion.setMana(champion.getMana() - a.getManaCost());
		champion.setCurrentActionPoints(champion.getCurrentActionPoints() - a.getRequiredActionPoints());

		dead(targets);

	}

	public void useLeaderAbility() throws GameActionException {
		Champion lead = this.getCurrentChampion();
		Player player = whichPlayer(lead);

		if (!lead.equals(player.getLeader()))
			throw new LeaderNotCurrentException("The current champion is not the leader");

		if ((firstPlayer.getTeam().contains(lead) && firstLeaderAbilityUsed == true)
				|| (secondPlayer.getTeam().contains(lead) && secondLeaderAbilityUsed == true))
			throw new LeaderAbilityAlreadyUsedException("Leader ability already used");

		if (lead instanceof Hero) {
			ArrayList<Champion> team = player.getTeam();
			lead.useLeaderAbility(team);
		}

		if (lead instanceof Villain) {
			ArrayList<Champion> team = null;
			if (player.equals(firstPlayer))
				team = secondPlayer.getTeam();
			if (player.equals(secondPlayer))
				team = firstPlayer.getTeam();

			lead.useLeaderAbility(team);
		}

		if (lead instanceof AntiHero) {
			ArrayList<Champion> team = new ArrayList<Champion>();
			ArrayList<Champion> team1 = firstPlayer.getTeam();
			ArrayList<Champion> team2 = secondPlayer.getTeam();

			for (Champion champion : team1) {
				if (!champion.equals(firstPlayer.getLeader()))
					team.add(champion);
			}

			for (Champion champion : team2) {

				if (!champion.equals(secondPlayer.getLeader()))
					team.add(champion);
			}

			lead.useLeaderAbility(team);
		}

		if (player.equals(firstPlayer))
			firstLeaderAbilityUsed = true;
		else
			secondLeaderAbilityUsed = true;

	}

	private boolean hasEffect(Champion currentChampion, String s) {
		for (Effect e : currentChampion.getAppliedEffects()) {
			if (e.getName().equals(s))
				return true;
		}
		return false;
	}
	
	public void endTurn() {
		turnOrder.remove();
		if (turnOrder.isEmpty())
			prepareChampionTurns();
		while (!turnOrder.isEmpty() && hasEffect((Champion) turnOrder.peekMin(), "Stun")) {
			Champion current = (Champion) turnOrder.peekMin();
			updateTimers(current);
			turnOrder.remove();
		}
		Champion current = (Champion) turnOrder.peekMin();
		updateTimers(current);
		
		current.setCurrentActionPoints(current.getMaxActionPointsPerTurn());
	}

	public void prepareChampionTurns() {
		for (Champion c : firstPlayer.getTeam()) 
			turnOrder.insert(c);
		for (Champion c : secondPlayer.getTeam()) 
			turnOrder.insert(c);
		
	}
	private void updateTimers(Champion current) {
		int i = 0;
		while (i < current.getAppliedEffects().size()) {
			Effect e = current.getAppliedEffects().get(i);
			e.setDuration(e.getDuration() - 1);
			if (e.getDuration() == 0) {
				current.getAppliedEffects().remove(e);
				e.remove(current);

			} else
				i++;
		}
		for (Ability a : current.getAbilities()) {
			if (a.getCurrentCooldown() > 0)
				a.setCurrentCooldown(a.getCurrentCooldown() - 1);
		}
		for (int j = 0; j<current.getAppliedEffects().size(); j++){
			if (current.getAppliedEffects().get(j) instanceof Shock){
				current.getAppliedEffects().get(j).apply(current);
			}
		}
	}

	public Champion nextTurn() {
		if (turnOrder.size() == 1) {
			return null;
		}
		Champion temp = (Champion) turnOrder.remove();
		Champion champion = (Champion) turnOrder.peekMin();
		turnOrder.insert(temp);
		return champion;

	}
}