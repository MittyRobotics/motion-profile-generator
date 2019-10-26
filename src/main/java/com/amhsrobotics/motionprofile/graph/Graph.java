package com.amhsrobotics.motionprofile.graph;

import com.amhsrobotics.motionprofile.datatypes.MotionFrame;
import com.amhsrobotics.motionprofile.TrapezoidalMotionProfile;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class Graph {




	public Graph(final String title, TrapezoidalMotionProfile motion_profile) {

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
		slider2.setMaximum(200);
		slider2.setValue(120);
		Box p = new Box(BoxLayout.X_AXIS);

		p.add(new JLabel("Max Velocity:"));
		p.add(slider);
		p.add(new JLabel("Max Acceleration:"));
		p.add(slider1);
		p.add(new JLabel("Setpoint:"));
		p.add(slider2);
//		slider.addChangeListener(new ChangeListener() {
//			@Override
//			public void stateChanged(ChangeEvent e) {
//				//System.out.println(velocity.getItemCount());
//				velocity.clear();
//				position.clear();
//				acceleration.clear();
//				TrapezoidalMotionProfile newProfile = new TrapezoidalMotionProfile(slider1.getValue(),slider.getValue(),slider2.getValue(), 0.05);
//				for(int i = 0; i < newProfile.getSteps(); i++){
//					double t = newProfile.stepsToTime(i);
//					position.add(t,newProfile.getFrameAtTime(t).getPosition());
//					velocity.add(t, newProfile.getFrameAtTime(t).getVelocity());
//					acceleration.add(t, newProfile.getFrameAtTime(t).getAcceleration());
//				}
//			}
//		});
//
//		slider1.addChangeListener(new ChangeListener() {
//			@Override
//			public void stateChanged(ChangeEvent e) {
//				//System.out.println(velocity.getItemCount());
//				velocity.clear();
//				position.clear();
//				acceleration.clear();
//				TrapezoidalMotionProfile newProfile = new TrapezoidalMotionProfile(slider1.getValue(),slider.getValue(),slider2.getValue(),0.05);
//				for(int i = 0; i < newProfile.getSteps(); i++){
//					double t = newProfile.stepsToTime(i);
//					position.add(t,newProfile.getFrameAtTime(t).getPosition());
//					velocity.add(t, newProfile.getFrameAtTime(t).getVelocity());
//					acceleration.add(t, newProfile.getFrameAtTime(t).getAcceleration());
//				}
//			}
//		});
//
//		slider2.addChangeListener(new ChangeListener() {
//			@Override
//			public void stateChanged(ChangeEvent e) {
//				//System.out.println(velocity.getItemCount());
//				velocity.clear();
//				position.clear();
//				acceleration.clear();
//				TrapezoidalMotionProfile newProfile = new TrapezoidalMotionProfile(slider1.getValue(),slider.getValue(),slider2.getValue(),0.05);
//				for(int i = 0; i < newProfile.getSteps(); i++){
//					double t = newProfile.stepsToTime(i);
//					position.add(t,newProfile.getFrameAtTime(t).getPosition());
//					velocity.add(t, newProfile.getFrameAtTime(t).getVelocity());
//					acceleration.add(t, newProfile.getFrameAtTime(t).getAcceleration());
//				}
//			}
//		});

		f.add(p, BorderLayout.SOUTH);
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
