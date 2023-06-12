package model.effects;

import model.world.Champion;

public class Silence extends Effect {

	public Silence( int duration) {
		super("Silence", duration, EffectType.DEBUFF);
		
	}

	@Override
	public void apply(Champion c) {
//		Target CAN NOT use abilities EXCEPTION AbilityUseException
		c.setCurrentActionPoints( c.getCurrentActionPoints() + 2 );
		c.setMaxActionPointsPerTurn( c.getMaxActionPointsPerTurn() + 2 );
	}

	@Override
	public void remove(Champion c) {
//		Target CAN use abilities
		c.setCurrentActionPoints( c.getCurrentActionPoints() - 2 );
		c.setMaxActionPointsPerTurn( c.getMaxActionPointsPerTurn() - 2 );
	}

	

}
