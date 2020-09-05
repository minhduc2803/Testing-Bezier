package io.practice;

import java.util.ArrayList;
import java.util.List;

public class preprocess {

    public List<Stroke> DataObject(List<ArrayList<double[]>> allstroke){
        int L = allstroke.size();
        for(int i =0 ; i<L;i++){
            allstroke.set(i,removeduplicate(allstroke.get(i)));
        }
        normalizeallstroke(allstroke);
        List<Stroke> allStrokeObject = new ArrayList<>();
        for(ArrayList<double[]> stroke : allstroke){
            allStrokeObject.add(Intostroke(stroke));
        }
        return allStrokeObject;
    }
    public Stroke Intostroke(ArrayList<double[]> stroke){
        ArrayList<Double> arc2 = argcTwo(stroke);
        ArrayList<Double> ratio = Ratio(stroke);
        return new Stroke(stroke,ratio,arc2);
    }

    public  ArrayList<double[]> removeduplicate( ArrayList<double[]> stroke){
        // only one stroke

        double t1 = stroke.get(0)[0];
        double t2 = stroke.get(0)[1];

        ArrayList<double[]> newstroke = new ArrayList<>();
        double[] temp ={t1,t2};
        newstroke.add(temp);

        for(double[] point:stroke){
//                        System.out.println("{" + t1 +" , "+t2+"}");
            if(point[0]!=t1 || point[1]!=t2)
            {
                newstroke.add(point);
                t1 = point[0];
                t2 = point[1];
            }
        }
        return newstroke;
    }

    public void normalizeallstroke(List<ArrayList<double[]>> allstroke){
        double minY = allstroke.get(0).get(0)[1];
        double maxY =0;
        double shiftx = allstroke.get(0).get(0)[0];
        for(ArrayList<double[]> stroke : allstroke){
            // find min max in each stroke;
            double mintemp = ops.minInList(stroke,2);
            if(mintemp<minY){
                minY=mintemp;
            }
            double maxtemp = ops.maxInList(stroke,2);
            if(maxtemp>maxY){
                maxY = maxtemp;
            }
        }
//        List<ArrayList<ArrayList<Double>>> newallstroke = new ArrayList<>();
        int L = allstroke.size();
        for(int i=L-1;i>=0;i--){
//            ArrayList<ArrayList<Double>> element = ops.subtractByNumber()
            if(allstroke.get(i).size()>1){
                allstroke.set(i,ops.subtractByNumber(allstroke.get(i),minY,2));
                allstroke.set(i,ops.subtractByNumber(allstroke.get(i), shiftx,1));
                allstroke.set(i,ops.divideByNumber(allstroke.get(i),maxY-minY));}
            else{
                allstroke.remove(i);
            }
        }
//        return allstroke;
    }
    public  ArrayList<Double> argcTwo(ArrayList<double[]> stroke){
        int L = stroke.size();
        ArrayList<Double> argctwo = new ArrayList<>();
        for(int i =0 ; i<L-1; i++){
            double ax = stroke.get(i)[0]-stroke.get(i+1)[0];
            double ay = stroke.get(i)[1]-stroke.get(i+1)[1];
            double arg = Math.sqrt(Math.pow(ax,2)+Math.pow(ay,2));
            argctwo.add(arg);

        }
        return argctwo;
    }
    public  ArrayList<Double> argcThree(ArrayList<double[]> stroke){
        int L = stroke.size();
        ArrayList<Double> argcthree = new ArrayList<>();
        for(int i =0 ; i<L-2; i++){
            double ax = stroke.get(i)[0]-stroke.get(i+2)[0];
            double ay = stroke.get(i)[1]-stroke.get(i+2)[1];
            double arg = Math.sqrt(Math.pow(ax,2)+Math.pow(ay,2));
            argcthree.add(arg);
        }
        return argcthree;
    }
    public  ArrayList<Double> Ratio(ArrayList<double[]> stroke){
        // Argale of next two point in one stroke
        ArrayList<Double> argtwo = argcTwo(stroke);
        ArrayList<Double> argthree = argcThree(stroke);
        int L = argthree.size();
        ArrayList<Double> ratio = new ArrayList<>();
        for(int i=0;i<L;i++){
            ratio.add(argthree.get(i)/(argtwo.get(i)+argtwo.get(i+1)));
        }

        return ratio;
    }
}