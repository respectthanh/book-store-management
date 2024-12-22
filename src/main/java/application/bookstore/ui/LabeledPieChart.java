package application.bookstore.ui;

import javafx.collections.ListChangeListener;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.Region;
import javafx.scene.shape.Arc;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

public class LabeledPieChart extends PieChart {

    private final Map<Data, Text> _labels = new HashMap<>();

    public LabeledPieChart() {
        super();
        this.getData().addListener((ListChangeListener.Change<? extends Data> c) -> {
            addLabels();
        });

    }

    private static double normalizeAngle(double angle) {
        double a = angle % 360;
        if (a <= -180) {
            a += 360;
        }
        if (a > 180) {
            a -= 360;
        }
        return a;
    }

    private static double calcX(double angle, double radius, double centerX) {
        return centerX + radius * Math.cos(Math.toRadians(-angle));
    }

    private static double calcY(double angle, double radius, double centerY) {
        return centerY + radius * Math.sin(Math.toRadians(-angle));
    }

    @Override
    protected void layoutChartChildren(double top, double left, double contentWidth, double contentHeight) {
        super.layoutChartChildren(top, left, contentWidth, contentHeight);

        double centerX = contentWidth / 2 + left;
        double centerY = contentHeight / 2 + top;

        layoutLabels(centerX, centerY);
    }

    private void addLabels() {

        for (Text label : _labels.values()) {
            this.getChartChildren().remove(label);
        }
        _labels.clear();
        for (Data vData : getData()) {
            final Text dataText;
            final double yValue = vData.getPieValue();
            dataText = new Text(Double.toString(yValue));
            _labels.put(vData, dataText);
            this.getChartChildren().add(dataText);
        }
    }

    private void layoutLabels(double centerX, double centerY) {

        double total = 0.0;
        for (Data d : this.getData()) {
            total += d.getPieValue();
        }
        double scale = (total != 0) ? 360 / total : 0;

        for (Map.Entry<Data, Text> entry : _labels.entrySet()) {
            Data vData = entry.getKey();
            Text vText = entry.getValue();

            Region vNode = (Region) vData.getNode();
            Arc arc = (Arc) vNode.getShape();

            double start = arc.getStartAngle();

            double size = (isClockwise()) ? (-scale * Math.abs(vData.getPieValue())) : (scale * Math.abs(vData.getPieValue()));
            final double angle = normalizeAngle(start + (size / 2));
            final double sproutX = calcX(angle, arc.getRadiusX() / 2, centerX);
            final double sproutY = calcY(angle, arc.getRadiusY() / 2, centerY);

            vText.relocate(
                    sproutX - vText.getBoundsInLocal().getWidth(),
                    sproutY - vText.getBoundsInLocal().getHeight());
        }

    }

}