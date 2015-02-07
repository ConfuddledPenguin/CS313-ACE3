package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import memory.StatsInterface;

/**
 * The results controller
 * 
 * @author Tom Maxwell
 *
 */
public class ResultsController {
	
	//ui elements
	@FXML private Label configLabel;
	@FXML private Label configTLBsizeLabel;
	@FXML private Label configPagesizeLabel;
	@FXML private Label configTLBAlgoLabel;
	@FXML private Label configPageAlgoLabel;
	@FXML private Label configInputTypeLabel;
	
	@FXML private Pane TLBChartPane;
	@FXML private Pane PageChartPane;
	@FXML private Button returnToConfigButton;
	
	@FXML private Label totalReadsLabel;
	@FXML private Label totalBytesReadLabel;
	@FXML private Label totalTLBMissesLabel;
	@FXML private Label totalPageFaultsLabel;
	
	@FXML private Label totalTLBHitsLabel;
	@FXML private Label totalTLBHitRateLabel;
	@FXML private Label totalPageHitsLabel;
	@FXML private Label totalPageHitRateLabel;
	
	@FXML private Label pageLoadsInfo;
	
	private Home home;
	private Scene scene;
	private StatsInterface stats;
	private Config config;
	
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
	 * The constructor
	 * 
	 * @param home The window
	 * @param scene The scene where are in charge off
	 * @param stats The stats from the memory systems performance
	 * @param config The config ran
	 */
	public ResultsController(Home home, Scene scene, StatsInterface stats, Config config) {
		this.home = home;
		this.scene = scene;
		this.stats = stats;
		this.config = config;
	}
	
	/**
	 * Show the results
	 */
	public void display(){
		
		//Using a grid for this would have bee cleaner but more effort. Hence all the info was thrown in one label spaced using tabs.
		configLabel.setText("The configuration ran is:");
		configTLBsizeLabel.setText("Number of TLB entries - " + config.TLBSize );
		configPagesizeLabel.setText("No of frames - " + config.noFrames);
		configTLBAlgoLabel.setText("TLB replacement algorithm - " + config.TLBAlgo);
		configPageAlgoLabel.setText("PageTable replacement algorithm - " + config.PageAlgo);
		configInputTypeLabel.setText("Input type used - " + config.inputfileType);
		
		//First chart - TLB misses
		ObservableList<PieChart.Data> data = FXCollections.observableArrayList(
				new PieChart.Data("Hits", stats.getTLBHits()),
				new PieChart.Data("Misses", stats.getTLBMisses()));
		DoughtnutChart tlbHitsChart = new DoughtnutChart(data);
		tlbHitsChart.setStartAngle(270);
		tlbHitsChart.setLabelsVisible(false);
		tlbHitsChart.setTitle("TLB Hits/Misses");
		tlbHitsChart.prefWidthProperty().bind(TLBChartPane.prefWidthProperty());
		tlbHitsChart.prefHeightProperty().bind(TLBChartPane.prefHeightProperty());
		TLBChartPane.getChildren().add(tlbHitsChart);
		
		//Second chart - Page misses
		data = FXCollections.observableArrayList(
				new PieChart.Data("Hits", stats.getPageHits()),
				new PieChart.Data("Misses", stats.getPageFaults()));
		DoughtnutChart pageHitsChart = new DoughtnutChart(data);
		pageHitsChart.setStartAngle(270);
		pageHitsChart.setTitle("PageTable Hits/faults");
		pageHitsChart.setLabelsVisible(false);
		pageHitsChart.prefHeightProperty().bind(PageChartPane.prefHeightProperty());
		pageHitsChart.prefWidthProperty().bind(PageChartPane.prefWidthProperty());
		PageChartPane.getChildren().add(pageHitsChart);
		
		//Detailed stats
		totalReadsLabel.setText("Total Reads - " + stats.getTotalReads());
		totalBytesReadLabel.setText("Total bytes read from disk - " + stats.getBytesRead());
		totalTLBMissesLabel.setText("Total TLB misses - " + stats.getTLBMisses());
		totalPageFaultsLabel.setText("Total Pagetable fualts - " + stats.getPageFaults());
		
		totalTLBHitsLabel.setText("Total TLB hits - " + stats.getTLBHits());
		totalTLBHitRateLabel.setText("TLB hit rate - " + stats.getTLBHitPercentage() + "%");
		totalPageHitsLabel.setText("Total Page hits - " + stats.getPageHits());
		totalPageHitRateLabel.setText("Pagetable hit rate - " + stats.getPageHitPercentage() + "%");
		
		StringBuilder sb = new StringBuilder();
		sb.append(stats.getNumberOfPagesLoadedMaxTimes() + " pages where loaded " + stats.getMaxTimesAPageWasLoaded() +
							" times, the addresses of these pages where: ");
		
		int i = 0;
		for(int value: stats.getMostLoadedPages()){
			i++;
			sb.append(value);
			
			if( i > 6 && stats.getMostLoadedPages().size() != 7){
				sb.append("...");
				break;
			}
			
			if(stats.getMostLoadedPages().size() != i){
				sb.append(", ");
			}
		}
		pageLoadsInfo.setText(sb.toString());
		
		
		//Return to config button
		returnToConfigButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				home.goBackToConfig();
			}
		});
		
	}
}
