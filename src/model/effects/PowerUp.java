package model.effects;

import java.util.ArrayList;
import model.abilities.*;
import model.world.Champion;

public class PowerUp extends Effect {
	

	public PowerUp(int duration) {
		super("PowerUp", duration, EffectType.BUFF);
		
	}

	@Override
	public void apply(Champion c) {
		ArrayList<Ability> a = c.getAbilities();
		for (int i = 0 ; i < a.size() ; i++)
		{
			Ability aa = a.get(i);
			if( aa instanceof DamagingAbility )
				((DamagingAbility) aa).setDamageAmount( (int) (((DamagingAbility) aa).getDamageAmount()*1.2) );
			
			if( aa instanceof HealingAbility )
				((HealingAbility) aa).setHealAmount( (int) (((HealingAbility) aa).getHealAmount()*1.2) );
		}
	}

	@Override
	public void remove(Champion c) {
		ArrayList<Ability> a = c.getAbilities();
		for (int i = 0 ; i < a.size() ; i++)
		{
			Ability aa = a.get(i);
			if( aa instanceof DamagingAbility )
				((DamagingAbility) aa).setDamageAmount( (int) (((DamagingAbility) aa).getDamageAmount()/1.2) );
			
			if( aa instanceof HealingAbility )
				((HealingAbility) aa).setHealAmount( (int) (((HealingAbility) aa).getHealAmount()/1.2) );
		}
	}

	
	
}
