package io.practice;

import org.ojalgo.matrix.Primitive64Matrix;

import java.util.ArrayList;

public class Optimize {
    double [] sinit;//[1,s,s2,s3]
    double[][] S;
    int N;
    int epoch=6;
    int col =4;
    double[][] invB;
    double[][] cofficience;

    Optimize(int N){
        this.N = N;
        this.invB = new double[4][4];
        this.sinit = new double[N];
        createSinit(N);
//        ops.printsinit(sinit);
        invertmatrixB();

        this.S = new double [N][this.col];
        this.cofficience = new double[this.col][2];

    }
    void  createSinit(int n){

        double si;
        for(int i=0; i<n ; i++){
            si = (double) i/(n-1);
            this.sinit[i]  = si;
        }
//        ops.printsinit(sinit);
    }

    void createSmatrix(){
        for(int i=0; i<N ; i++){
            double si = sinit[i];
            for(int j=0; j<col;j++){
                S[i][j] = Math.pow(si,j);
            }
        }
//        ops.prints(S);
//        System.out.println("________________________________");
    }
    void invertmatrixB(){
        double[][] B = {{1,0,0,0},
                {-3,3,0,0},
                {3,-6,3,0},
                {-1,3,-3,1}};
        Primitive64Matrix.Factory matrixFactory = Primitive64Matrix.FACTORY;
        Primitive64Matrix matrixB = matrixFactory.rows(B);
        Primitive64Matrix invb = matrixB.invert();

        for(int i=0; i<4;i++){
            for(int j=0;j<4;j++){
                invB[i][j] = invb.get(i,j);
            }
        }
//        ops.prints(invB);
    }
    double[][] invertmatrix(){
        double[][] pinvS =new double[this.col][this.N];
        Primitive64Matrix.Factory matrixFactory = Primitive64Matrix.FACTORY;
        Primitive64Matrix matrixS = matrixFactory.rows(S);
        Primitive64Matrix pseudoinv = matrixS.invert();
        for(int i=0; i<col;i++){
            for(int j=0;j<N;j++){
                pinvS[i][j] = pseudoinv.get(i,j);
//                System.out.println(pinvS[i][j]);
            }
        }
//        ops.prints(pinvS);
        return pinvS;
    }
    void findcof(ArrayList<double[]> datapoint , substroke str){
        double[][] pinvS= invertmatrix();
        for(int i=0 ; i <col; i++){
            double t1=0;
            double t2=0;
            for(int j=0;j<N;j++){
                t1 += pinvS[i][j]*datapoint.get(str.startpoint+j)[0];
                t2 += pinvS[i][j]*datapoint.get(str.startpoint+j)[1];

            }
            this.cofficience[i][0]=t1;
            this.cofficience[i][1]=t2;

        }
    }
    double firstDivSecondOrder(substroke str,ArrayList<double[]> datapoint, double si, int i){
        double f1,f2;
        double xb=0,yb=0;
        double x1b=0,y1b=0;
        double x2b=0,y2b=0;
        double [] s1i = {0,1,2*si,3*si*si};
        double [] s2i = {0,0,2,6*si};
        for(int j=0;j<this.col;j++){
            xb += Math.pow(si,j)*cofficience[j][0];
            yb +=  Math.pow(si,j)*cofficience[j][1];
            x1b += s1i[j]*cofficience[j][0];
            y1b += s1i[j]*cofficience[j][1];
            x2b += s2i[j]*cofficience[j][0];
            y2b += s2i[j]*cofficience[j][1];
        }
        f1 = x1b*(datapoint.get(str.startpoint+i)[0]-xb) + y1b*(datapoint.get(str.startpoint+i)[1]-yb);
        f2 = x2b*(datapoint.get(str.startpoint+i)[0]-xb)-x1b*x1b + y2b*(datapoint.get(str.startpoint+i)[1]-yb)-y1b*y1b;
        return f1/f2;

    }
    void  NewtonMethod(substroke str,ArrayList<double[]> datapoint){
        for(int i=0; i<N; i++){
            sinit[i] = sinit[i] - firstDivSecondOrder(str,datapoint,sinit[i],i);
        }

    }
    double computeLoss(substroke str,ArrayList<double[]> datapoint){
//            double xb,yb;
        double loss = 0;
        for(int i=0; i<N;i++){
            double xb = 0;
            double yb = 0;

            for(int j=0; j<this.col;j++){
                xb += cofficience[j][0]*Math.pow(sinit[i],j);
                yb += cofficience[j][1]*Math.pow(sinit[i],j);
            }
            loss += Math.pow(xb-datapoint.get(str.startpoint+i)[0],2)+ Math.pow(yb-datapoint.get(str.startpoint+i)[1],2);

        }
//        loss = loss / N;
        return loss/(2*N);
    }
    double[][] findControlPoint(){
        double[][] controlPoint = new double[4][2];
        for(int i=0;i<4;i++){
            double p1=0,p2=0;
            for(int j=0; j<this.col;j++){
                p1+=cofficience[j][0]*invB[i][j];
                p2+=cofficience[j][1]*invB[i][j];
            }
            controlPoint[i][0] = p1;
            controlPoint[i][1] = p2;
        }
        return controlPoint;
    }
    Bezier findBezierfunction(substroke str, ArrayList<double[]> datapoint){
        for(int i = 0; i<epoch;i++){
            createSmatrix();
            findcof( datapoint, str);
            for (int j =0; j<35;j++){
                NewtonMethod(str, datapoint);
            }
        }
        double loss = computeLoss(str, datapoint);
//        System.out.println(loss);
        double[][] controlpoint = findControlPoint();

        return new Bezier(controlpoint,loss);
    }

}