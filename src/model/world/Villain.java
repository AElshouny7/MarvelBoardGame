package model.world;

import java.util.ArrayList;

public class Villain extends Champion {

	public Villain(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);

	}

	@Override
	public void useLeaderAbility(ArrayList<Champion> targets) {
		// TODO Auto-generated method stub
		for (int i = 0; i < targets.size(); i++) {
			Champion t = targets.get(i);
			if (t.getCurrentHP() < t.getMaxHP() * 1.3)
				t.setCondition(Condition.KNOCKEDOUT);
		}
	}

	@Override
	public String toStringHTML() {
		String result = "<html>" + "- Name: " + getName() + "<br>" + "- Type: Villain" + "<br>" + "- Action Points:"
				+ getMaxActionPointsPerTurn() + "<br>" + "- Normal Attack Range:" + getAttackDamage() + "<br>"
				+ "- Normal Attack Damage:" + getAttackRange() + "<br>" + "- Speed:" + getSpeed() + "<br>"
				+ "Abilities --> " + "<br>" + getAbilities().get(0).getName() + "<br>" + getAbilities().get(1).getName()
				+ "<br>" + getAbilities().get(2).getName() + "<br>";
		// WITHOUT HP AND MANA

		return result;
	}

	@Override
	public String myLeaderAbilityInfo() {
		String info = "<html>" + "Immediately eliminates (knocks out) all enemy champions with less than 30% health"
				+ "<br>" + "points.";
		return info;
	}

}
