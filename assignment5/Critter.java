

package assignment5;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import java.lang.reflect.InvocationTargetException;
import java.util.List;


public abstract class Critter {
	/* NEW FOR PROJECT 5 */
	public enum CritterShape {
		CIRCLE,
		SQUARE,
		TRIANGLE,
		DIAMOND,
		STAR
	}
	
	/* the default color is white, which I hope makes critters invisible by default
	 * If you change the background color of your View component, then update the default
	 * color to be the same as you background 
	 * 
	 * critters must override at least one of the following three methods, it is not 
	 * proper for critters to remain invisible in the view
	 * 
	 * If a critter only overrides the outline color, then it will look like a non-filled 
	 * shape, at least, that's the intent. You can edit these default methods however you 
	 * need to, but please preserve that intent as you implement them. 
	 */
	public javafx.scene.paint.Color viewColor() { 
		return javafx.scene.paint.Color.WHITE; 
	}
	
	public javafx.scene.paint.Color viewOutlineColor() { return viewColor(); }
	public javafx.scene.paint.Color viewFillColor() { return viewColor(); }
	
	public abstract CritterShape viewShape(); 
	
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();
	private static Critter[][] critterGrid = new Critter [Params.world_height][Params.world_width];
	private boolean moved = false; //checks if critter has already moved in timeStep
	private boolean timeStep = false; // checks whether walk/run is called in timeStep or fight (false if in timeStep)
	
	int oldx;
	int oldy;
		
	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	protected final String look(int direction, boolean steps) {
		int lookx = this.x_coord;
		int looky = this.y_coord;
		int numSteps;
		if(steps == false){
			numSteps = 1;
		}
		else{
			numSteps = 2;
		}
		
		if(direction==0 || direction==1 || direction==7){  // right directions
			lookx += numSteps;
		}
		if(direction == 1 || direction == 2 || direction == 3){	// up directions
		
			looky += numSteps;
		}
		
		if(direction== 3 || direction== 4 || direction== 5){  // left directions
			lookx -= numSteps;
		}
		
		if(direction == 5 || direction == 6 || direction == 7){	// down directions
			
			looky -= numSteps;
		}
		
		if(lookx<0){  					//keep neg numbers on board
			lookx+=Params.world_width;
		}
		if(looky<0){
			looky+=Params.world_height;	//keep neg numbers on board
		}
		lookx = lookx %Params.world_width;	// keeps critter on the board 
		looky = looky %Params.world_height;
		
		energy-=Params.look_energy_cost;

		if(occupied(lookx,looky) == false){
			return null;
		}
		else{
			if(timeStep == false){
				for(Critter c: population){
					if(c.energy > 0 && c.x_coord==lookx && c.y_coord==looky){
						return c.toString();
					}
				}
			}
			
			else{
				for(Critter c: population){
					if(c.energy > 0 && c.oldx == lookx && c.oldy == looky){
						return c.toString();
					}
				}
			}
		}
		return null;
		
	
	}
	
	/* rest is unchanged from Project 4 */
	
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;
	
