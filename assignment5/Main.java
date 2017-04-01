package assignment5;


import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Painter;

import assignment5.Critter.CritterShape;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;

public class Main extends Application {
	
	
	
	class ResizeCanvas extends Canvas {
		
		public ResizeCanvas() {
			widthProperty().addListener(evt -> redraw());
			heightProperty().addListener(evt -> redraw());
		}
		
		public ResizeCanvas(double width, double height) {
			//setWidth(width);
			//setHeight(height);
			super(width, height);
			widthProperty().addListener(evt -> redraw());
			heightProperty().addListener(evt -> redraw());
		}
		
		private void redraw() {
			Main.screenHeight = getHeight();
			Main.screenWidth = getWidth();
			Critter.displayWorld();
		}
		
		public double prefWidth(double height) {
			return getWidth();
		}
		
		public double prefHeight(double width) {
			return getHeight();
		}
		
		
		public boolean isResizable() {
			return true;
		}
	}
	
	
	
	
	
	
    //Variables created for drawing grid
    public static ResizeCanvas gridCanvas=null;//Main canvas which the world is displayed
    public static GraphicsContext gridGraphicsContext=null;
    public static int gridRows=10;//Initial rows and columns
    public static int gridCols=10;
    public static double gridLineWidth=10;
    public static int minSize = Math.min(Params.world_width, Params.world_height);
    public static double screenHeight=Math.max(100, Math.min(Params.world_height*25, 1200));
    public static double screenWidth=Math.max(100, Math.min(Params.world_width*25, 1200));
    public static String myPackage = Critter.class.getPackage().toString().split(" ")[1];
    
    static GridPane animate;
    static Button animateButton; //so animations can access that button
    static Button animateStopButton;
    static boolean animating = false;
    static Timer timer;
    static TimerTask animateGo;
    //run stats
    public static ComboBox<String> statsType;
    public static Label statsLabel;
    
    static ScrollPane scrollpane = new ScrollPane();

    
    // controls to disable
    static Button statsTypeButton;
    static ComboBox<String> speedComboBox;
    static Button quitButton;
    static ComboBox<String> critterTypeComboBox;
	static GridPane grid = new GridPane();
	static Button makeCritterButton;
	static TextField critterQuantityTextField;
	static Button timeStepButton;
	static TextField numStepsTextField;
	static TextField seedNumberTextField;
	static Button setSeedButton;

