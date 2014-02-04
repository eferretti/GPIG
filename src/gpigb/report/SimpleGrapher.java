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
    
    // Coordinate basis set-up
    final int axisXStart;
    final int axisYStart;
    final int axisXEnd;
    final int axisYEnd;
    
    // lineWidth when drawing a line
    int lineWidth = 2;
    // cur_x position for the point which we are currently graphing
    int cur_x = 1;
    
    int avg = 0;
    /**
     * Constructors for SimpleGrapher objects. Draw window used default name
     * @param width - width of the drawing space window
     * @param height - height of the drawing space window
     */
    public SimpleGrapher(int width, int height) {
        this("Simple Grapher", width, height);
    }

    /**
     * Constructors for SimpleGrapher objects. Draw window used default name and dimensions
     */
    public SimpleGrapher() {
        this("Simple Grapher", 800, 500);
    }
    
    /** Constructor for SimpleGraph objects
     * @param title - Title of the current Graph window
     * @param width - width if the window
     * @param height - height of the window
     */
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
    
    /**
     * Draws the axis of the 2D base of the drawing space
     * We are using the conventional coordinate basis - (0,0) is at the bottom left
     * In the default Java coordinate basis (0,0) is at the top left 
     */
    private void drawAxis()
    {
        // draw the axes in red
        theDrawSpace.setForegroundColor(Color.red);
        // Y axis
        theDrawSpace.drawLine(getTrueX(0), axisYStart, getTrueX(0), axisYEnd);
        // X axis
        theDrawSpace.drawLine(axisXStart, getTrueY(0), axisXEnd,  getTrueY(0));
    }
    
    /**
     * Draws the legend for the graph
     */
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
        // Time
        theDrawSpace.setForegroundColor(Color.red);
        theDrawSpace.drawString("Time", axisXEnd - 25, axisYEnd + 15);
        
    }
    
    /** Because Java uses a non-conventional 2D basis, need to convert x of each point
     * @param x - x Coordinate of a specific point
     * @return returns the new x coordinate according to the basis of the 
     * drawing space
     */
    private int getTrueX(int x) {
        return  x + axisXStart;
    }
    
    /** Because Java uses a non-conventional 2D basis, need to convert y of each point
     * @param y - y Coordinate of a specific point
     * @return returns the new y coordinate according to the basis of the 
     * drawing space
     */
    private int getTrueY(int y) {
         return axisYEnd - y;
    }

    /**
     * Draws a point (which is essentially a small vertical bar due to 
     * aesthetic reasons :)
     * @param xPos - x Coordinate of the point
     * @param yPos - y Coordinate of the point
     */
    private void drawPoint(int xPos, int yPos) {
    	theDrawSpace.drawLine(getTrueX(xPos), getTrueY(yPos-2), getTrueX(xPos), getTrueY(yPos));
    }
    
    /**
     * Draws a vertical bar to represent a point on the drawing space
     * @param xPos - x Coordinate of the point
     * @param yPos - y Coordinate of the point
     */
    private void drawBar(int xPos, int yPos) {
        theDrawSpace.drawLine(getTrueX(xPos), getTrueY(0), getTrueX(xPos), getTrueY(yPos));
    }
    
    /**
     * Plots the specified data record (usually used for real-time monitoring)
     * @param record - data record
     */
    public void plotData(SensorRecord<?> record) {        
            int dataPoint = (Integer)record.getData();
    		theDrawSpace.setForegroundColor(Color.gray);
            drawBar(cur_x, dataPoint);
            theDrawSpace.setForegroundColor(Color.black);
            drawPoint(cur_x, dataPoint);
            
            //Plot Average - probably shouldn't be estimated here
            avg += dataPoint;
            theDrawSpace.setForegroundColor(Color.blue);
            drawBar(cur_x, avg/cur_x);
            
            cur_x += lineWidth;
    }
    
    /** Plots the specified datastream (set of records)
     * @param recordSet - set of records
     */
    public void plotData(RecordSet<?> recordSet) {
        for( int i = 0 ; i < recordSet.getRecordCount() ; i++) {
            plotData(recordSet.getReadingAtPosition(i));
        }
    }
    
     /**
     * Clears the drawing space
     */
    public void clear() {
        theDrawSpace.erase();
        cur_x = 1;
    }
}