	/**
	 * If called in doTimeStep() in Critter: Critter moves a space in a direction
	 * If called in fight() in Critter: attempts to move Critter a space in specified direction(if space open)
	 * @param direction critter will move (8 directions)
	 */
	protected final void walk(int direction) {
		//if it hasnt moved and is doing timeStep
		if(moved == false && timeStep == false){
			executeMove(1,direction);
			moved = true;
		}
		//if in fight and hasnt moved
		else if(moved == false){
			if(unOccupied(1,direction)){
				executeMove(1,direction);
				moved = true;
			}
			
		}
		energy-=Params.walk_energy_cost;

		
	}
	
	
	/**
	 * If called in doTimeStep() in Critter: Critter moves a space in a direction
	 * If called in fight() in Critter: attempts to move Critter a space in specified direction(if space open)
	 * @param direction critter will move (8 directions)
	 */
	protected final void run(int direction) {
		//if it hasnt moved and is doing timeStep
				if(moved == false && timeStep == false){
					executeMove(2,direction);
					moved = true;
				}
				//if in fight and hasnt moved
				else if(moved == false){
					if(unOccupied(2,direction)){
						executeMove(2,direction);
						moved = true;
					}
					
				}
				energy-=Params.run_energy_cost;
	}
	
	
	
	
	/**
	 * checks if critter can move into empty spot
	 * @param numSteps Number of steps to take in direction
	 * @param direction of movement
	 * @return True if the spot not occupied
	 */
	private boolean unOccupied(int numSteps,int direction){
		int xTemp= x_coord;
		int yTemp= y_coord;
		if(direction==0 || direction==1 || direction==7){  // right directions
			xTemp += numSteps;
		}
		if(direction == 1 || direction == 2 || direction == 3){	// up directions
		
			yTemp += numSteps;
		}
		
		if(direction== 3 || direction== 4 || direction== 5){  // left directions
			xTemp -= numSteps;
		}
		
		if(direction == 5 || direction == 6 || direction == 7){	// down directions
			
			yTemp -= numSteps;
		}
		
		if(xTemp<0){  					//keep neg numbers on board
			xTemp+=Params.world_width;
		}
		if(yTemp<0){
			yTemp+=Params.world_height;	//keep neg numbers on board
		}
		x_coord=x_coord%Params.world_width;	// keeps critter on the board 
		y_coord=y_coord%Params.world_height;
		
		if(occupied(xTemp,yTemp) == false){
			return true;
		}
		else 
			return false;
	}
	
	
	/**
	 * Moves critter numsteps in a direction and wraps if needed
	 * @param numSteps The number of steps to move the Critter.
	 * @param direction The direction to move the Critter.
	 */
	private final void executeMove(int numSteps, int direction){
		if(direction==0 || direction==1 || direction==7){  // right directions
			x_coord += numSteps;
		}
		if(direction == 1 || direction == 2 || direction == 3){	// up directions
		
			y_coord += numSteps;
		}
		
		if(direction== 3 || direction== 4 || direction== 5){  // left directions
			x_coord -= numSteps;
		}
		
		if(direction == 5 || direction == 6 || direction == 7){	// down directions
			
			y_coord -= numSteps;
		}
		
		if(x_coord<0){  					//check for neg and places onto board
			x_coord+=Params.world_width;
		}
		if(y_coord<0){
			y_coord+=Params.world_height;	//check for neg
		}
		x_coord=x_coord%Params.world_width;	// keeps critter on the board 
		y_coord=y_coord%Params.world_height;
	}
	
	/**
	 * Checks if the critter has enough energy to reproduce and create an offspring, and placed the offspring on the board if it does have enough energy.
	 * @param offspring The offspring critter that is to be added if the parent has enough energy
	 * @param direction The direction in which the offspring of the parent will be placed
	 */
	protected final void reproduce(Critter offspring, int direction) {
		//not enough energy to reproduce
		if(this.energy < Params.min_reproduce_energy){
			return;
		}
		
		else{
			offspring.x_coord = this.x_coord;
			offspring.y_coord = this.y_coord;
			offspring.executeMove(1, direction);
			offspring.moved = false;
			offspring.energy = this.energy/2;
			this.energy = (int) Math.ceil(((double) (this.energy))/2);
			babies.add(offspring);
		}
	}

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		
		Class<?> myCritter = null;
		Constructor<?> constructor = null;
		Object instanceOfMyCritter = null;

		try {
			myCritter = Class.forName(myPackage + "." + critter_class_name); 	// Class object of specified name
		} catch (ClassNotFoundException e) {
			throw new InvalidCritterException(critter_class_name);
		}
		catch (NoClassDefFoundError e) {
			throw new InvalidCritterException(critter_class_name);
		}
		
		
		
		try {
			constructor = myCritter.getConstructor();		// No-parameter constructor object
			instanceOfMyCritter = constructor.newInstance();	// Create new object using constructor
		} catch ( InstantiationException e) {
			// Do whatever is needed to handle the various exceptions here -- e.g. rethrow Exception
			throw new InvalidCritterException(critter_class_name);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			throw new InvalidCritterException(critter_class_name);

		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			throw new InvalidCritterException(critter_class_name);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new InvalidCritterException(critter_class_name);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			throw new InvalidCritterException(critter_class_name);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			throw new InvalidCritterException(critter_class_name);
		}
		
		
		
		
		
		Critter me = (Critter)instanceOfMyCritter;		// Cast to Critter
		me.energy = Params.start_energy;
		me.x_coord = getRandomInt(Params.world_width);
		me.y_coord= getRandomInt(Params.world_height);
		
