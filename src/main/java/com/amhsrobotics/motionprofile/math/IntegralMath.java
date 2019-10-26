package com.amhsrobotics.motionprofile.math;

/**
 * https://gist.github.com/JoseRivas1998/f6642e1e8dcea665b12e0f7264d3e088#file-mainwithoutlambda-java
 */
public class IntegralMath {
	public static final double INCREMENT = 1E-4;
	
	public static void main(String[] args) {
		System.out.println(integral(0, 2, new Function() {
			@Override
			public double f(double x) {
				return Math.pow(x, 2);
			}
		}));
	}
	
	public static double integral(double a, double b, Function function) {
		double area = 0;
		double modifier = 1;
		if(a > b) {
			double tempA = a;
			a = b;
			b = tempA;
			modifier = -1;
		}
		for(double i = a + INCREMENT; i < b; i += INCREMENT) {
			double dFromA = i - a;
			area += (INCREMENT / 2) * (function.f(a + dFromA) + function.f(a + dFromA - INCREMENT));
		}
		return (Math.round(area * 1000.0) / 1000.0) * modifier;
	}
}
