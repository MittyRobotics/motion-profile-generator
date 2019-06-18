import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class Graph {
	public Graph(final String title, double[][] points) {

		final XYSeries velocity = new XYSeries("Velocity");
		final XYSeries position = new XYSeries("Position");
		final XYSeries acceleration = new XYSeries("Acceleration");
		for(int i = 0; i < points.length; i++){
			position.add(points[i][3], points[i][0]);
			velocity.add(points[i][3], points[i][1]);
			acceleration.add(points[i][3], points[i][2]);
			//System.out.println(points[i][0] + " " + points[i][3]);
		}
		final XYSeriesCollection data = new XYSeriesCollection(velocity);
		data.addSeries(position);
		data.addSeries(acceleration);
		final JFreeChart chart = ChartFactory.createXYLineChart(
				"Motion Profile Test",
				"Time (seconds)",
				"Velocity (ft/s), Position (ft), Acceleration (ft/s)",
				data,
				PlotOrientation.VERTICAL,
				true,
				true,
				false
		);

		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
		//setContentPane(chartPanel);
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.add(chartPanel);
		final JSlider slider = new JSlider(0, 10);
		slider.setMinimum(1);
		slider.setMaximum(50);
		slider.setValue(30);
		final JSlider slider1 = new JSlider(0, 10);
		slider1.setMinimum(1);
		slider1.setMaximum(50);
		slider1.setValue(10);
		final JSlider slider2 = new JSlider(0, 10);
		slider2.setMinimum(1);
		slider2.setMaximum(1000);
		slider2.setValue(120);
		Box p = new Box(BoxLayout.X_AXIS);

		p.add(new JLabel("Max Velocity:"));
		p.add(slider);
		p.add(new JLabel("Max Acceleration:"));
		p.add(slider1);
		p.add(new JLabel("Setpoint:"));
		p.add(slider2);
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				//System.out.println(velocity.getItemCount());
				velocity.clear();
				position.clear();
				acceleration.clear();
				double[][] newOutput = Main.generateMotionProfile((double)slider1.getValue()/10,(double)slider.getValue()/10,200,(double)slider2.getValue()/10);
				for( int i = 0; i < newOutput.length; i++){
					velocity.add(newOutput[i][3], newOutput[i][1]);
					position.add(newOutput[i][3], newOutput[i][0]);
					acceleration.add(newOutput[i][3], newOutput[i][2]);
				}
			}
		});

		slider1.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				//System.out.println(velocity.getItemCount());
				velocity.clear();
				position.clear();
				acceleration.clear();
				double[][] newOutput = Main.generateMotionProfile((double)slider1.getValue()/10,(double)slider.getValue()/10,200,(double)slider2.getValue()/10);
				for( int i = 0; i < newOutput.length; i++){
					velocity.add(newOutput[i][3], newOutput[i][1]);
					position.add(newOutput[i][3], newOutput[i][0]);
					acceleration.add(newOutput[i][3], newOutput[i][2]);
				}
			}
		});

		slider2.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				//System.out.println(velocity.getItemCount());
				velocity.clear();
				position.clear();
				acceleration.clear();
				double[][] newOutput = Main.generateMotionProfile((double)slider1.getValue()/10,(double)slider.getValue()/10,200,(double)slider2.getValue()/10);
				for( int i = 0; i < newOutput.length; i++){
					velocity.add(newOutput[i][3], newOutput[i][1]);
					position.add(newOutput[i][3], newOutput[i][0]);
					acceleration.add(newOutput[i][3], newOutput[i][2]);

				}
			}
		});

		f.add(p, BorderLayout.SOUTH);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
}
