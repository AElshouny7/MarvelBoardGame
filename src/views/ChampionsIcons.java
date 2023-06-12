package views;

import java.util.ArrayList;

import javax.swing.ImageIcon;

public class ChampionsIcons {

	private ImageIcon captainAmerica;
	private ImageIcon deadPool;
	private ImageIcon drStrange;
	private ImageIcon electro;
	private ImageIcon ghostRider;
	private ImageIcon hela;
	private ImageIcon hulk;
	private ImageIcon iceMan;
	private ImageIcon ironMan;
	private ImageIcon loki;
	private ImageIcon quickSilver;
	private ImageIcon spiderMan;
	private ImageIcon thor;
	private ImageIcon venom;
	private ImageIcon yellowJacket;
	
	private ArrayList<ImageIcon> championsSewar;

	public ChampionsIcons() {

		captainAmerica = new ImageIcon("Captain America.png");
		deadPool = new ImageIcon("Deadpool.png");
		drStrange = new ImageIcon("Dr Strange.png");
		electro = new ImageIcon("Electro.png");
		ghostRider = new ImageIcon("Ghost Rider.png");
		hela = new ImageIcon("Hela.png");
		hulk = new ImageIcon("Hulk.png");
		iceMan = new ImageIcon("Iceman.png");
		ironMan = new ImageIcon("Iron Man.png");
		loki = new ImageIcon("Loki.png");
		quickSilver = new ImageIcon("Quicksilver.jpg");
		spiderMan = new ImageIcon("Spiderman.png");
		thor = new ImageIcon("Thor.png");
		venom = new ImageIcon("Venom.png");
		yellowJacket = new ImageIcon("yellow jacket.png");
		
		championsSewar = new ArrayList<ImageIcon>();
		
		championsSewar.add(captainAmerica);
		championsSewar.add(deadPool);
		championsSewar.add(drStrange);
		championsSewar.add(electro);
		championsSewar.add(ghostRider);
		championsSewar.add(hela);
		championsSewar.add(hulk);
		championsSewar.add(iceMan);
		championsSewar.add(ironMan);
		championsSewar.add(loki);
		championsSewar.add(quickSilver);
		championsSewar.add(spiderMan);
		championsSewar.add(thor);
		championsSewar.add(venom);
		championsSewar.add(yellowJacket);
		
	}

}
