package nl.tudelft.jpacman.fruit;

import nl.tudelft.jpacman.sprite.PacManSprites;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.npc.NPC;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FruitFactory {
	
	private final int LIFE_TIME = 10;
	private final int POMEGRANATE_DURATION = 4;
	private final int BELL_PEPPER_DURATION = 4;
	private final int TOMATO_DURATION = 4;
	private final int KIDNEY_BEAN_DURATION = 4;
	private final int POTATO_DURATION = 2;
	private final int FISH_DURATION = 2;

	private final PacManSprites sprites;
	private List<Fruit> fruits = new ArrayList<>();
	private List<Square> fruitPositions;
	private Random rand = new Random();
	
	private Fruit fish;
	private Fruit potato;
	private Fruit tomato;
	private Fruit pomgranate;
	private Fruit bellPepper;
	private Fruit kidneyBean;
	
	public FruitFactory(PacManSprites spriteStore, List<Square> fruitPos, List<NPC> npcs) {
		this.sprites = spriteStore;
		fruitPositions = fruitPos;
		fish = new Fish(sprites.getFishSprite(), LIFE_TIME, FISH_DURATION);
		fruits.add(fish);
		potato = new Potato(sprites.getPotatoSprite(), LIFE_TIME, POTATO_DURATION, npcs);
		fruits.add(potato);
		tomato = new Tomato(sprites.getTomatoSprite(), LIFE_TIME, TOMATO_DURATION);
		fruits.add(tomato);
		pomgranate = new Pomgranate(sprites.getPomgranateSprite(), LIFE_TIME, POMEGRANATE_DURATION, npcs);
		fruits.add(pomgranate);
		bellPepper = new BellPepper(sprites.getBellPepperSprite(), LIFE_TIME, BELL_PEPPER_DURATION);
		fruits.add(bellPepper);
		kidneyBean = new KidneyBean(sprites.getKidneyBeanSprite(), LIFE_TIME, KIDNEY_BEAN_DURATION);
		fruits.add(kidneyBean);
	}

	public Fruit getRandomFruit() {
		return fruits.get(rand.nextInt(fruits.size()));
	}
	
	public Fruit getFish() {
		return fish;
	}
	
	public Fruit getPotato() {
		return potato;
	}
	
	public Fruit getTomato() {
		return tomato;
	}
	
	public Fruit getPomgranate() {
		return pomgranate;
	}
	
	public Fruit getBellPepper() {
		return bellPepper;
	}
	
	public Fruit getKidneyBean() {
		return kidneyBean;
	}
	
    public Square getRandomFruitPosition() {
    	return fruitPositions.get(rand.nextInt(fruitPositions.size()));
	}
}