		population.add(me);
	}
	
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
		Class<?> myCritter = null;
		Constructor<?> constructor = null;
		Object instanceOfMyCritter = null;
		
		try {
			myCritter = Class.forName(myPackage + "." + critter_class_name); 	// Class object of specified name
		} catch (ClassNotFoundException e) {
			throw new InvalidCritterException(critter_class_name);
		}
		catch (NoClassDefFoundError e) {
			throw new InvalidCritterException(critter_class_name);
		}
		
		try {
			constructor = myCritter.getConstructor();		// No-parameter constructor object
			instanceOfMyCritter = constructor.newInstance();	// Create new object using constructor
		} catch ( InstantiationException e) {
			// Do whatever is needed to handle the various exceptions here -- e.g. rethrow Exception
			throw new InvalidCritterException(critter_class_name);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			throw new InvalidCritterException(critter_class_name);

		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			throw new InvalidCritterException(critter_class_name);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new InvalidCritterException(critter_class_name);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			throw new InvalidCritterException(critter_class_name);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			throw new InvalidCritterException(critter_class_name);
		}
		
		Critter me = (Critter)instanceOfMyCritter;		// Cast to Critter
		
		for (Critter crit : population) {
			if (me.getClass().isInstance(crit)) {
				result.add(crit);
			}
		}
		return result;
	}
	
	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static String runStats(List<Critter> critters) {
		String result = "";
		System.out.print("" + critters.size() + " critters as follows -- ");
		result = result+"" + critters.size() + " critters as follows -- ";

		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			result=result+prefix + s + ":" + critter_count.get(s);
			prefix = ", ";
		}
		System.out.println();		
		result = result+"\n";
		return result;
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		population.clear();
		babies.clear();
		critterGrid=new Critter[Params.world_height][Params.world_width];
	}
	
	public static void worldTimeStep() {
		//set old x and y for look
		setOldxy();
		// Do timestep for all critters
		doTimeStepAllCritters();
		
		
		//resolve encounter
		resolveEncounters();
		
		//update energy after rest
		updateEnergy();
		
		//remove dead critters (energy < 0)
		removeDead();
		
		// Add algae
		makeAlgae();
		
		//add new baby critters
		addCritters();
		
		
		
	}
	
