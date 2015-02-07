package ui;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The error display 
 * 
 * @author Tom Maxwell
 *
 */
public class Error {

	private String msg;
	private boolean exit;
	
	@FXML private Label msgLabel;
	@FXML private Button errorButton;
	
	/**
	 * Constructor
	 * 
	 * @param msg The message to show
	 * @param exit whether the program should close
	 * after usr clicks "OK"
	 */
	public Error(String msg, boolean exit) {
		this.msg = msg;
		this.exit = exit;
		
		display();
	}
	
	/**
	 * Show the error message
	 */
	private void display() {
		
		Stage error = new Stage();
		error.initStyle(StageStyle.UNDECORATED);
		
		Parent root = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("display/Error.fxml"));
			loader.setController(this);
			root = loader.load();
		} catch (IOException e) {
			/*
			 * Now this is interesting. The error failed.
			 * 
			 * What to do, what to do?
			 * 
			 * Cry? Cry loudly about unfair play.
			 * 
			 * Nothing we can really do here
			 * so just print the stack and hope for the best
			 */
			e.printStackTrace();
		}
		
		root.setId("errorStage");
		
		msgLabel.setText(msg);
		errorButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				if(exit){
					Platform.exit();
				}else{
					error.close();
				}
			}
		});
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(this.getClass().getResource("skin/application.css").toExternalForm());
		
		//Show scene in stage
		error.setScene(scene);
		error.show();

	}
}
