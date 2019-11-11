package com.amhsrobotics.motionprofile.graph;

import com.amhsrobotics.motionprofile.datatypes.MotionFrame;
import com.amhsrobotics.motionprofile.TrapezoidalMotionProfile;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class GraphMotionProfile {




	public GraphMotionProfile(TrapezoidalMotionProfile motion_profile) {

		final XYSeries velocity = new XYSeries("Velocity",false);
		final XYSeries position = new XYSeries("Position",false);
		final XYSeries acceleration = new XYSeries("Acceleration",false);
		double t = 0;
		for(int i = 0; i < motion_profile.gettTotal()*100; i++){
			t = (double)i / 100;
			//System.out.println(t + " " + motion_profile.gettTotal());
			MotionFrame frame1 = motion_profile.getFrameAtTime(t);
			position.add(t,frame1.getPosition());
			velocity.add(t, frame1.getVelocity());
			acceleration.add(t, frame1.getAcceleration());
		}

		final XYSeriesCollection data = new XYSeriesCollection(velocity);
		data.addSeries(position);
		data.addSeries(acceleration);
		final JFreeChart chart = ChartFactory.createXYLineChart(
				"Motion Profile Example",
				"Time (seconds)",
				"Velocity (units/s), Position (units), Acceleration (units/s^2)",
				data,
				PlotOrientation.VERTICAL,
				true,
				true,
				false
		);


		chart.setBackgroundPaint(new Color(71, 71, 71));
		chart.getTitle().setPaint(new Color(158, 159, 157));

		chart.getLegend().setBackgroundPaint(new Color(71, 71, 71));
		LegendTitle title = chart.getLegend();
		title.setItemPaint(new Color(158, 159, 157));

		XYPlot plot = (XYPlot)chart.getPlot();
		plot.setBackgroundPaint(Color.DARK_GRAY);

		plot.getDomainAxis().setLabelPaint(new Color(158, 159, 157));
		plot.getRangeAxis().setLabelPaint(new Color(158, 159, 157));
		plot.getDomainAxis().setTickLabelPaint(new Color(158, 159, 157));
		plot.getRangeAxis().setTickLabelPaint(new Color(158, 159, 157));

		plot.setDomainGridlinePaint(new Color(0,0,0, 180));
		plot.setRangeGridlinePaint(new Color(0,0,0, 180));


		final ChartPanel panel = new ChartPanel(chart);
		panel.setPreferredSize(new java.awt.Dimension(800, 600));
		JFrame f = new JFrame();
		panel.setBackground(new Color(71, 71, 71));
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.add(panel);

		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	private class CustomColorRenderer extends XYLineAndShapeRenderer {

		private Shape shape;
		private Color color;

		public CustomColorRenderer(boolean lines, boolean shapes, Shape shape, Color color) {
			super(lines, shapes);
			this.shape = shape;
			this.color = color;
		}

		@Override
		public Shape getItemShape(int row, int column) {
			return shape;
		}

		@Override
		public Paint getItemPaint(int row, int col) {
			return color;
		}

	}
}
