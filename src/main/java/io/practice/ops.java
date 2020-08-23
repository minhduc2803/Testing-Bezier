package io.practice;

import java.util.ArrayList;

public class ops {
    public static double  minInList(ArrayList<double[]> lists,  int indexcol){
        // index is numberth of col
        double min = lists.get(0)[indexcol-1];
        for(double[] point : lists){
            if(point[indexcol-1]<min){
                min=point[indexcol-1];
            }
        }
        return min;
    }
    public static double maxInList(ArrayList<double[]> lists, int indexcol){
        double max =0;
        for(double[] point: lists){
            if(point[indexcol-1]>max){
                max=point[indexcol-1];

            }
        }
        return max;
    }
    public static ArrayList<double[]> divideByNumber(ArrayList<double[]> stroke, double value){

        for (int i =0; i<stroke.size();i++){
            double x = stroke.get(i)[0]/value;
            double y = stroke.get(i)[1]/value;
            stroke.get(i)[0]=x;
            stroke.get(i)[1]=y;
        }
        return stroke;
    }
    public static ArrayList<double[]> subtractByNumber(ArrayList<double[]> stroke,double value,int indexcol){
        for(int i=0;i<stroke.size();i++){
            double sub = stroke.get(i)[indexcol-1]-value;
            stroke.get(i)[indexcol-1] = sub;
        }
        return stroke;
    }
    public static void printsinit(double[] si){
        for (int i=0; i<si.length;i++) {
            System.out.print(si[i] +" , ");
        }
        System.out.print("\n");
    }
    public static void prints(double[][] s){
        for (int i=0; i<s.length;i++){
            for(int j=0;j<s[i].length;j++){
                System.out.print(s[i][j]+" , ");
            }
            System.out.print("\n");
        }
    }

}