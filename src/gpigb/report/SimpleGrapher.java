package gpigb.report;

import gpigb.data.*;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;

import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.util.Random;

/**
 * Class SimpleGrapher - a short demonstration showing animation with the 
 * DrawSpace class. 
 */

public class SimpleGrapher   
{
    private DrawSpace myCanvas;
    final int axisSurface = 15;
    final int pointR = 2;
    int cur_x = 0;
    
    /**
     * Create a SimpleGrapher object. Creates a fresh canvas and makes it visible.
     */
    public SimpleGrapher(int width, int height) {
        myCanvas = new DrawSpace("Simple Grapher", width, height);
        myCanvas.setVisible(true);
        this.drawAxis();
    }
    
    public SimpleGrapher() {
        myCanvas = new DrawSpace("Simple Grapher", 800, 500);
        myCanvas.setVisible(true);
    }

    private void drawAxis()
    {
        int height = myCanvas.getSize().height;
        int width = myCanvas.getSize().height;
        // draw the axes
        myCanvas.setForegroundColor(Color.red);
        myCanvas.drawLine(axisSurface, axisSurface, axisSurface, height - axisSurface);
        myCanvas.drawLine(axisSurface, height - axisSurface, width - axisSurface, height - axisSurface);
    }
    
    private int getTrueX(int x) {
        return x + axisSurface;
    }
    
    private int getTrueY(int y) {
         int height = myCanvas.getSize().height;
         return height - axisSurface - y;
    }
    
    private void drawPoint(int xPos, int yPos) {
        myCanvas.fill(new Ellipse2D.Double (getTrueX(xPos), getTrueY(yPos) - pointR , pointR, pointR));
    }
    
    private void drawBar(int xPos, int yPos) {
        myCanvas.drawLine(getTrueX(xPos), getTrueY(0), getTrueX(xPos), getTrueY(yPos));
    }
    
    public void plotData(SensorRecord<?> record) {        
            myCanvas.setForegroundColor(Color.gray);
            drawBar(cur_x, (Integer) record.getData());
            myCanvas.setForegroundColor(Color.black);
            drawPoint(cur_x++, (Integer) record.getData());
    }
    
    public void plotData(RecordSet<?> recordSet) {
        for( int i = 0 ; i < recordSet.getRecordCount() ; i++) {
            plotData(recordSet.getReadingAtPosition(i));
        }
    }
    
     public void clear() {
        myCanvas.erase();
        cur_x = 0;
    }
}
