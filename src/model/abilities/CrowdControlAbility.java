package model.abilities;

import java.util.ArrayList;

import model.effects.Effect;
import model.world.Champion;
import model.world.Damageable;

public class CrowdControlAbility extends Ability {
	private Effect effect;

	public CrowdControlAbility(String name, int cost, int baseCoolDown, int castRadius, AreaOfEffect area, int required,
			Effect effect) {
		super(name, cost, baseCoolDown, castRadius, area, required);
		this.effect = effect;

	}

	public Effect getEffect() {
		return effect;
	}

	@Override
	public void execute(ArrayList<Damageable> targets) throws CloneNotSupportedException {
		
		for(int i = 0 ; i < targets.size() ; i++)			
		{
			Champion target = (Champion) targets.get(i);
			Effect e =  (Effect) effect.clone();
			e.apply((Champion) target);
			((Champion) target).getAppliedEffects().add(e);
		}
		this.setCurrentCooldown(this.getBaseCooldown());
	}

	@Override
	public String toStringHTML() {
		String result = "<html>" + 
				"- Name: " + getName() + "<br>" + 
				"- Type: " + "Crowd Control" + "<br>" + 
				"- Cast Area:" + getCastArea()  + "<br>" +
				"- Mana Cost:" + getManaCost() + "<br>" + 
				"- Cast Range:" + getCastRange()  + "<br>" +
				"- Required Action Points:" + getRequiredActionPoints()  + "<br>";
		return result;
	}

}
