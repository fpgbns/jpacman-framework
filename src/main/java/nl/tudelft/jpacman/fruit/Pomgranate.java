package nl.tudelft.jpacman.fruit;

import java.util.List;

import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.npc.NPC;
import nl.tudelft.jpacman.npc.ghost.Ghost;
import nl.tudelft.jpacman.npc.ghost.Navigation;
import nl.tudelft.jpacman.sprite.Sprite;

public class Pomgranate extends Fruit {
	
	List<NPC> npcs;
	
	protected Pomgranate(Sprite sprite, int lifetime, int effectDuration, List<NPC> npcs) {
		super(sprite, lifetime, effectDuration);
		this.npcs = npcs;
	}

	@Override
	public void fruitEffect(Player p) {
		Ghost g;
		for(NPC npc: npcs){
			if(npc instanceof Ghost){
				g = (Ghost) npc;
				if(Navigation.shortestPath(p.getSquare(), g.getSquare(), this).size() <= 4){
					g.setExplode(true);
				}
			}
		}
	}
}
