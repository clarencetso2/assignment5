package assignment5;


import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Painter;

import javafx.application.Application;
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

public class Main extends Application {
    //Variables created for drawing grid
    public static Canvas gridCanvas=null;//Main canvas which the world is displayed
    public static GraphicsContext gridGraphicsContext=null;
    public static int gridRows=10;//Initial rows and columns
    public static int gridCols=10;
    public static double gridLineWidth=10;
    public static double screenHeight=800;
    public static double screenWidth=800;
    public static String myPackage = Critter.class.getPackage().toString().split(" ")[1];
    
    
    //run stats
    public static ComboBox<String> statsType;
    public static Label statsLabel;

	static GridPane grid = new GridPane();
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
	           // 	addSeedGridPane(controllerGridPane);
	           // 	addQuitButton(controllerGridPane);
	            	
	            	
	            	controllerStage.setScene(new Scene(controllerGridPane));
	                controllerStage.setOnHiding(e->System.exit(0));
	            	
	                controllerStage.show();
	            	
	            	
	            	primaryStage.setTitle("Critter Display");
	    	    	Group root=new Group();
	    	    	gridCanvas=new Canvas(1280,720);
	    	    	gridGraphicsContext=gridCanvas.getGraphicsContext2D();
	    	    	root.getChildren().add(gridCanvas);
	    	    	primaryStage.setScene(new Scene(root));

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
    	gridGraphicsContext.fillRect(0,0,1280,720);
    	gridGraphicsContext.setFill(Color.BLACK);
    	gridLineWidth=Math.min(screenWidth/gridCols*1.0, screenHeight/gridRows*1.0)/10;
    	
    	double widthBetweenLines=(screenWidth-gridLineWidth*1.0)/(gridCols);
    	double heightBetweenLines=(screenHeight-gridLineWidth*1.0)/(gridRows);
    	for(int i=0;i<gridCols+1;i++){
    		gridGraphicsContext.fillRect(i*widthBetweenLines,0,gridLineWidth,screenHeight);
    	}
    	for(int i=0;i<gridRows+1;i++){
    		gridGraphicsContext.fillRect(0,i*heightBetweenLines,screenWidth,gridLineWidth);
    	}
    	
    	if(grid==null){
    		return;
    	}
    	//drawCritters(grid,widthBetweenLines,heightBetweenLines);
    	//statisticsEventHandler(statisticsComboBox.getValue());

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
    	
    	ComboBox<String> critterTypeComboBox=new ComboBox<String>();
    	critterTypeComboBox.getItems().addAll(getAllCritterType(".")); 
    	makeCritter.add(critterTypeComboBox,1,1);
    	
    	Label critterQuantity=new Label();
    	critterQuantity.setText("Quantity");
    	makeCritter.add(critterQuantity,0,2);
    	
    	TextField critterQuantityTextField=new TextField();
    	makeCritter.add(critterQuantityTextField,1,2);
    	
    	Button makeCritterButton = new Button();
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
    	
    	TextField numStepsTextField=new TextField();
    	timeStepGridPane.add(numStepsTextField,1,1);
    	
    	Button timeStepButton = new Button();
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
    	
    	Button statsTypeButton = new Button("Run Stats");
    	statisticsGridPane.add(statsTypeButton, 2, 1);
    	
    	
    	 statsType=new ComboBox<String>();
    	statisticsGridPane.add(statsType,1,1);
    	
    	
    	statsType.getItems().addAll(getAllCritterType(".")); 
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
    	statsLabel.setText(displayString);
    	
    }
    
  
    
    
	public static void main(String[] args) {
		launch(args);
	}
}



	