package model.effects;
import model.world.*;
import java.util.ArrayList;
import model.abilities.*;

public class Disarm extends Effect {
	

	public Disarm( int duration) {
		super("Disarm", duration, EffectType.DEBUFF);
		
	}

	@Override
	public void apply(Champion c) {
		// Target CAN NOT use normal attacks. DID NOT DO IT YET
		DamagingAbility punch = new DamagingAbility("Punch" , 0 , 1 , 1 , AreaOfEffect.SINGLETARGET , 1 , 50);
		ArrayList<Ability> a = c.getAbilities();
		a.add(punch);
	}
	

	@Override
	public void remove(Champion c) {
			// Target CAN use normal attacks. DID NOT DO IT YET
			ArrayList<Ability> a = c.getAbilities();
			for (int i = 0 ; i < a.size() ; i++)
			{
				Ability aa = a.get(i);
				if(aa.getName() == "Punch")
					a.remove(aa);
			}
		
	}

	
	
}
