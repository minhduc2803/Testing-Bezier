package io.practice;

import org.math.plot.*;

import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String [] args) {

        double[][] stroke = {{ 857.,853.},
                { 825.,799.},
                { 825.,799.},
                { 825.,799.},
                { 825.,799.},
                { 825.,799.},
                { 825.,799.},
                { 795.,743.},
                { 795.,743.},
                { 795.,743.},
                { 795.,743.},
                { 795.,743.},
                { 795.,743.},
                { 830.,685.},
                { 830.,685.},
                { 830.,685.},
                { 830.,685.},
                { 888.,657.},
                { 888.,657.},
                { 888.,657.},
                { 942.,655.},
                { 942.,655.},
                { 942.,655.},
                {1005.,671.},
                {1005.,671.},
                {1005.,671.},
                {1059.,705.},
                {1059.,705.},
                {1059.,705.},
                {1059.,705.},
                {1103.,761.},
                {1103.,761.},
                {1103.,761.},
                {1103.,761.},
                {1098.,819.},
                {1098.,819.},
                {1098.,819.},
                {1098.,819.},
                {1098.,819.},
                {1040.,869.},
                {1040.,869.},
                {1040.,869.},
                {1040.,869.},
                {1040.,869.},
                { 980.,888.},
                { 980.,888.},
                { 980.,888.},
                { 980.,888.},
                { 980.,888.},
                { 980.,888.},
                { 980.,888.},
                { 980.,888.},
                { 980.,888.},
                { 980.,888.},
                {1031.,887.},
                {1031.,887.},
                {1031.,887.},
                {1084.,897.},
                {1084.,897.},
                {1084.,897.},
                {1140.,920.},
                {1140.,920.},
                {1140.,920.},
                {1194.,957.},
                {1194.,957.},
                {1194.,957.},
                {1194.,957.},
                {1238., 1024.},
                {1238., 1024.},
                {1238., 1024.},
                {1240., 1083.},
                {1240., 1083.},
                {1240., 1083.},
                {1205., 1139.},
                {1205., 1139.},
                {1205., 1139.},
                {1142., 1180.},
                {1142.,1180.},
                {1142., 1180.},
                {1070. ,1209.},
                {1070. ,1209.},
                {1070., 1209.},
                {1007., 1213.},
                {1007., 1213.},
                {1007., 1213.}};
        preprocess preprocess = new preprocess();
        ArrayList<double[]> rawstroke = new ArrayList<>();

        for(int i =0; i<stroke.length;i++) {
            double[] point = stroke[i];
            rawstroke.add(point);
        }
        mathplot plot = new mathplot();


        List<ArrayList<double[]>> rawallstroke = new ArrayList<>();
        rawallstroke.add(rawstroke);
        List<Stroke>  AllStrokeBoject = preprocess.DataObject(rawallstroke);
        int k = AllStrokeBoject.get(0).datapoint.size();
        substroke fsub = new substroke(0,k-1);
        ArrayList<substroke> be = AllStrokeBoject.get(0).findBezier(fsub);


        Plot2DPanel plot2db= new Plot2DPanel();

        for(int i=0;i<be.size();i++) {
            double[][] p = be.get(i).B.controlpoint;

            plot.addline(p, plot2db);

        }
        plot.show(plot2db, "1");
    }
}