	//Shape final constants to draw normalized to 1 by 1 square
    private final static double[][] triangle={{0.50,0.06},{0.06,0.78},{0.94,0.78}};
    private final static double[][] square={{0.12,0.12},{0.12,0.88},{0.88,0.88},{0.88,0.12}};
    private final static double[][] diamond={{0.50,0.06},{0.25,0.50},{0.50,0.94},{0.75,0.50}};
    private final static double[][] star={{0.20,0.95},{0.50,0.75},{0.80,0.95},{0.68,0.60},{0.95,0.40},{0.62,0.38},{0.50,0.05},{0.38,0.38},{0.05,0.40},{0.32,0.64}};
  //Oval/circle draw scaling to fit in center of circle
    private final static double scalingCircle=0.5;
	
	
	MediaPlayer mediaPlayer;
	@Override
	public void start(Stage primaryStage) {
		try {			
			Stage menuStage=new Stage();
	    	menuStage.setTitle("menu");	
			BorderPane pane = new BorderPane();
			URL musicFile = getClass().getResource("LouisArmstrong.mp3");   
			Media sound = new Media(musicFile.toString());
			 mediaPlayer = new MediaPlayer(sound);
			
			String image = getClass().getResource("Critters.jpg").toExternalForm();
			pane.setStyle("-fx-background-image: url('" + image + "'); " +
			           "-fx-background-position: center center; " +
			           "-fx-background-repeat: stretch;");			
	      	Button start = new Button("Start");
			start.setMaxWidth(200);
			start.setMaxHeight(50);
			pane.setCenter(start);
	        Scene menuScene = new Scene(pane, 1200, 720);
			menuStage.setScene(menuScene);
			mediaPlayer.play();
	        menuStage.show();
			
	        start.setOnAction(new EventHandler<ActionEvent>() {
				 
	            public void handle(ActionEvent event) {
	            	mediaPlayer.stop();
	            	menuStage.hide();
	            	           	
	            	//////START ACTUAL CODE HERE////////
	            	Stage controllerStage=new Stage();
	            	controllerStage.setTitle("Critter Controller");
	            	GridPane controllerGridPane= new  GridPane();

	            	controllerGridPane.setHgap(10);
	            	controllerGridPane.setVgap(10);
	            	controllerGridPane.setPadding(new Insets(0, 10, 0, 10));
	                
	            	addMakeCritterGridPane(controllerGridPane);
	            	addTimeStepGridPane(controllerGridPane);
	            	addStatsGridPane(controllerGridPane);
	            	addSeedGridPane(controllerGridPane);
	            	addAnimationGridPane(controllerGridPane);
	            	
	            	// 	ADD Quit button
	            	GridPane quitButtonGridPane=new GridPane();
	                quitButtonGridPane.setHgap(10);
	                quitButtonGridPane.setVgap(10);
	                quitButtonGridPane.setPadding(new Insets(10, 2, 10, 2));
	                
	                quitButton = new Button();
	                quitButton.setText("Quit");
	                quitButton.setOnAction(e->System.exit(0));
	                quitButtonGridPane.add(quitButton, 0, 0);
	                
	               	               
	                
	                controllerGridPane.add(quitButtonGridPane, 0, 5);
	            	
	            	
	            	controllerStage.setScene(new Scene(controllerGridPane));
	                controllerStage.setOnHiding(e->System.exit(0));
	            	
	                controllerStage.show();
	            	
	            	
	            	primaryStage.setTitle("Critter Display");
	    	    	Group root=new Group();
	    	    	gridCanvas=new ResizeCanvas(screenWidth,screenHeight);
	    	    	gridGraphicsContext=gridCanvas.getGraphicsContext2D();
	    	    	/*
	    	    	scrollpane.setContent(gridCanvas);
	    	    	scrollpane.setFitToHeight(true);
	    	    	scrollpane.setFitToWidth(true);
	    	    	scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
	    	    	scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
	    	    	scrollpane.setPrefViewportWidth(Math.min(screenWidth, 1200));
	    	    	scrollpane.setPrefViewportHeight(Math.min(screenHeight, 600));
	    	    	//root.getChildren().add(gridCanvas);
	    	    	 *
	    	    	 */
	    	    	grid.getChildren().add(gridCanvas);
	    	    	gridCanvas.widthProperty().bind(grid.widthProperty());
	    	    	gridCanvas.heightProperty().bind(grid.heightProperty());
	    	    	primaryStage.setScene(new Scene(grid));

	    	    	drawGrid(null);	    			
	    			grid.setGridLinesVisible(true);
	            	
	    			primaryStage.show();
	            	
	            }
	        });
			
	        
	        
			
			
			
			// Paints the icons.
	//		Painter.paint(null, scene, 0, 0);
			
		} catch(Exception e) {
			e.printStackTrace();		
		}
	}
	

	
    public static void drawGrid(Critter[][] grid){
    	
    	gridGraphicsContext.setFill(Color.SKYBLUE);
    	gridGraphicsContext.fillRect(0,0,screenWidth,screenHeight);
    	gridGraphicsContext.setFill(Color.BLACK);
    	gridLineWidth = Math.min(screenWidth/Params.world_width*1.0, screenHeight/Params.world_height*1.0)/Math.min(Params.world_width, Params.world_height);
    	
    	double widthBetween = ((screenWidth - (gridLineWidth*1.0))/(Params.world_width));
    	double heightBetween = ((screenHeight - (gridLineWidth*1.0))/(Params.world_height));
    	double spaceBetween = Math.min(widthBetween, heightBetween);
    	gridLineWidth = 0.1*spaceBetween;
    	for(int i=0;i<Params.world_width+1;i++){
    		gridGraphicsContext.fillRect(i*spaceBetween,0,gridLineWidth,(Params.world_height)*spaceBetween+gridLineWidth);
    	}
    	for(int i=0;i<Params.world_height+1;i++){
    		gridGraphicsContext.fillRect(0,i*spaceBetween,(Params.world_width)*spaceBetween,gridLineWidth);
    	}
    	
    	
    	
    	if(grid==null){
    		return;
    	}
    	
    	drawCritters(grid,widthBetween,heightBetween);
    	//statisticsEventHandler(statisticsComboBox.getValue());

    }
    
