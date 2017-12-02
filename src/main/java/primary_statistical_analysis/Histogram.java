package primary_statistical_analysis;

import java.awt.*;
import java.util.Random;

import org.jfree.chart.*;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.UIUtils;
import org.jfree.data.statistics.*;
import org.jfree.chart.plot.PlotOrientation;

public class Histogram extends ApplicationFrame {

    public Histogram(String title) {
        super(title);
        double[] value = new double[100];
        Random generator = new Random();
        for (int i = 1; i < 100; i++) {
            value[i] = generator.nextDouble();
        }
        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.RELATIVE_FREQUENCY);
        int number = 10;
        dataset.addSeries("Histogram", value, number);
        String plotTitle = "Histogram";
        String xaxis = "number";
        String yaxis = "value";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = false;
        boolean urls = false;
        JFreeChart chart = ChartFactory.createHistogram(plotTitle, xaxis, yaxis,
                dataset, orientation, show, toolTips, urls);
        ChartPanel chartPanel = new ChartPanel(chart, false);
        chartPanel.setFillZoomRectangle(true);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(chartPanel);

    }

    public static void main(String[] args) {
        Histogram demo = new Histogram("JFreeChart: BarChartDemo1.java");
        demo.pack();
        UIUtils.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }
}