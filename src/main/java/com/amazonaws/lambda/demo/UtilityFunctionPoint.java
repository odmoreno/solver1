package com.amazonaws.lambda.demo;

public class UtilityFunctionPoint {
	private double hit;
    private double m;

    public UtilityFunctionPoint(double m, double hit){
        this.hit=hit;
        this.m=m;
    }

    public double getHit(){
        return hit;
    }
    public double getM(){
        return m;
    }
}
