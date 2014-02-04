package gpigb.report;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;

import java.util.Random;

public class SmartGrapher
{
    private static final Color LIGHT_GRAY = new Color(0, 0, 0, 40);

    private  JFrame frame;
    private  GraphPanel graph;
    private  static final int Default_startMaxY =  300;
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
    public SmartGrapher(int width, int height, int startMax)
    {
        if (frame == null) {
            frame = makeFrame(width, height, startMax);
        }
        else {
            graph.newRun();
        }
    }
    
    public SmartGrapher(int width, int height)
    {
        this(width, height, Default_startMaxY);
    }
    
    public SmartGrapher()
    {
        this(Default_width, Default_height, Default_startMaxY);
    }
    
    
    
    public void reset()
    {
        graph.newRun();
    }
    
    public void plot(int data){
        graph.update(data);
    }
    
    public void generateAndRun(){
        int[] data = new int[2000];
        Random g = new Random();
        
        for(int i = 0 ; i < 2000 ; i ++)
            data[i] = g.nextInt(2000);
        
        for(int i = 0 ; i < 2000 ; i++){
            graph.update(data[i]);
            wait(100);
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
    
    private JFrame makeFrame(int width, int height, int startMax)
    {
        JFrame frame = new JFrame("Graph View");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        Container contentPane = frame.getContentPane();

        graph = new GraphPanel(width, height, startMax);
        contentPane.add(graph, BorderLayout.CENTER);
        /*
        JPanel bottom = new JPanel();
        bottom.add(new JLabel("Step:"));
        stepLabel = new JLabel("");
        bottom.add(stepLabel);
        countLabel = new JLabel(" ");
        bottom.add(countLabel);
        contentPane.add(bottom, BorderLayout.SOUTH);
        */
        frame.pack();
        frame.setLocation(20, 400);

        frame.setVisible(true);

        return frame;
    }

}