    protected static void drawCritters(Critter[][] grid,double widthBetweenLines, double heightBetweenLines){
    	for(int i=0;i<grid.length;i++){
    		for(int j=0;j<grid[0].length;j++){
    			if(grid[i][j]==null){
    				continue;
    			}
    			CritterShape val=grid[i][j].viewShape();
    			gridGraphicsContext.setFill(grid[i][j].viewFillColor());
    			gridGraphicsContext.setStroke(grid[i][j].viewOutlineColor());
    			gridGraphicsContext.setLineWidth(gridLineWidth/2);
    			if(val==CritterShape.CIRCLE){
    				gridGraphicsContext.strokeOval(j*widthBetweenLines+gridLineWidth+scalingCircle*(widthBetweenLines-gridLineWidth)/2.0,i*heightBetweenLines+gridLineWidth+scalingCircle*(heightBetweenLines-gridLineWidth)/2.0,(widthBetweenLines-gridLineWidth)*scalingCircle,(heightBetweenLines-gridLineWidth)*scalingCircle);
    				gridGraphicsContext.fillOval(j*widthBetweenLines+gridLineWidth+scalingCircle*(widthBetweenLines-gridLineWidth)/2.0,i*heightBetweenLines+gridLineWidth+scalingCircle*(heightBetweenLines-gridLineWidth)/2.0,(widthBetweenLines-gridLineWidth)*scalingCircle,(heightBetweenLines-gridLineWidth)*scalingCircle);
    			}
    			else if(val==CritterShape.DIAMOND){
    				drawPolygon(diamond,i,j,widthBetweenLines,heightBetweenLines);
    			}
    			else if(val==CritterShape.STAR){
    				drawPolygon(star,i,j,widthBetweenLines,heightBetweenLines);
    			}
    			else if(val==CritterShape.TRIANGLE){
    				drawPolygon(triangle,i,j,widthBetweenLines,heightBetweenLines);
    			}
    			else if(val==CritterShape.SQUARE){
    				drawPolygon(square,i,j,widthBetweenLines,heightBetweenLines);
    			}
    		}
    	}
    }
    
    private static void drawPolygon(double[][] shapeCoordinates,int row, int col, double widthBetweenLines,double heightBetweenLines){
    	double[] xCoords=new double[shapeCoordinates.length];
    	double[] yCoords=new double[shapeCoordinates.length];
    	double horizontalSize=widthBetweenLines-gridLineWidth;
    	double verticalSize=heightBetweenLines-gridLineWidth;
    	double horizontalOffset=widthBetweenLines*col+gridLineWidth;
    	double verticalOffset=heightBetweenLines*row+gridLineWidth;
    	for(int i=0;i<shapeCoordinates.length;i++){
    		xCoords[i]=shapeCoordinates[i][0]*horizontalSize+horizontalOffset;
    		yCoords[i]=shapeCoordinates[i][1]*verticalSize+verticalOffset;
    	}
    	gridGraphicsContext.strokePolygon(xCoords,yCoords,shapeCoordinates.length);
    	gridGraphicsContext.fillPolygon(xCoords,yCoords,shapeCoordinates.length);
    }

    private static void addAnimationGridPane(GridPane mainGridPane){
    	animate=new GridPane();
        animate.setHgap(10);
        animate.setVgap(10);
        animate.setPadding(new Insets(10, 2, 10, 2));
        
        Label animateLabel=new Label();
    	animateLabel.setText("ANIMATION TOOL");
    	animate.add(animateLabel, 0,0);
    	
    	
    	Label animateSpeed=new Label();
    	animateSpeed.setText("Set Speed");
    	animate.add(animateSpeed, 0,1);
        
    	speedComboBox=new ComboBox<String>();
    	speedComboBox.getItems().addAll("1x" , "5x" , "10x" , "100x"); 
    	speedComboBox.getSelectionModel().selectFirst();

    	animate.add(speedComboBox,1,1);
    	
    	
        animateButton = new Button();
        animateButton.setText("Start");
        animateButton.setOnAction(e->animateHandler(speedComboBox.getValue()));
        animate.add(animateButton, 0,2 );
        
        animateStopButton = new Button();
        animateStopButton.setText("Stop");
        animateStopButton.setOnAction(e->animateStopHandler());
        animate.add(animateStopButton, 1,2 );
        
        
        mainGridPane.add(animate, 0, 4);
    }
    	
    	
    	
