package model.effects;

import model.world.Champion;
import model.world.Condition;

public class Stun extends Effect {

	public Stun(int duration) {
		super("Stun", duration, EffectType.DEBUFF);
	}

	@Override
	public void apply(Champion c) {
		c.setCondition(Condition.INACTIVE);
		// REMOVE FROM TURN QUEUE
//		Target is not allowed to play their turn for the duration. EXCEPTION GameActionException
	}

	@Override
	public void remove(Champion c) 
	{
		if (c.getCondition() == Condition.ROOTED)
			c.setCondition(Condition.ROOTED);
		else 	
			c.setCondition(Condition.ACTIVE);
//		Target is  allowed to play their turn for the duration.
	}

	


}
