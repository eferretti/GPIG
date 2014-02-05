package gpigb.report;

import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;

import java.awt.*;

import javax.swing.*;

import java.util.Random;
import java.lang.Math;


public class SmartGrapher
{
    
    private  JFrame frame;
    private  GraphPanel graph;
    
    private  static final int Default_width     =  800;
    private  static final int Default_height    =  500;
    
    
    /**
     * Constructor.
     * 
     * @param width The width of the plotter window (in pixles).
     * @param height The height of the plotter window (in pixles).
     * @param startMax The initial maximum value for the y axis.
     * @param world The world object.
     * @param class1 The first class to be plotted.
     * @param width The second class to be plotted.
     */
    public SmartGrapher(int width, int height)
    {  
        frame = makeFrame(width, height);     
    }
    
    
    public SmartGrapher()
    {
        this(Default_width, Default_height);
    }
    
    
    public static void main(String[] args){
        SmartGrapher sg = new SmartGrapher();
        sg.testRun();
        sg.clear();
        sg.testRun2();
        sg.clear();
        sg.testChunk();
    }
    
    public void plot(int data){
    	graph.update(data);
    }
    
    
    public void plot(int[] data) {
        for( int i = 0 ; i < data.length ; i++) {
            plot(data[i]);
        }
    }
    
    public void testRun2(){
        int[] data = new int[500];
        Random g = new Random();
        
        for(int i = 0 ; i < 500 ; i ++){
            data[i] = g.nextInt(1000);
        }
        
        for(int i = 0 ; i < 500 ; i++){
            plot(data[i]);
            wait(30);
        }
    }

    public void testRun(){
        int[] data = new int[2000];
        Random g = new Random();
        
        for(int i = 0 ; i < 2000 ; i ++){
            if (i < 100)data[i] = 300 - g.nextInt(600);
            else if (i<200)data[i] = 500 - g.nextInt(1000);
            else if (i<400)data[i] = 800 - g.nextInt(1600);
            else if (i<500)data[i] = 1000 - g.nextInt(2000);
            else if (i<1500)data[i] = g.nextInt(3000);
            else if (i<2000)data[i] = 0 - g.nextInt(5000);
        }
        
        for(int i = 0 ; i < 2000 ; i++){
            plot(data[i]);
            wait(15);
        }
    }
    
    public void testChunk(){
        
        Random g = new Random();
        for(int j = 0 ; j < 50 ; j ++){
            int[] data = new int[100];
            for(int i = 0 ; i < 100 ; i ++){
                data[i] = 1000 - g.nextInt(2500);
            }
            plot(data);
            wait(50);
        }
    }
    
    public void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } 
        catch (InterruptedException e) {
            // CATCH A FLU
        }
    }
    
    protected JFrame makeFrame(int width, int height)
    {
        JFrame frame = new JFrame("Graph View");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setResizable(false);
        Container contentPane = frame.getContentPane();
        
        graph = new GraphPanel(width, height);
        contentPane.add(graph, BorderLayout.CENTER);
        
        frame.pack();
        frame.setLocation(50, 250);

        frame.setVisible(true);
        
        return frame;
    }
    
    public void clear(){
        graph.clear();
    }
}

