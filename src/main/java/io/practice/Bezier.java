
package io.practice;

public class Bezier {
    public double[][] controlpoint;
    double Loss;
    public Bezier(double[][] controlpoint,double loss){
        this.controlpoint = controlpoint;
        this.Loss = loss;
    }
}