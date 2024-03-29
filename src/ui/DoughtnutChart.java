package ui;

import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * The charts used to display the data
 * 
 * I don't know javaFX well enough to create these wonderful things
 * hence I have borrowed from this post, because why reinvent the wheel
 * when its not really the assignment and the code is in the public domain.
 * 
 * The creator can be found here
 * http://stackoverflow.com/questions/24121580/can-piechart-from-javafx-be-displayed-as-a-doughnut
 * 
 * This class created Doughnut charts. These are hollow pie charts.
 * 
 * It achieves this by wrapping the pi charts in a wrapper (this class)
 * Then a circle is places in the centre of the pie chart.
 */
public class DoughtnutChart extends PieChart{

	private final Circle innerCircle;
	
	public DoughtnutChart(ObservableList<Data> data) {
		super(data);

        innerCircle = new Circle();

        // just styled in code for demo purposes,
        // use a style class instead to style via css.
        innerCircle.setFill(Color.WHITE);
	}
	
	@Override
    protected void layoutChartChildren(double top, double left, double contentWidth, double contentHeight) {
        super.layoutChartChildren(top, left, contentWidth, contentHeight);

        addInnerCircleIfNotPresent();
        updateInnerCircleLayout();
    }

    private void addInnerCircleIfNotPresent() {
        if (getData().size() > 0) {
            Node pie = getData().get(0).getNode();
            if (pie.getParent() instanceof Pane) {
                Pane parent = (Pane) pie.getParent();

                if (!parent.getChildren().contains(innerCircle)) {
                    parent.getChildren().add(innerCircle);
                }
            }
        }
    }

    private void updateInnerCircleLayout() {
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;
        for (PieChart.Data data: getData()) {
            Node node = data.getNode();

            Bounds bounds = node.getBoundsInParent();
            if (bounds.getMinX() < minX) {
                minX = bounds.getMinX();
            }
            if (bounds.getMinY() < minY) {
                minY = bounds.getMinY();
            }
            if (bounds.getMaxX() > maxX) {
                maxX = bounds.getMaxX();
            }
            if (bounds.getMaxY() > maxY) {
                maxY = bounds.getMaxY();
            }
        }

        innerCircle.setCenterX(minX + (maxX - minX) / 2);
        innerCircle.setCenterY(minY + (maxY - minY) / 2);

        innerCircle.setRadius((maxX - minX) / 4);
    }
}