    private static void animateStopHandler() {
    	statsTypeButton.setDisable(false);
        speedComboBox.setDisable(false);
        quitButton.setDisable(false);
      critterTypeComboBox.setDisable(false);
   	makeCritterButton.setDisable(false);
   	critterQuantityTextField.setDisable(false);
   	timeStepButton.setDisable(false);
   	numStepsTextField.setDisable(false);
   	seedNumberTextField.setDisable(false);
	setSeedButton.setDisable(false);
	statsTypeButton.setDisable(false);
	statsType.setDisable(false);
    	timer.cancel();
	}

    
    private static void disableControls(){
    	 statsTypeButton.setDisable(true);
         speedComboBox.setDisable(true);
         quitButton.setDisable(true);
       critterTypeComboBox.setDisable(true);
    	makeCritterButton.setDisable(true);
    	critterQuantityTextField.setDisable(true);
    	timeStepButton.setDisable(true);
    	numStepsTextField.setDisable(true);
    	seedNumberTextField.setDisable(true);
    	setSeedButton.setDisable(true);
    	statsTypeButton.setDisable(true);
    	statsType.setDisable(true);
    }

	private static void animateHandler(String string){   
    	animating = true;
    	timer = new Timer();
    	disableControls();
        animateGo = new TimerTask(){
    		
    		@Override
			public void run() {
     				int speed = 1;
        	    	if(string.equals("1x")){
        	    		speed = 1;
        	    	}
        	    	else if(string.equals("5x")){
        	    		speed = 5;
        	    	}
        	    	else if(string.equals("10x")){
        	    		speed = 10;
        	    	}
        	    	else if(string.equals("100x")){
        	    		speed = 100;
        	    	}
        	    	else
        	    		speed = 1;
    		
            		timeStepEventHandler(Integer.toString(speed));

    	    		    			
    			
    			if(statsType.getValue().isEmpty() == false){
	    			Platform.runLater(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
	    	    			runStatsEventHandler(statsType.getValue());

						}

	    			});    	    		
	    			}
    			
			}
    	};
    	
    		
        	timer.scheduleAtFixedRate(animateGo, 500, 1000);

        
    	
    }
	

    
    
    private static void addMakeCritterGridPane(GridPane mainGridPane){
    	GridPane makeCritter=new GridPane();
        makeCritter.setHgap(10);
        makeCritter.setVgap(10);
        makeCritter.setPadding(new Insets(10, 2, 10, 2));
    	
    	Label makeCritterLabel=new Label();
    	makeCritterLabel.setText("MAKE NEW CRITTER");
    	makeCritter.add(makeCritterLabel, 0,0);
    	
    	Label critterType=new Label();
    	critterType.setText("Type");
    	makeCritter.add(critterType, 0,1);
    	
    	critterTypeComboBox=new ComboBox<String>();
    	critterTypeComboBox.getItems().addAll(getAllCritterType(".")); 
    	makeCritter.add(critterTypeComboBox,1,1);
    	
    	Label critterQuantity=new Label();
    	critterQuantity.setText("Quantity");
    	makeCritter.add(critterQuantity,0,2);
    	
    	critterQuantityTextField=new TextField();
    	makeCritter.add(critterQuantityTextField,1,2);
    	
    	makeCritterButton = new Button();
        makeCritterButton.setText("Add Critters");
        makeCritterButton.setOnAction(e->makeCritterHandler(critterTypeComboBox.getValue(), critterQuantityTextField.getText()));
        makeCritter.add(makeCritterButton, 0,3 );
        
        
        mainGridPane.add(makeCritter, 0, 0);
    }
	
   
    private static ArrayList<String> getAllCritterType(String path){
    	 	   	
    	File file=new File(path);
    	
    	ArrayList<File> critterTypes=new ArrayList<File>();
    	getAllClassFiles(critterTypes,path);
    	
    	ArrayList<String> critterNames=new ArrayList<String>();
    	for(int i = 0; i < critterTypes.size(); i++){
			try{
				String className = critterTypes.get(i).getName().replace(".class", ""); 
				Class<?> testingClass = Class.forName(myPackage + "." + className); 
				if(testingClass.newInstance() instanceof Critter) {
					critterNames.add(className);
				}
				
			}
			catch(LinkageError ex){
				
			}
			catch(ClassCastException ex){ 
				//try the next file
			}
			catch(ClassNotFoundException cnf){ 
				//try the next file
			}
			catch (IllegalAccessException ec) {
				//try the next file
			} 
			catch(InstantiationException in) {
				//try the next file
			}
			catch (IllegalArgumentException ee) {
				//try the next file
			} 
		}
    
    	return critterNames;
    }

    private static void getAllClassFiles(ArrayList<File> f,String path){
        File critter = new File(path);
        //get all the files from a directory
        File[] filesList = critter.listFiles();
        for (File file : filesList){
            if (file.isDirectory()){
                getAllClassFiles(f,file.getAbsolutePath());
            }
            if (file.isFile() && file.getName().endsWith(".class")){
                f.add(file);
            } 
        }
    }	
    
    
    
    private static void makeCritterHandler(String type,String quantity){
    	if(type==null){
    		
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Error");
    		alert.setHeaderText(null);
    		alert.setContentText("Critter not chosen");
    		alert.showAndWait();
    		return;
    	}
    	
    	int numCritters;
    	try{
    		numCritters=Integer.parseInt(quantity);
    	}catch(NumberFormatException ex){
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Error");
    		alert.setHeaderText(null);
    		if(quantity.trim().length()>0){
    			alert.setContentText("Invalid Quantity: "+quantity.trim());
    		}
    		else{
    			alert.setContentText("No number entered");
    		}
    		
    		alert.showAndWait();
    		return;
    	}
    	try{
    	for(int i=0;i<numCritters;i++){
    		Critter.makeCritter(type);
    		
    	}
    	}catch(InvalidCritterException ex){
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Error");
    		alert.setHeaderText(null);
    		
    			alert.setContentText("Invalid Critter Exception thrown");
    		
    	}
    	Critter.displayWorld();
    	
    }
    
    private static void addTimeStepGridPane(GridPane mainGridPane){
        GridPane timeStepGridPane=new GridPane();
        timeStepGridPane.setHgap(10);
        timeStepGridPane.setVgap(10);
        timeStepGridPane.setPadding(new Insets(10, 2, 10, 2));
       
        Label TimeStepLabel=new Label();
    	TimeStepLabel.setText("TIME STEPPER");
    	timeStepGridPane.add(TimeStepLabel, 0,0);
    	
    	Label numTimeStepLabel=new Label();
    	numTimeStepLabel.setText("Number of Steps");
    	timeStepGridPane.add(numTimeStepLabel,0,1);
    	
    	numStepsTextField=new TextField();
    	timeStepGridPane.add(numStepsTextField,1,1);
    	
    	timeStepButton = new Button();
        timeStepButton.setText("Step");
        timeStepButton.setOnAction(e->timeStepEventHandler(numStepsTextField.getText()));
        timeStepGridPane.add(timeStepButton, 0, 2);
        
        
        
        mainGridPane.add(timeStepGridPane, 0,1 );
    }
    
    private static void timeStepEventHandler(String numSteps){
    	int step;
    	try{
			step=Integer.parseInt(numSteps);
		}catch(NumberFormatException ex){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			if(numSteps.trim().length()>0){
				alert.setContentText("Enter a valid number: "+numSteps);
			}
			else{
				alert.setContentText("No number entered");
			}
			alert.showAndWait();
			return;
		}
		for(int i=0;i<step;i++){
			Critter.worldTimeStep();
		}
    	Critter.displayWorld();  	
    	
		if(statsType.getValue().isEmpty() == false){
			Platform.runLater(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
	    			runStatsEventHandler(statsType.getValue());

				}

			});    	    		
			}
    }
   
    private static void addStatsGridPane(GridPane mainGridPane){
    	GridPane statisticsGridPane=new GridPane();
        statisticsGridPane.setHgap(10);
        statisticsGridPane.setVgap(10);
        statisticsGridPane.setPadding(new Insets(10, 2, 10, 2));
       
        Label statisticsLabel=new Label();
    	statisticsLabel.setText("RUNSTATS TOOL");
    	statisticsGridPane.add(statisticsLabel, 0,0);
    	
    	Label statsTypeLabel=new Label();
    	statsTypeLabel.setText("Type");
    	statisticsGridPane.add(statsTypeLabel,0,1);
    	
    	statsTypeButton = new Button("Run Stats");
    	statisticsGridPane.add(statsTypeButton, 2, 1);
    	
    	
    	 statsType=new ComboBox<String>();
    	statisticsGridPane.add(statsType,1,1);
    	
    	statsType.getItems().addAll("");
    	statsType.getItems().addAll(getAllCritterType(".")); 
    	statsType.getSelectionModel().selectFirst();

    	statsTypeButton.setOnAction(e->runStatsEventHandler(statsType.getValue()));
    	
    	
    	statsLabel=new Label();
    	statsLabel.setText("Critter not selected");
    	statisticsGridPane.add(statsLabel,0, 2);
    
    	
        mainGridPane.add(statisticsGridPane,0,2);
    }
    
    private static void runStatsEventHandler(String text){
    	
    	String displayString;
    	List<Critter> critterList;
    	
    	try{
    		 critterList=Critter.getInstances(text);
    		 
    		 try{
    		    	Class<?> inClass=Class.forName(myPackage+"."+ text);
    		    	Method inMethod=inClass.getMethod("runStats",List.class);
    		    	
    		    	displayString=(String)inMethod.invoke(null,critterList);
    		    	statsLabel.setText(displayString);

    		    	
    		 		}catch(Exception ex){
    		    		
    		    		throw new InvalidCritterException(text);
    		    		
    		    	}
    		 
    		 }catch(InvalidCritterException ex){
    			 
    			 
    			 Alert alert = new Alert(AlertType.INFORMATION);
    				alert.setTitle("Error");
    				alert.setHeaderText(null);
    				alert.setContentText("Invalid Critter Exception");
    				alert.showAndWait();
    				return;
    			
    		 }
    	
    }
    
    private static void addSeedGridPane(GridPane mainGridPane){
    	GridPane seedGridPane=new GridPane();
        seedGridPane.setHgap(10);
        seedGridPane.setVgap(10);
        seedGridPane.setPadding(new Insets(10, 2, 10, 2));
       
        Label seedLabel=new Label();
    	seedLabel.setText("SET RANDOM SEED");
    	seedGridPane.add(seedLabel, 0,0);
    	
    	Label seedNumberTextLabel=new Label();
    	seedNumberTextLabel.setText("Seed");
    	seedGridPane.add(seedNumberTextLabel,0,1);
    	
    	seedNumberTextField=new TextField();
    	seedGridPane.add(seedNumberTextField,1,1);
    	
    	setSeedButton = new Button();
        setSeedButton.setText("Set seed");
        setSeedButton.setOnAction(e->seedEventHandler(seedNumberTextField.getText()));
        seedGridPane.add(setSeedButton, 0, 2);
        
        
        mainGridPane.add(seedGridPane, 0,3);
    }
    
    private static void seedEventHandler(String text){
    	int seed;
    	try{
			seed= Integer.parseInt(text);
		}catch(NumberFormatException ex){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			if(text.length()>0){
				alert.setContentText("Number not entered: "+text);
			}
			else{
				alert.setContentText("No number entered");
			}
			alert.showAndWait();
			return;
		}
    	
		Critter.setSeed(seed);
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Seed Set");
		alert.setHeaderText(null);
			alert.setContentText("Seed set to "+text);
		
	
		alert.showAndWait();
    }
    
	public static void main(String[] args) {
		launch(args);
	}
}