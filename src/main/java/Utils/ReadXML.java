package Utils;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import io.practice.Stroke;
import io.practice.mathplot;
import io.practice.preprocess;
import io.practice.substroke;
import org.math.plot.Plot2DPanel;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadXML {
    public void XMLtoBezier(String folderName) {
        BufferedWriter bw = null;
        try {
            // create output file
            File outputFile = new File("../output.txt");
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }

            FileWriter fw = new FileWriter(outputFile);
            bw = new BufferedWriter(fw);

            // read all file in folderName
            File folder = new File(folderName);
            File fileList[] = folder.listFiles();

            for (File file : fileList) {
                // parse each file
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(file);
                doc.getDocumentElement().normalize();
                System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
                NodeList nodeList = doc.getElementsByTagName("traceGroup");
                for (int itr = 0; itr < nodeList.getLength(); itr++) {
                    Node node = nodeList.item(itr);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) node;

                        // write label to output file, label is in tag "Tg_Truth"
                        bw.write(eElement.getElementsByTagName("Tg_Truth").item(0).getTextContent() + "\n");

                        // find bezier curves from dataPoints, dataPoints are in tag "trace"
                        NodeList strokes = eElement.getElementsByTagName("trace");

                        //write number of strokes to output file
                        bw.write(strokes.getLength());
                        bw.write("\n");
                        ArrayList<double[][]> word = new ArrayList<>();
                        for (int i = 0; i < strokes.getLength(); i++) {
                            Node stroke = strokes.item(i);
                            if (stroke.getNodeType() == stroke.ELEMENT_NODE) {
                                Element strokeElement = (Element) stroke;
                                double[][] dataPoints = stringToDoubles(stroke.getTextContent());
                                word.add(dataPoints);


                            }
                        }
                        ArrayList<double[][]> Beziers = findBeziers(word);

                        writeBeziersToFile(bw, Beziers);
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
            } catch (Exception ex) {
                System.out.println("Error in closing the BufferedWriter" + ex);
            }
        }
    }

    public ArrayList<double[][]> findBeziers(ArrayList<double[][]> word) {
        ArrayList<double[][]> Beziers = new ArrayList<>();
        List<ArrayList<double[]>> rawallstroke = new ArrayList<>();
        preprocess Preprocess = new preprocess();
        for (double[][] stroke : word) {


            ArrayList<double[]> rawstroke = new ArrayList<>();

            for (int i = 0; i < stroke.length; i++) {
                double[] point = stroke[i];
                rawstroke.add(point);
            }

            rawallstroke.add(rawstroke);
        }

        List<Stroke> AllStrokeoject = Preprocess.DataObject(rawallstroke);

        ArrayList<substroke> be = new ArrayList<>();
       // System.out.println(AllStrokeoject.size());
        for (int i=0; i<AllStrokeoject.size();i++) {
            int k = AllStrokeoject.get(i).datapoint.size();

            substroke fsub = new substroke(0, k - 1);// index of point in stroke

            be.addAll(AllStrokeoject.get(i).findBezier(fsub));

        }

        for (int i = 0; i < be.size(); i++) {
            double[][] p = be.get(i).B.controlpoint;
            Beziers.add(p);
        }
        return Beziers;
    }


    public double[][] stringToDoubles(String stringPoints) {
        String[] listStringPoints = stringPoints.split(",");
        double[][] dataPoints = new double[listStringPoints.length][2];
        for (int i = 0; i < dataPoints.length; i++) {
            String[] XY = listStringPoints[i].strip().split(" ");
            dataPoints[i][0] = Double.parseDouble(XY[0].strip());
            dataPoints[i][1] = Double.parseDouble(XY[1].strip());
        }
        return dataPoints;
    }

    public double[][] minusY(double[][] data) {
        for (int i = 0; i < data.length; i++) {
            data[i][1] = -data[i][1];
        }
        return data;
    }

    public void writeBeziersToFile(BufferedWriter bw, ArrayList<double[][]> Beziers) throws Exception {
        bw.write(Beziers.size());
        bw.write("\n");
        for (double[][] p : Beziers) {
            for (double x : p[0]) {
                bw.write(String.valueOf(x));
                bw.write(" ");
            }
            bw.write("\n");
            for (double y : p[1]) {
                bw.write(String.valueOf(y));
                bw.write(" ");
            }
            bw.write("\n");
        }
    }

    public void printDouble2D(double[][] dataPoints) {
        for (int i = 0; i < dataPoints.length; i++) {
            for (int j = 0; j < dataPoints[i].length; j++) {
                System.out.print(dataPoints[i][j]);
                System.out.print(" ");
            }
            System.out.print(", ");
        }
        System.out.print("\n");
    }

    ///// main


    public static void main(String argv[]) {
        //XMLtoBezier("../InkData_word");
        ReadXML Sun = new ReadXML();
        try {
            File folder = new File("../InkData_word");
            File fileList[] = folder.listFiles();
            int panel = 0;

            for (int fileIndex = 0; fileIndex < 1; fileIndex++) {

                File file = fileList[fileIndex];
                System.out.println(file.getName());
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(file);
                doc.getDocumentElement().normalize();

                System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

                NodeList nodeList = doc.getElementsByTagName("traceGroup");
                for (int itr = 0; itr < 100; itr++) {
                    Node node = nodeList.item(itr);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) node;
                        mathplot plot = new mathplot();
                        Plot2DPanel plot2db = new Plot2DPanel();
                        Plot2DPanel plot2data= new Plot2DPanel();
                        NodeList strokes = eElement.getElementsByTagName("trace");
                        System.out.println(itr);
                        System.out.println(eElement.getElementsByTagName("Tg_Truth").item(0).getTextContent());

                        ArrayList<double[][]> word = new ArrayList<>();
                        for (int i = 0; i < strokes.getLength(); i++) {
                            Node stroke = strokes.item(i);
                            if (stroke.getNodeType() == stroke.ELEMENT_NODE) {
                                Element strokeElement = (Element) stroke;

                                System.out.print("stroke " + String.valueOf(i) + ": ");

                                double[][] dataPoints = Sun.stringToDoubles(stroke.getTextContent());
                                Sun.minusY(dataPoints);
                                word.add(dataPoints);
                                Sun.printDouble2D(dataPoints);
                                plot2data.addScatterPlot("stroke "+String.valueOf(i)+": ",dataPoints);
                            }
                        }
//                        JFrame frame = new JFrame("plot data: ");
//                        frame.setContentPane(plot2data);
//                        frame.setBounds(100,100,800,600);
//                        frame.setVisible(true);

                        ArrayList<double[][]> Beziers = Sun.findBeziers(word);
                        System.out.println(Beziers.size());
                        for (double[][] p : Beziers) {
                            plot.addline(p, plot2db);
                        }
                        JFrame frame = plot.show(plot2db, "Bezier");
                        Thread.sleep(1000);
                        frame.dispose();

                    }

                }

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}