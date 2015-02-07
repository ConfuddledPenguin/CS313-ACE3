package ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import memory.Global;

/**
 * This is the controller for the config view
 * 
 * @author Tom Maxwell
 *
 */
public class ConfigController {
	
	@FXML private ChoiceBox<String> TLBSize;
	@FXML private ChoiceBox<String> noFrames;
	@FXML private ChoiceBox<String> TLBAlgo;
	@FXML private ChoiceBox<String> PageAlgo;
	@FXML private ChoiceBox<String> inputType;
	@FXML private Button run;
	
	private Home home;
	private Scene scene;
	
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
	 * Constructor
	 * 
	 * @param home The window
	 * @param scene The scene we have populated
	 */
	public ConfigController(Home home, Scene scene) {
		this.home = home;
		this.scene = scene;
	}

	/**
	 * Called when the FXMl file has been loaded
	 * 
	 * This does basic setup that is overly difficult to achieve
	 * in the FXML files
	 */
	public void FXMLLoaded(){
		
		//Configure the choice boxes. Its easier here then in the fxml.
		TLBSize.getItems().addAll("8", "16", "32");
		TLBSize.setValue("16");
		TLBSize.setOnMouseEntered(mouseOn);
		TLBSize.setOnMouseExited(mouseOut);
		Tooltip tip = new Tooltip("The TLB size effects the number of entries stored the in TLB.");
		tip.getStyleClass().add("tooltip");
		TLBSize.setTooltip(tip);
		noFrames.getItems().addAll("64", "128", "256");
		noFrames.setValue("128");
		noFrames.setOnMouseEntered(mouseOn);
		noFrames.setOnMouseExited(mouseOut);
		tip = new Tooltip("The number of frames effects the size of the main memory. The more frames, the larger memory.");
		tip.getStyleClass().add("tooltip");
		noFrames.setTooltip(tip);
		TLBAlgo.getItems().addAll("FIFO", "LRU", "Random");
		TLBAlgo.setValue("FIFO");
		TLBAlgo.setOnMouseEntered(mouseOn);
		TLBAlgo.setOnMouseExited(mouseOut);
		tip = new Tooltip("The replacement algorithm used in the TLB.");
		tip.getStyleClass().add("tooltip");
		TLBAlgo.setTooltip(tip);
		PageAlgo.getItems().addAll("FIFO", "LRU", "Random");
		PageAlgo.setValue("FIFO");
		PageAlgo.setOnMouseEntered(mouseOn);
		PageAlgo.setOnMouseExited(mouseOut);
		tip = new Tooltip("The replacment algorithm used in the Page Table.");
		tip.getStyleClass().add("tooltip");
		PageAlgo.setTooltip(tip);
		inputType.getItems().addAll("Random", "Little Locality", "Large Locality");
		inputType.setValue("Random");
		inputType.setOnMouseEntered(mouseOn);
		inputType.setOnMouseExited(mouseOut);
		tip = new Tooltip("The input is the values from memory to be read.");
		tip.getStyleClass().add("tooltip");
		inputType.setTooltip(tip);
		
		run.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				System.out.println("run" + TLBSize.getValue());
				
			}
		});
		run.setOnMouseEntered(mouseOn);
		run.setOnMouseExited(mouseOut);
		
		run.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				handleRunButton();
			}
		});
	}
	
	/**
	 * Method to handle the run button being pressed
	 */
	private void handleRunButton(){
		
		Config config = new Config();
		
		config.TLBSize = Integer.parseInt(TLBSize.getValue());
		config.noFrames = Integer.parseInt(noFrames.getValue());
		config.TLBAlgo = getAlgo(TLBAlgo.getValue());
		config.PageAlgo = getAlgo(PageAlgo.getValue());
		
		try{
		if(inputType.getValue().equals("Random")){
			config.inputfile = "files/InputFile.txt";
			config.inputfileType = "Random";
		}else if(inputType.getValue().equals("Large Locality")){
			config.inputfile = "files/InputFile_3.txt";
			config.inputfileType = "Large Locality";
		}else{
			config.inputfile = "files/InputFile_2.txt";
			config.inputfileType = "Little Localtiy";
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		home.configEntered(config);
		
	}
	
	/**
	 * A method to get the algo from the users choice
	 * 
	 * @param algo The string representation of the algo
	 * @return The ENUM representation of the algo
	 */
	private Global.algorithm getAlgo(String algo){
		
		if(algo.equals(Global.algorithm.FIFO.toString())){
			return Global.algorithm.FIFO;
		}else if(algo.equals(Global.algorithm.LRU.toString())){
			return Global.algorithm.LRU;
		}else{ // assume that if not the above random
			return Global.algorithm.RANDOM;
		}
	}
}
