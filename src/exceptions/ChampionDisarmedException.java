package exceptions;

public class ChampionDisarmedException extends GameActionException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ChampionDisarmedException() {
		super();
	}

	public ChampionDisarmedException(String s) 
	{
		super(s);
	}
	
	
}
