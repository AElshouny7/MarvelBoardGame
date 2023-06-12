package model.abilities;

import java.util.ArrayList;

import model.world.*;


public  class HealingAbility extends Ability {
	private int healAmount;

	public HealingAbility(String name,int cost, int baseCoolDown, int castRadius, AreaOfEffect area,int required, int healingAmount) {
		super(name,cost, baseCoolDown, castRadius, area,required);
		this.healAmount = healingAmount;
	}

	public int getHealAmount() {
		return healAmount;
	}

	public void setHealAmount(int healAmount) {
		this.healAmount = healAmount;
	}

	@Override
	public void execute(ArrayList<Damageable> targets) {
		for(Damageable t : targets)
			((Champion)t).setCurrentHP( ((Champion)t).getCurrentHP() + healAmount);
		
		this.setCurrentCooldown(this.getBaseCooldown());
	}

	@Override
	public String toStringHTML() {
		String result = "<html>" + 
				"- Name: " + getName() + "<br>" + 
				"- Type: " + "Healing" + "<br>" + 
				"- Cast Area:" + getCastArea()  + "<br>" +
				"- Mana Cost:" + getManaCost() + "<br>" + 
				"- Cast Range:" + getCastRange()  + "<br>" +
				"- Required Action Points:" + getRequiredActionPoints()  + "<br>";
		return result;
	}

	

	

}
