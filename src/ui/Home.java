package ui;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import memory.MMU;
import memory.MemorySubSystem;
import memory.StatsInterface;

public class Home extends Application {
	
	private Stage stage;
	private Scene scene;
	@FXML private Button close;
	@FXML private Button min;
	@FXML private Pane content;
	
	private SimpleBooleanProperty closeState;
	private SimpleBooleanProperty minState;
	
	private StatsInterface stats;
	
	EventHandler<MouseEvent> mouseOn = new EventHandler<MouseEvent>() {
	    public void handle(MouseEvent me) {
	        scene.setCursor(Cursor.HAND); //Change cursor to hand
	    }
	};
	
	EventHandler<MouseEvent> mouseOut = new EventHandler<MouseEvent>() {
	    public void handle(MouseEvent me) {
	        scene.setCursor(Cursor.DEFAULT); //Change cursor to hand
	    }
	};
	
	
	/**
	 * Initialise the states of the buttons
	 */
	private void initStates(){
		
		closeState = new SimpleBooleanProperty();
		closeState.set(false);
		
		closeState.addListener( new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean arg1, Boolean arg2) {
				
				Platform.runLater(new Runnable() {
		            @Override
		            public void run() {
		                stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
		            }
		        });
				
			}
		});
		
		minState = new SimpleBooleanProperty();
		minState.set(false);
		minState.addListener( new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				
				if (!Platform.isFxApplicationThread()) // Ensure on correct thread else hangs X under Unbuntu
		        {
		            Platform.runLater(new Runnable() {
		                @Override
		                public void run() {
		                    stage.setIconified(true);
		                }
		            });
		        } else {
		            stage.setIconified(true);
		        }
				
			}
		});
	}
	
	/**
	 * initaite the buttons, so that things happen
	 * when they are clicked on.
	 */
	private void initButtons(){
		
		/*
		 * The close buttons
		 */
		close.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				closeState.set(!closeState.get());
			}
		});
		
		/*
		 * The minimise button
		 */
		min.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				minState.set(!minState.get());
			}
		});

		
		min.setOnMouseEntered(mouseOn);
		close.setOnMouseEntered(mouseOn);
		min.setOnMouseExited(mouseOut);
		close.setOnMouseExited(mouseOut);
		
	}
	
	private void showConfig(){
		
		FadeTransition ft;
		if(content.getChildren().size() >= 1){ // if coming from results
			Node current = content.getChildren().get(0);
			ft = new FadeTransition(new Duration(700), current);
			ft.setFromValue(1);
			ft.setToValue(0);
			//remove the faded out element when done
			ft.setOnFinished(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					content.getChildren().remove(0);
					
				}
			});
			
			ft.play();
		}
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("display/config.fxml"));
		ConfigController configC = new ConfigController(this, scene);
		loader.setController(configC);
		Pane config = null;
		try {
			config = loader.load();
			configC.FXMLLoaded();
		} catch (IOException e) {
			stage.close();
			new Error("Error reading config.fxml", true);
			return;
		}
		
		content.getChildren().add(config);
		ft = new FadeTransition(new Duration(700), config);
		ft.setFromValue(0);
		ft.setToValue(1);
		
		ft.play();
	}
	
	private void showResults(Config config){
		
		Node current = content.getChildren().get(0);
		FadeTransition ft = new FadeTransition(new Duration(700), current);
		ft.setFromValue(1);
		ft.setToValue(0);
		ft.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				content.getChildren().remove(0);
				
			}
		});
		
		ft.play();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("display/results.fxml"));
		ResultsController resultsC = new ResultsController(this, scene, stats, config);
		loader.setController(resultsC);
		Pane results = null;
		try {
			results = loader.load();
			resultsC.display();
		} catch (IOException e) {
			stage.close();
			new Error("Error reading results.fxml", true);
			return;
		}
		
		content.getChildren().add(results);
		ft = new FadeTransition(new Duration(700), results);
		ft.setFromValue(0);
		ft.setToValue(1);
		
		ft.play();
	}
	
	/**
	 * The javaFX application starts here.
	 */
	@Override
	public void start(Stage primaryStage) {
		
		//stage
		stage = primaryStage;
		stage.initStyle(StageStyle.UNDECORATED);
		
		//get stuff to stick in stage
		Parent root = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("display/Home.fxml"));
			loader.setController(this);
			root = loader.load();
		} catch (IOException e) {
			stage.close();
			new Error("Error reading home.fxml", true);
			return;
		}
		
		//Get the window frame working
		initStates();
		initButtons();
		
		//Style scene
		scene = new Scene(root, 1000, 600);
		scene.getStylesheets().add(this.getClass().getResource("skin/application.css").toExternalForm());
		
		//Show scene in stage
		stage.setScene(scene);
		stage.show();
		
		showConfig();
		
	}
	
	/**
	 * The main method fot he javaFX application.
	 * 
	 * @param args none
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	void goBackToConfig(){
		
		showConfig();
	}
	
	void configEntered(Config config){
		
		MemorySubSystem mmu = null;

		try {
			mmu = new MMU(config.TLBSize, config.noFrames, config.TLBAlgo, config.PageAlgo);
			stats = mmu.getStats();
		} catch (IOException e1) {
			stage.close();
			new Error("Error writing to log.", true);
			return;
		}
		

				File input = new File(config.inputfile);
				LineNumberReader reader = null;
				
				try {
					reader  = new LineNumberReader(new FileReader(input));
				} catch (FileNotFoundException e) {
					stage.close();
					new Error("Error reading selected input file. Is " + input + " present", true);
					return;
				}
				
				try {
					String line;
					while((line = reader.readLine()) != null){
						
						int address = Integer.parseInt(line);
						mmu.read(address);
						//Uncomment below for a printout of line numbers to value
//						int value = mmu.read(address);
//						System.out.print("Line number " +reader.getLineNumber() + " \t");
//						System.out.println("Value: " + value + "");
					}
					reader.close();
				} catch (IOException e) {
					stage.close();
					new Error("Error reading selected input file.", true);
					return;
				}
		
		//no longer need it so free it for collection
		mmu = null;
		showResults(config);	
	}
}
