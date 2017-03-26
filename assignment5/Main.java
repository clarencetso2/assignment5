package assignment5;


import java.io.File;
import java.net.URL;

import javax.swing.Painter;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
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
    public static int gridRows=10;//Reset to correct attribute in code
    public static int gridCols=10;
    public static double gridLineWidth=10;
    public static double screenHeight=800;
    public static double screenWidth=800;
	
	
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
    
    

	
//	public static void drawCritters(Critter[][] grid, double width, double height) {
		
//	}



	public static void main(String[] args) {
		launch(args);
	}
}



	