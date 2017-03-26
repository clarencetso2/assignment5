package assignment5;


import java.io.File;
import java.net.URL;

import javax.swing.Painter;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
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
	            	
	    			Scene scene = new Scene(grid, 500, 500);
	    			grid.setGridLinesVisible(true);
	            	primaryStage.setTitle("Grid");
	    			primaryStage.setScene(scene);
	    			primaryStage.show();
	            	
	            }
	        });
			
	        
	        
			
			
			
			// Paints the icons.
	//		Painter.paint(null, scene, 0, 0);
			
		} catch(Exception e) {
			e.printStackTrace();		
		}
	}
	

	
	
	
	public static void main(String[] args) {
		launch(args);
	}
}



	