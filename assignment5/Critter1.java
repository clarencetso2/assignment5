package assignment5;

/*
 * Critter that moves in a random direction each timestep
 */
import java.util.*;
public class Critter1 extends Critter {
	int direction = 0;
	public String toString() { 
		return "1"; 
	}
	
	
	public void doTimeStep() {
		look(direction, false);
		walk(direction);
		direction = getRandomInt(8);
	}
	

	@Override
	public boolean fight(String opponent) {
		if (getEnergy() > 0) 
			return true;
		
		return false;
	}


	@Override
	public CritterShape viewShape() {
		// TODO Auto-generated method stub
		return CritterShape.TRIANGLE;
		
	}
	public javafx.scene.paint.Color viewColor() { 
		return javafx.scene.paint.Color.BLACK; 
	}
	

}
