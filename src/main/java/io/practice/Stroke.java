package io.practice;

import java.util.ArrayList;

public class Stroke {

    public ArrayList<double[]> datapoint;
    ArrayList<Double> ratio;
    ArrayList<Double> argctwo;

    Stroke(ArrayList<double[]> datapoint,ArrayList<Double> ratio,ArrayList<Double> argctwo){
        this.datapoint =datapoint;// datapoint in one stroke
        this.ratio = ratio;
        this.argctwo =  argctwo;
    }


    public ArrayList<substroke> findBezier(substroke sub){
        ArrayList<substroke> allbezier = new ArrayList<>();
//        boolean flag = true;
        int n = sub.endpoint-sub.startpoint+1;

        Optimize opts = new Optimize(n);
        if(n>11) {
            if(ifTooSharp(sub)) {
                ArrayList<substroke> substrokedata = splitIftoosharp(sub);
                //return data and bezier
                ArrayList<substroke> subbecurvesOne = findBezier(substrokedata.get(0));
                boolean flagOne = subbecurvesOne.get(subbecurvesOne.size()-1).flag;
//  viet new function
                for (int i = 0; i < subbecurvesOne.size(); i++) {
                    allbezier.add(subbecurvesOne.get(i));
                }

                ArrayList<substroke> subbecurvesTwo = findBezier(substrokedata.get(1));
                boolean flagTwo = subbecurvesOne.get(subbecurvesOne.size()-1).flag;

                for (int i = 0; i < subbecurvesTwo.size(); i++) {
                    allbezier.add(subbecurvesTwo.get(i));
                }

                int index = subbecurvesOne.size();
                if(!(flagOne && flagTwo)) {
                    allbezier = stitBacktotal(allbezier, index);
                }
            }
            else{
                Bezier  bezieronly = opts.findBezierfunction(sub,this.datapoint);// loss and controk point;
                if(bezieronly.Loss >=0.00002){
                    ArrayList<substroke> substrokedata = splitByLoss(sub);
                    //return data and bezier
                    ArrayList<substroke> subbecurvesOne = findBezier(substrokedata.get(0));

                    boolean flagOne = subbecurvesOne.get(subbecurvesOne.size()-1).flag;
                    //                double flagOne =
                    for (int i = 0; i < subbecurvesOne.size(); i++) {
                        allbezier.add(subbecurvesOne.get(i));
                    }
                    ArrayList<substroke> subbecurvesTwo = findBezier(substrokedata.get(1));

                    boolean flagTwo = subbecurvesOne.get(subbecurvesOne.size()-1).flag;

                    for (int i = 0; i < subbecurvesTwo.size(); i++) {
                        allbezier.add(subbecurvesTwo.get(i));
                    }
                    int index = subbecurvesOne.size();
                    //                    int index = subbecurvesOne.size();
                    if(!(flagOne && flagTwo)) {
                        allbezier = stitBacktotal( allbezier, index);
                    }
                }
                else{
                    sub.B= bezieronly;
                    sub.flag = true;
                    allbezier.add(sub);
                }
            }
        }
        else{
            Bezier  bezieronly = opts.findBezierfunction(sub,this.datapoint);// loss and controk point;
            if(bezieronly.Loss>=0.00002){
                sub.B= bezieronly;
                sub.flag = false;
                allbezier.add(sub);
            }
            else{
                sub.B = bezieronly;
                sub.flag = true;
                allbezier.add(sub);
            }
        }
        return allbezier;
    }
    boolean ifTooSharp(substroke str){
//            int L = Stroke.this.datapoint.size();
        double ax = this.datapoint.get(str.startpoint)[0] - this.datapoint.get(str.endpoint)[0];
        double ay = this.datapoint.get(str.startpoint)[1] - this.datapoint.get(str.endpoint)[1];
        double EndDistance = Math.sqrt(Math.pow(ax,2)+Math.pow(ay,2));
        double totallens = 0;
        for(int i =str.startpoint; i< str.endpoint ;i++){
            totallens += this.argctwo.get(i);
        }
        return totallens>=3*EndDistance;
    }

    ArrayList<substroke> stitBacktotal(ArrayList<substroke> subBe,int index){
        if(subBe.size()>=3 && index>0){
//            System.out.println("index "+subBe.size());
            for(int i =0; i< index;i++){
                int e=0;
                if (i==0){
                    e=1;}

                for(int j=subBe.size()-1-e;j>index-1;j--){
                    substroke newsub = new substroke(subBe.get(i).startpoint,subBe.get(j).endpoint);
                    int n = newsub.endpoint-newsub.startpoint+1;
                    Optimize opts = new Optimize(n);
                    Bezier  bezieronly = opts.findBezierfunction(newsub,this.datapoint);
                    if(bezieronly.Loss<=0.00002){
                        newsub.B= bezieronly;
//                        newsub.flag = true;
                        subBe.set(i,newsub);
                        for( int k=0;k<j-i;k++) {
                            subBe.remove(i+1);
                        }
                        subBe.get(subBe.size()-1).flag=true;
                        return subBe;
                    }
                }
            }
        }
        return subBe;
    }
    ArrayList<substroke> splitIftoosharp(substroke str){
        // find min in ratio
        int indexOfSplit =str.startpoint+5;

        for(int i= indexOfSplit; i< str.endpoint-5;i++){
            if(this.ratio.get(i) < this.ratio.get(indexOfSplit)){
                indexOfSplit = i;
            }
        }
        indexOfSplit ++;
        ArrayList<substroke> subcurves = new ArrayList<>();
        subcurves.add(new substroke(str.startpoint,indexOfSplit));
        subcurves.add(new substroke(indexOfSplit,str.endpoint));
        return subcurves;
    }


    ArrayList<substroke> splitByLoss(substroke str){
        int indexOfSplit =str.startpoint+5;

        for(int i= indexOfSplit; i< str.endpoint-5;i++){
            if(this.ratio.get(i) > this.ratio.get(indexOfSplit)){
                indexOfSplit = i;
            }
        }
        indexOfSplit ++;
        ArrayList<substroke> subcurves = new ArrayList<>();
        subcurves.add(new substroke(str.startpoint,indexOfSplit));
        subcurves.add(new substroke(indexOfSplit,str.endpoint));
        return subcurves;
    }

}