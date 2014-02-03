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
    
    final int axisXStart;
    final int axisYStart;
    final int axisXEnd;
    final int axisYEnd;
    int lineWidth = 2;
    final int pointR = 2;
    int cur_x = 1;
    
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
        axisXStart = (int) (width*0.05);
        axisXEnd   = (int) (width*0.95);
        axisYStart = (int) (height*0.05);
        axisYEnd   = (int) (height*0.7);
        this.drawAxis();
        this.drawLegend();
        theDrawSpace.setLineWidth(lineWidth);
    }

    private void drawAxis()
    {
        // draw the axes in red
        theDrawSpace.setForegroundColor(Color.red);
        // Y axis
        theDrawSpace.drawLine(getTrueX(0), axisYStart, getTrueX(0), axisYEnd);
        // X axis
        theDrawSpace.drawLine(axisXStart, getTrueY(0), axisXEnd,  getTrueY(0));
    }
    
    private void drawLegend()
    {
    	theDrawSpace.setLineWidth(3);
    	// Sensor Value
        theDrawSpace.setForegroundColor(Color.gray);
        theDrawSpace.drawLine(axisXEnd - 70, axisYStart, axisXEnd - 50, axisYStart);
        theDrawSpace.drawString("Sensor Value", axisXEnd - 45, axisYStart + 5);
        // Average Value
        theDrawSpace.setForegroundColor(Color.blue);
        theDrawSpace.drawLine(axisXEnd - 70, axisYStart + 15, axisXEnd - 50,  axisYStart + 15);
        theDrawSpace.drawString("Avg Value", axisXEnd - 45, axisYStart + 20);

    }
    
    private int getTrueX(int x) {
        return  x + axisXStart;
    }
    
    private int getTrueY(int y) {
         return axisYEnd - y;
    }
    /* Point is essentially a small vertical bar - looks prettier */
    private void drawPoint(int xPos, int yPos) {
    	theDrawSpace.drawLine(getTrueX(xPos), getTrueY(yPos-2), getTrueX(xPos), getTrueY(yPos));
    }
    
    private void drawBar(int xPos, int yPos) {
        theDrawSpace.drawLine(getTrueX(xPos), getTrueY(0), getTrueX(xPos), getTrueY(yPos));
    }
    
    public void plotData(SensorRecord<?> record) {        
            int dataPoint = (Integer)record.getData();
    		theDrawSpace.setForegroundColor(Color.gray);
            drawBar(cur_x, dataPoint);
            theDrawSpace.setForegroundColor(Color.black);
            drawPoint(cur_x, dataPoint);
            cur_x += lineWidth;
    }
    
    public void plotData(RecordSet<?> recordSet) {
        for( int i = 0 ; i < recordSet.getRecordCount() ; i++) {
            plotData(recordSet.getReadingAtPosition(i));
        }
    }
    
     public void clear() {
        theDrawSpace.erase();
        cur_x = 1;
    }
}
