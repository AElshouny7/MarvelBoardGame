package model.abilities;

import java.util.ArrayList;

import model.world.Damageable;

public class DamagingAbility extends Ability {
	
	private int damageAmount;
	
	public DamagingAbility(String name, int cost, int baseCoolDown, int castRadius, AreaOfEffect area,int required,int damageAmount) {
		super(name, cost, baseCoolDown, castRadius, area,required);
		this.damageAmount=damageAmount;
	}
	public int getDamageAmount() {
		return damageAmount;
	}
	public void setDamageAmount(int damageAmount) {
		this.damageAmount = damageAmount;
	}
	@Override
	public void execute(ArrayList<Damageable> targets) {
		// TODO Auto-generated method stub
		for(int i = 0 ; i < targets.size() ; i++)
		{
			Damageable t = targets.get(i);
			t.setCurrentHP( t.getCurrentHP() - damageAmount);
		}
		
		this.setCurrentCooldown(this.getBaseCooldown());
	}
	@Override
	public String toStringHTML() {
		String result = "<html>" + 
				"- Name: " + getName() + "<br>" + 
				"- Type: " + "Damaging" + "<br>" + 
				"- Cast Area:" + getCastArea()  + "<br>" +
				"- Mana Cost:" + getManaCost() + "<br>" + 
				"- Cast Range:" + getCastRange()  + "<br>" +
				"- Required Action Points:" + getRequiredActionPoints()  + "<br>";
		return result;
	}
	

}
