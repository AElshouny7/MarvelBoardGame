package model.world;

import java.util.ArrayList;

import model.effects.*;

public class Hero extends Champion {

	public Hero(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);

	}

	@Override
	public void useLeaderAbility(ArrayList<Champion> targets) {
		// TODO Auto-generated method stub
		for (int i = 0; i < targets.size(); i++) {
			Champion t = targets.get(i);
			ArrayList<Effect> effects = t.getAppliedEffects();
			ArrayList<Effect> removed = new ArrayList<Effect>();

			for (int j = 0; j < effects.size(); j++) {
				Effect e = effects.get(j);
				if (e.getType() == EffectType.DEBUFF)
					removed.add(e);

			}

			for (int k = 0; k < removed.size(); k++) {
				Effect e = removed.get(k);
				effects.remove(e);
			}

			Embrace embrace = new Embrace(2);
			embrace.apply(t);
			effects.add(embrace);

		}
	}

	public String toStringHTML() {

		String result = "<html>" + "- Name: " + getName() + "<br>" + "- Type: Hero" + "<br>" + "- Action Points:"
				+ getMaxActionPointsPerTurn() + "<br>" + "- Normal Attack Range:" + getAttackDamage() + "<br>"
				+ "- Normal Attack Damage:" + getAttackRange() + "<br>" + "- Speed:" + getSpeed() + "<br>"
				+ "Abilities --> " + "<br>" + getAbilities().get(0).getName() + "<br>" + getAbilities().get(1).getName()
				+ "<br>" + getAbilities().get(2).getName() + "<br>";
		// WITHOUT HP AND MANA

		return result;
	}

	@Override
	public String myLeaderAbilityInfo() {

		String info = "<html>" + "Removes all negative effects from the player’s entire team and adds an Embrace effect"
				+ "<br>" + "to them which lasts for 2 turns.";
		return info;
	}

}
