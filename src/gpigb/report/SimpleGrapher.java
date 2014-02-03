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
 * Class SimpleGrapher - 
 */

public class SimpleGrapher   
{
    private DrawSpace theDrawSpace;
    final int axisSurface = 15;
    final int pointR = 2;
    int cur_x = 0;
    
    /**
     * Constructors for SimpleGrapher objects. Creates a fresh draw space and makes it visible.
     */
    public SimpleGrapher(int width, int height) {
        this("Simple Grapher", width, height);
    }
     
    public SimpleGrapher() {
        this("Simple Grapher", 800, 500);
    }
    
    public SimpleGrapher(String title, int width, int height) {
        theDrawSpace = new DrawSpace(title, width, height);
        theDrawSpace.setVisible(true);
        this.drawAxis();
    }

    private void drawAxis()
    {
        int height = theDrawSpace.getSize().height;
        int width = theDrawSpace.getSize().height;
        // draw the axes in red
        theDrawSpace.setForegroundColor(Color.red);
        // Y axis
        theDrawSpace.drawLine(axisSurface, axisSurface, axisSurface, height - axisSurface);
        // X axis
        theDrawSpace.drawLine(axisSurface, height - axisSurface, width - axisSurface, height - axisSurface);
    }
    
    private int getTrueX(int x) {
        return x + axisSurface;
    }
    
    private int getTrueY(int y) {
         int height = theDrawSpace.getSize().height;
         return height - axisSurface - y;
    }
    
    private void drawPoint(int xPos, int yPos) {
        theDrawSpace.fill(new Ellipse2D.Double (getTrueX(xPos), getTrueY(yPos) - pointR , pointR, pointR));
    }
    
    private void drawBar(int xPos, int yPos) {
        theDrawSpace.drawLine(getTrueX(xPos), getTrueY(0), getTrueX(xPos), getTrueY(yPos));
    }
    
    public void plotData(SensorRecord<?> record) {        
            theDrawSpace.setForegroundColor(Color.gray);
            drawBar(cur_x, (Integer) record.getData());
            theDrawSpace.setForegroundColor(Color.black);
            drawPoint(cur_x++, (Integer) record.getData());
    }
    
    public void plotData(RecordSet<?> recordSet) {
        for( int i = 0 ; i < recordSet.getRecordCount() ; i++) {
            plotData(recordSet.getReadingAtPosition(i));
        }
    }
    
     public void clear() {
        theDrawSpace.erase();
        cur_x = 0;
    }
}
