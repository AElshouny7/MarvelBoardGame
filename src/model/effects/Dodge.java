package model.effects;
import model.world.Champion;

public class Dodge extends Effect {

	public Dodge(int duration) {
		super("Dodge", duration, EffectType.BUFF);
		
	}

	@Override
	public void apply(Champion c) {
		// Target has a 50% chance of dodging normal attacks. DID NOT DO IT YET
		c.setSpeed( (int) (c.getSpeed()*1.05) );
	}

	@Override
	public void remove(Champion c) {
		// Target has a 100% chance of dodging normal attacks. DID NOT DO IT YET
		c.setSpeed( (int) (c.getSpeed()/1.05));
	}

	

}
