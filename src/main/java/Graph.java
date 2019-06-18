import motion_profile.TrapazoidalMotionProfile;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class Graph {
	public Graph(final String title, TrapazoidalMotionProfile motion_profile) {

		final XYSeries velocity = new XYSeries("Velocity");
		final XYSeries position = new XYSeries("Position");
		final XYSeries acceleration = new XYSeries("Acceleration");
		for(int i = 0; i < motion_profile.getSteps(); i++){
			double t = motion_profile.stepsToTime(i);
			position.add(t,motion_profile.getFrameAtTime(t).getPosition());
			velocity.add(t, motion_profile.getFrameAtTime(t).getVelocity());
			acceleration.add(t, motion_profile.getFrameAtTime(t).getAcceleration());
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
		slider2.setMaximum(200);
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
				TrapazoidalMotionProfile newProfile = new TrapazoidalMotionProfile(slider1.getValue(),slider.getValue(),slider2.getValue(), motion_profile.getSteps());
				for(int i = 0; i < newProfile.getSteps(); i++){
					double t = newProfile.stepsToTime(i);
					position.add(t,newProfile.getFrameAtTime(t).getPosition());
					velocity.add(t, newProfile.getFrameAtTime(t).getVelocity());
					acceleration.add(t, newProfile.getFrameAtTime(t).getAcceleration());
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
				TrapazoidalMotionProfile newProfile = new TrapazoidalMotionProfile(slider1.getValue(),slider.getValue(),slider2.getValue(), motion_profile.getSteps());
				for(int i = 0; i < newProfile.getSteps(); i++){
					double t = newProfile.stepsToTime(i);
					position.add(t,newProfile.getFrameAtTime(t).getPosition());
					velocity.add(t, newProfile.getFrameAtTime(t).getVelocity());
					acceleration.add(t, newProfile.getFrameAtTime(t).getAcceleration());
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
				TrapazoidalMotionProfile newProfile = new TrapazoidalMotionProfile(slider1.getValue(),slider.getValue(),slider2.getValue(), motion_profile.getSteps());
				for(int i = 0; i < newProfile.getSteps(); i++){
					double t = newProfile.stepsToTime(i);
					position.add(t,newProfile.getFrameAtTime(t).getPosition());
					velocity.add(t, newProfile.getFrameAtTime(t).getVelocity());
					acceleration.add(t, newProfile.getFrameAtTime(t).getAcceleration());
				}
			}
		});

		f.add(p, BorderLayout.SOUTH);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
}
