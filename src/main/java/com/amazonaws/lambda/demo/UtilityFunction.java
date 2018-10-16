package com.amazonaws.lambda.demo;
import java.util.ArrayList;

public class UtilityFunction {
	private ArrayList<UtilityFunctionPoint> function;
    public UtilityFunction(){
        function= new ArrayList<UtilityFunctionPoint>();
    }
    ArrayList<UtilityFunctionPoint> getFunction(){
        return function;
    }

    void addPoint(UtilityFunctionPoint p){
        function.add(p);
    }

    void addPoint(double m, double hit){
        function.add(new UtilityFunctionPoint(m,hit));
    }

    public int size(){
        return function.size();
    }

    public UtilityFunctionPoint get(int index){
        return function.get(index);
    }
}
