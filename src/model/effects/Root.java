package model.effects;

import model.world.Champion;
import model.world.Condition;

public class Root extends Effect {

	public Root( int duration) {
		super("Root", duration, EffectType.DEBUFF);
		
	}
	public void apply(Champion c) {
//		 EXCEPTION Target Can not move (UnallowedMovementException)
		if (c.getCondition().equals(Condition.INACTIVE))
			c.setCondition(Condition.INACTIVE);
		else
		c.setCondition(Condition.ROOTED);
	}
	@Override
	public void remove(Champion c) {
		if (c.getCondition() == Condition.INACTIVE)
			c.setCondition(Condition.INACTIVE);
		else if (c.getCondition() == Condition.ROOTED)
			c.setCondition(Condition.ROOTED);
		else
			c.setCondition(Condition.ACTIVE);
		
	}
	

}
