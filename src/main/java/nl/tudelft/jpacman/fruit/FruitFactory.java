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
	private final int POMEGRANATE_DURATION = 0;
	//private final int BELL_PEPPER_DURATION = 0;
	private final int TOMATO_DURATION = 3;
	//private final int KIDNEY_BEAN_DURATION = 0;
	private final int POTATO_DURATION = 1;
	private final int FISH_DURATION = 1;

	private final PacManSprites sprites;
	private List<Fruit> fruits = new ArrayList<>();
	private List<Square> fruitPositions;
	private Random rand = new Random();
	
	public FruitFactory(PacManSprites spriteStore, List<Square> fruitPos, List<NPC> npcs) {
		this.sprites = spriteStore;
		fruitPositions = fruitPos;
		fruits.add(new Fish(sprites.getFishSprite(), LIFE_TIME, FISH_DURATION));
		fruits.add(new Potato(sprites.getFishSprite(), LIFE_TIME, POTATO_DURATION, npcs));
		fruits.add(new Tomato(sprites.getTomatoSprite(), LIFE_TIME, TOMATO_DURATION));
		fruits.add(new Pomgranate(sprites.getPomegranateSprite(), LIFE_TIME, POMEGRANATE_DURATION, npcs));
	}

	public Fruit getRandomFruit() {
		return fruits.get(rand.nextInt(fruits.size()));
	}
	
    public Square getRandomFruitPosition() {
    	return fruitPositions.get(rand.nextInt(fruitPositions.size()));
	}
}
