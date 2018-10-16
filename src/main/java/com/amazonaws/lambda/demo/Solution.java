package com.amazonaws.lambda.demo;

public class Solution {
	private double[] xs;
    private double result;

    public Solution(double[] xs, double result){
        this.xs = xs;
        this.result = result;
    }
    public double[] getXs(){
        return xs;
    }
    public double getResult(){
        return result;
    }
}
