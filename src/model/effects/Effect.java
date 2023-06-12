package model.effects;

import model.world.*;

public abstract class Effect implements Cloneable {
	private String name;
	private EffectType type;
	private int duration;

	public Effect(String name, int duration, EffectType type) {
		this.name = name;
		this.type = type;
		this.duration = duration;
	}

	public String getName() {
		return name;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public EffectType getType() {
		return type;
	}

	public Object clone() throws CloneNotSupportedException {
		return (Effect) super.clone();
	}

	abstract public void apply(Champion c);

	abstract public void remove(Champion c);

	
	public String toStringHTML() {
		String result = "<html>" + 
				"- Name: " + name + "<br>" + 
				"- Type: " + type + "<br>" + 
				"- Duration:" + duration  + "<br>" ;
				
		return result;
	}
	
	@Override
	public String toString() {
		String result = 
				"- Name: " + name + '\n' + 
				"- Type: " + type + '\n' + 
				"- Duration:" + duration  + '\n' ;
				
		return result;
	}

}
