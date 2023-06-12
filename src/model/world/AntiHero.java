package model.world;

import java.util.ArrayList;

import model.effects.*;


public class AntiHero extends Champion {

	public AntiHero(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);

	}

	

	@Override
	public void useLeaderAbility(ArrayList<Champion> targets) {
		// TODO Auto-generated method stub
		for (Champion champion : targets)
		{
			Effect stun = new Stun(2);
			champion.getAppliedEffects().add(stun);
			stun.apply(champion);
		
			
			
		}
	}



	@Override
	public String toStringHTML() {
		String result = "<html>" + 
				"- Name: " + getName() + "<br>" + 
				"- Type: AntiHero" + "<br>" + 
				"- Action Points:" + getMaxActionPointsPerTurn()  + "<br>" +
				"- Normal Attack Range:" + getAttackDamage()  + "<br>" +
				"- Normal Attack Damage:" + getAttackRange()  + "<br>" +
				"- Speed:" + getSpeed() + "<br>" + "Abilities --> " + "<br>" + getAbilities().get(0).getName() + "<br>"
				+ getAbilities().get(1).getName() + "<br>" + getAbilities().get(2).getName() + "<br>";
				
		// WITHOUT HP AND MANA
		
		return result;
	}



	@Override
	public String myLeaderAbilityInfo() {
		String info = "<html>" + "All champions on the board except for the leaders of each team will be stunned" + "<br>"
				+ "for 2 turns.";
		return info;
	}
}
