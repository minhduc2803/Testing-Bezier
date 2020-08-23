package io.practice;

import org.math.plot.Plot2DPanel;

import javax.swing.*;
import java.util.ArrayList;

public class mathplot {
    public void addline(double[][] Pcontrol, Plot2DPanel plot2d) {
        ArrayList<double[]> xy = xybezier(Pcontrol);
        Plotshow(xy, plot2d);
    }

    void Plotshow(ArrayList<double[]> stroke,Plot2DPanel plot2d) {
        double[] x = new double[stroke.size()];
        double[] y = new double[stroke.size()];
        for (int i = 0; i < stroke.size(); i++) {
            x[i] = stroke.get(i)[0];

            y[i] = stroke.get(i)[1];
//            System.out.println(x[i]+" , "+y[i]);

        }
        // create your PlotPanel (you can use it as a JPanel)


        // add a line plot to the PlotPanel
        plot2d.addLinePlot("my plot", x, y);

        // put the PlotPanel in a JFrame, as a JPanel

    }

    ArrayList<double[]> xybezier(double[][] pcontrol) {
        ArrayList<double[]> xy = new ArrayList<>();
        double t = 0;
        for (int i = 0; i < 50; i++) {
            t = (double) i / (49);
//            System.out.println(t);
            double x = 0;
            double y = 0;
            double c;
            for (int j = 0; j < 4; j++) {
                if (j >= 1 && j <= 2) {
                    c = 3;
                } else {
                    c = 1;
                }
                x += c * pcontrol[j][0] * Math.pow((1 - t), (3 - j)) * Math.pow(t, j);
                y += c * pcontrol[j][1] * Math.pow((1 - t), (3 - j)) * Math.pow(t, j);

            }
            double[] point = {x, y};
            xy.add(point);
        }
        return xy;
    }

    public void show(Plot2DPanel plot, String order) {
        JFrame frame = new JFrame("plot number: "+order);
        frame.setContentPane(plot);
        frame.setVisible(true);
    }
}