/**
	 * Displays the current game world.
	 */
	public static void displayWorld() {
		
    	double gridLineWidth = Math.min(Main.screenWidth/Params.world_width*1.0, Main.screenHeight/Params.world_height*1.0)/Math.min(Params.world_width, Params.world_height);
    	
    	double widthBetween = ((Main.screenWidth - (gridLineWidth*1.0))/(Params.world_width));
    	double heightBetween = ((Main.screenHeight - (gridLineWidth*1.0))/(Params.world_height));
    	double spaceBetween = Math.min(widthBetween, heightBetween);
    	
    	for(int i = 0; i < Params.world_height; i++) {
    		for(int j = 0; j < Params.world_width; j++) {
    			critterGrid[i][j] = null;
    		}
    	}
		
		for( Critter next:population ) {
			if(critterGrid[next.y_coord][next.x_coord] == null) {
				critterGrid[next.y_coord][next.x_coord] = next;
			}
		}
		
		
		//clear the grid of critters
		for(int i = 0; i < Params.world_height; i++) {
			for(int j = 0; j < Params.world_width; j++) {
				Main.gridGraphicsContext.clearRect(gridLineWidth + i*spaceBetween, gridLineWidth + j*spaceBetween, spaceBetween - gridLineWidth, spaceBetween - gridLineWidth);
				Main.gridGraphicsContext.setFill(Color.SKYBLUE);
				Main.gridGraphicsContext.fillRect(gridLineWidth + i*spaceBetween, gridLineWidth + j*spaceBetween, spaceBetween - gridLineWidth, spaceBetween - gridLineWidth);
			}
		}
    	Main.gridGraphicsContext.setFill(Color.SKYBLUE);
    	Main.gridGraphicsContext.fillRect(0,0,1280,720);
		Main.gridGraphicsContext.setFill(Color.BLACK);
    	for(int i=0;i<Params.world_height+1;i++){
    		Main.gridGraphicsContext.fillRect(i*spaceBetween,0,gridLineWidth,(Params.world_width)*spaceBetween+gridLineWidth);
    	}
    	for(int i=0;i<Params.world_width+1;i++){
    		Main.gridGraphicsContext.fillRect(0,i*spaceBetween,(Params.world_height)*spaceBetween,gridLineWidth);
    	}
		
		//populate the grid with critters
		for(int i = 0; i < Params.world_height; i++) {
			for(int j = 0; j < Params.world_width; j++) {
				if(critterGrid[i][j] != null) {
					int critShape = 0;
					for (int k = 0; k <= 4; k++) {
						if(critterGrid[i][j].viewShape().equals(CritterShape.values()[k])) {
							critShape = k;
						}
					}
					
					javafx.scene.paint.Color fillColor = critterGrid[i][j].viewFillColor();
					javafx.scene.paint.Color outlineColor = critterGrid[i][j].viewOutlineColor();
					Main.gridGraphicsContext.setFill(fillColor);
					Main.gridGraphicsContext.setStroke(outlineColor);
					switch(critShape) {
					case 0: 
							Main.gridGraphicsContext.fillOval(gridLineWidth + i*spaceBetween + spaceBetween*0.3, gridLineWidth + j*spaceBetween + spaceBetween*0.3, spaceBetween*0.4, spaceBetween*0.4);
							Main.gridGraphicsContext.strokeOval(gridLineWidth + i*spaceBetween + spaceBetween*0.3, gridLineWidth + j*spaceBetween + spaceBetween*0.3, spaceBetween*0.4, spaceBetween*0.4);
							break;
					
					case 1: 
						
							Main.gridGraphicsContext.fillRect(gridLineWidth + i*spaceBetween + spaceBetween*0.3, gridLineWidth + j*spaceBetween + spaceBetween*0.3, spaceBetween*0.4, spaceBetween*0.4);
							Main.gridGraphicsContext.strokeRect(gridLineWidth + i*spaceBetween + spaceBetween*0.3, gridLineWidth + j*spaceBetween + spaceBetween*0.3, spaceBetween*0.4, spaceBetween*0.4);
							break;

					case 2: 							
							
							Main.gridGraphicsContext.fillPolygon(new double[]{gridLineWidth + i*spaceBetween + spaceBetween*0.5, gridLineWidth + i*spaceBetween + spaceBetween*0.3, gridLineWidth + i*spaceBetween + spaceBetween*0.7},
																 new double[]{gridLineWidth + j*spaceBetween + spaceBetween*0.3, gridLineWidth + j*spaceBetween + spaceBetween*0.7, gridLineWidth + j*spaceBetween + spaceBetween*0.7},
																 3);
							Main.gridGraphicsContext.strokePolygon(new double[]{gridLineWidth + i*spaceBetween + spaceBetween*0.5, gridLineWidth + i*spaceBetween + spaceBetween*0.3, gridLineWidth + i*spaceBetween + spaceBetween*0.7},
																   new double[]{gridLineWidth + j*spaceBetween + spaceBetween*0.3, gridLineWidth + j*spaceBetween + spaceBetween*0.7, gridLineWidth + j*spaceBetween + spaceBetween*0.7},
																   3);
							break;

					case 3: 
						
							Main.gridGraphicsContext.fillPolygon(new double[]{gridLineWidth + i*spaceBetween + spaceBetween*0.5, gridLineWidth + i*spaceBetween + spaceBetween*0.3, gridLineWidth + i*spaceBetween + spaceBetween*0.5, gridLineWidth + i*spaceBetween + spaceBetween*0.7},
																 new double[]{gridLineWidth + j*spaceBetween + spaceBetween*0.3, gridLineWidth + j*spaceBetween + spaceBetween*0.5, gridLineWidth + j*spaceBetween + spaceBetween*0.7, gridLineWidth + j*spaceBetween + spaceBetween*0.5},
																 4);
							Main.gridGraphicsContext.strokePolygon(new double[]{gridLineWidth + i*spaceBetween + spaceBetween*0.5, gridLineWidth + i*spaceBetween + spaceBetween*0.3, gridLineWidth + i*spaceBetween + spaceBetween*0.5, gridLineWidth + i*spaceBetween + spaceBetween*0.7},
																   new double[]{gridLineWidth + j*spaceBetween + spaceBetween*0.3, gridLineWidth + j*spaceBetween + spaceBetween*0.5, gridLineWidth + j*spaceBetween + spaceBetween*0.7, gridLineWidth + j*spaceBetween + spaceBetween*0.5},
																   4);				
							break;
					case 4: 
						
							Main.gridGraphicsContext.fillPolygon(new double[]{gridLineWidth + i*spaceBetween + spaceBetween*0.5, gridLineWidth + i*spaceBetween + spaceBetween*0.45, gridLineWidth + i*spaceBetween + spaceBetween*0.3, gridLineWidth + i*spaceBetween + spaceBetween*0.42, gridLineWidth + i*spaceBetween + spaceBetween*0.37, gridLineWidth + i*spaceBetween + spaceBetween*0.5, gridLineWidth + i*spaceBetween + spaceBetween*0.63, gridLineWidth + i*spaceBetween + spaceBetween*0.58, gridLineWidth + i*spaceBetween + spaceBetween*0.7, gridLineWidth + i*spaceBetween + spaceBetween*0.55},
																 new double[]{gridLineWidth + j*spaceBetween + spaceBetween*0.3,gridLineWidth + j*spaceBetween + spaceBetween*0.46, gridLineWidth + j*spaceBetween + spaceBetween*0.46, gridLineWidth + j*spaceBetween + spaceBetween*0.54, gridLineWidth + j*spaceBetween + spaceBetween*0.7, gridLineWidth + j*spaceBetween + spaceBetween*0.62, gridLineWidth + j*spaceBetween + spaceBetween*0.7, gridLineWidth + j*spaceBetween + spaceBetween*0.54, gridLineWidth + j*spaceBetween + spaceBetween*0.46, gridLineWidth + j*spaceBetween + spaceBetween*0.46},
																 10);
							Main.gridGraphicsContext.strokePolygon(new double[]{gridLineWidth + i*spaceBetween + spaceBetween*0.5, gridLineWidth + i*spaceBetween + spaceBetween*0.45, gridLineWidth + i*spaceBetween + spaceBetween*0.3, gridLineWidth + i*spaceBetween + spaceBetween*0.42, gridLineWidth + i*spaceBetween + spaceBetween*0.37, gridLineWidth + i*spaceBetween + spaceBetween*0.5, gridLineWidth + i*spaceBetween + spaceBetween*0.63, gridLineWidth + i*spaceBetween + spaceBetween*0.58, gridLineWidth + i*spaceBetween + spaceBetween*0.7, gridLineWidth + i*spaceBetween + spaceBetween*0.55},
																   new double[]{gridLineWidth + j*spaceBetween + spaceBetween*0.3,gridLineWidth + j*spaceBetween + spaceBetween*0.46, gridLineWidth + j*spaceBetween + spaceBetween*0.46, gridLineWidth + j*spaceBetween + spaceBetween*0.54, gridLineWidth + j*spaceBetween + spaceBetween*0.7, gridLineWidth + j*spaceBetween + spaceBetween*0.62, gridLineWidth + j*spaceBetween + spaceBetween*0.7, gridLineWidth + j*spaceBetween + spaceBetween*0.54, gridLineWidth + j*spaceBetween + spaceBetween*0.46, gridLineWidth + j*spaceBetween + spaceBetween*0.46},
																   10);		

							break;
					}
					
				}
			}
		}
		
		/*
		System.out.print("+");
		for(int i = 0; i < Params.world_width; i++) {
			System.out.print("-");
		}
		System.out.println("+");
		
		for(int i = 0; i < Params.world_height; i++) {
			System.out.print("|");
			for(int j = 0; j < Params.world_width; j++) {
				if(critterGrid[i][j] != null) {
					System.out.print(critterGrid[i][j].toString());
				}
				else {
					System.out.print(" ");
				}
			}
			System.out.println("|");
		}
		
		System.out.print("+");
		for(int i = 0; i < Params.world_width; i++) {
			System.out.print("-");
		}
		System.out.println("+");
		*/
		
		
	}
	
	static Shape getIcon(int shapeIndex, int size, javafx.scene.paint.Color outlineColor, javafx.scene.paint.Color fillColor) {
		Shape s = null;
		
		switch(shapeIndex) {
			case 0: s = new Circle(size/2); 
			s.setFill(fillColor); 
			break;
			case 1: s = new Rectangle(size, size); 
				s.setFill(fillColor); 
				break;
			case 2: s = new Polygon();				//triangle
				((Polygon)s).getPoints().addAll(new Double[]{
					(double) (size/2), 0.0,
					0.0, (double) size,
					(double) size, (double) size
				});
				s.setFill(fillColor);
				break;
			case 3: s = new Polygon();				//diamond
				((Polygon)s).getPoints().addAll(new Double[]{
					(double) (size/2), 0.0,
					0.0, (double) (size/2),
					(double) (size/2), (double) size,
					(double) size, (double) (size/2)
				});
				s.setFill(fillColor);
				break;
			case 4: s = new Polygon();				//star
				((Polygon)s).getPoints().addAll(new Double[]{
					(double) (size/2), 0.0,
					(double) (size/3), (double) (size/3),
					0.0, (double) (size/2),
					(double) (size/3), (double) (2*size/3),
					0.0, (double) (size),
					(double) (size/2), (double) (5*size/6),
					(double) (size), (double) (size),
					(double) (2*size/3), (double) (2*size/3),
					(double) (size), (double) (size/2),
					(double) (2*size/3), (double) (size/3)
				});
				s.setFill(fillColor);
				break;
		}
		// set the outline of the shape
		s.setStroke(outlineColor); // outline
		return s;
	}
	
	/**
	 * Update energy of each Critter and populates a 2d grid array
	 */
	private static void updateEnergy(){
			//Update rest energy and grid
		for(Critter c:population){
			c.energy=c.energy-Params.rest_energy_cost; // make sure to do b4 dead
		}
	}
	/**
	 * Generates algae and adds them to population
	 */
	private static void makeAlgae(){
		for(int i = 0; i < Params.refresh_algae_count; i++){
			Critter algae = new Algae();
			algae.energy = Params.start_energy;
			algae.x_coord = getRandomInt(Params.world_width);
			algae.y_coord = getRandomInt(Params.world_height);
			population.add(algae);

		}
	}
	
	
	/**
	 * adds new critters into population array
	 */
	private static void addCritters(){
		for(int i = 0; i < babies.size(); i++){
			population.add(babies.get(i));
		}
	}
	
	/**
	 * Remove dead critters from population
	 */
	private static void removeDead(){
		//Remove dead critters from populaiton list
		for(int i = 0; i < population.size(); i++){
			if(population.get(i).energy <= 0){
				population.remove(i);
				i--;   //shifts back everything in list so i
			}   
		}
		

	}
	
	/**
	 * Executes the doTimeStep() function for each Critter
	 */
	private static void doTimeStepAllCritters(){
		//Do time step for each critter
		for(Critter c:population){
			c.moved=false; 
			c.timeStep=true; 
			c.doTimeStep();
			c.timeStep=false; 
		}
	}
	
	
	/**
	 * Resolves encounters between critters
	 */
	private static void resolveEncounters(){
		int diceA, diceB;
		Critter temp1, temp2;
		boolean fightAB, fightBA;
		
		//check for encounters on all critters in population
		for (int i = 0; i < population.size(); i++){
			temp1=population.get(i);
			temp1.timeStep = false;
			
			
			for(int j = i + 1; j < population.size(); j++){
				
				temp2 = population.get(j);
				temp2.timeStep = false;
				
	
				//if not at same location, cant fight
				if(sameLocation(temp1,temp2) == false){
					continue;
				}
				
				//If temp1 energy about to die, it cant fight
				if(temp1.energy <= 0){
					break;
				}
				//If temp2 about to die, temp1 doesnt need to fight it
				if(temp2.energy <= 0){
					continue;
				}
				
				//check if want to fight
				fightAB=temp1.fight(temp2.toString());
				fightBA=temp2.fight(temp1.toString());
				diceA = 0; diceB = 0;
				
				//check if at same location and both alive
				if(temp1.getEnergy() > 0 && temp2.getEnergy() > 0 && sameLocation(temp1, temp2) == true){
					
					if(fightAB){
						diceA = getRandomInt(temp1.getEnergy());
					}
					
					if(fightBA)
					{
						diceB = getRandomInt(temp2.getEnergy());
					}
					
					
					if(diceA >= diceB){
						temp1.energy = temp1.energy + temp2.energy/2;
						temp2.energy = 0;
					}
					
					else{
						temp2.energy = temp2.energy + temp1.energy/2;
						temp1.energy = 0;
					}				
				}
			}
		
		}
	}
	
	
	
	
	/**
	 * Checks if  critters are in the same location 
	 * @param a first Critter
	 * @param b second Critter
	 * @return True if critters are in  same location
	 */
	private static boolean sameLocation(Critter a,Critter b){
		if(a.x_coord==b.x_coord && b.y_coord==a.y_coord){
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if the current critter's location is occupied
	 * @param a Critter
	 * @return True if the space the critter is in is occupied
	 */
	private static boolean occupied(Critter a){
		for(Critter c:population){
			if(c.energy>0 && sameLocation(a,c)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if the current x and y coordinate are free
	 * @param x X coordinate or column to check
	 * @param y Y coordinate or row to check
	 * @return True if the (x,y) coordinate is occupied
	 */
	private static boolean occupied(int x, int y){
		for(Critter c:population){
			if(c.energy>0 && c.x_coord==x && c.y_coord==y){
				return true;
			}
		}
		
		return false;
	}
	
	
	public static void setOldxy(){
		for(Critter c:population){
			c.oldx=c.x_coord;
			c.oldy=c.y_coord;
		}
	}
}
