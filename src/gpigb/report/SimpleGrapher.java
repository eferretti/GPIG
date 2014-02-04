package gpigb.report;

import gpigb.data.*;
import java.awt.*;


/* works only with ConcreteSensorOne atm - need to make the scaling of the space proper (in order to do that need possible max
 * and min values)
 */

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
    
    //font
    final String font = "Serif";
    final int font_small = 10;
    final int font_big = 15;
    
    final int height;
    final int width;
    
    // lineWidth when drawing a bar
    int lineWidth = 3;
    
    // cur_x position for the point which we are currently graphing
    int cur_x = 1;
    
    //statistics
    private int min = Integer.MAX_VALUE;
    private int max = Integer.MIN_VALUE;
    private int avg = 0;
    private int sum = 0;
    private int count = 0;
    private int prev = 0;
    
    int printMaxAt;
    int printMinAt;
    
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
        
        this.width = width;
        this.height = height;
        
        axisXStart = (int) (width*0.05);
        axisXEnd   = (int) (width*0.95);
        axisYStart = (int) (height*0.3);
        axisYEnd   = (int) (height*0.7);
        
        printMaxAt = axisYStart -10;
        printMinAt = axisYEnd + 55;
        
        setFontBig();
        this.drawAxis();
        this.drawLegend();
        theDrawSpace.setLineWidth(lineWidth);
        
        
    }
    
    private void setToDefault(){
    	// lineWidth when drawing a line
        lineWidth = 4;
        // cur_x position for the point which we are currently graphing
        cur_x = 1;
        min = Integer.MAX_VALUE;
        max = Integer.MIN_VALUE;
        sum = 0;
        avg = 0;
        count = 0;
        prev = 0;
    }
    
    /**
     * Draws the axis of the 2D base of the drawing space
     * We are using the conventional coordinate basis - (0,0) is at the bottom left
     * In the default Java coordinate basis (0,0) is at the top left 
     */
    private void drawAxis()
    {
    	theDrawSpace.setLineWidth(2);
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
        theDrawSpace.drawLine((int)(width*0.82), (int)(height*0.1), (int)(width*0.85), (int)(height*0.1));
        theDrawSpace.drawString("Sensor Value", (int)(width*0.86), (int)(height*0.11));
        theDrawSpace.setForegroundColor(Color.black);
        theDrawSpace.drawLine((int)(width*0.845), (int)(height*0.1), (int)(width*0.85), (int)(height*0.1));
        
        // Average Value
        theDrawSpace.setForegroundColor(Color.blue);
        theDrawSpace.drawLine((int)(width*0.82), (int)(height*0.125), (int)(width*0.85),  (int)(height*0.125));
        theDrawSpace.drawString("Avg Value", (int)(width*0.86), (int)(height*0.135));
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
    
    private void setFontSmall(){
    	theDrawSpace.setFont(new Font(font, Font.BOLD, font_small));
    }
    
    private void setFontBig(){
    	theDrawSpace.setFont(new Font(font, Font.BOLD, font_big));
    }
    
    private void replaceString(String oldStr, String newStr, int x, int y, Color color){
    	theDrawSpace.eraseString(oldStr, x, y);
    	theDrawSpace.setForegroundColor(color);
    	theDrawSpace.drawString(newStr, x, y);
    }
    /**
     * Plots the specified data record (usually used for real-time monitoring)
     * @param record - data record
     */
    public void plotData(SensorRecord<?> record) {        
            int dataPoint = Math.round((float)Math.floor(Double.valueOf(record.getData().toString())));
    		//Draw data Bar
            theDrawSpace.setForegroundColor(Color.gray);
            drawBar(cur_x, dataPoint);
            
            //Plot Average bar - probably shouldn't be estimated here
            theDrawSpace.eraseString("global avg: " + avg, (int)(width*0.82), (int)(height*0.20));
            sum += dataPoint;
            avg = sum / ++count;
            
            theDrawSpace.setForegroundColor(Color.blue);
            drawBar(cur_x, avg);
            theDrawSpace.drawString("global avg: " + avg, (int)(width*0.82), (int)(height*0.20));
            
            //Plot data point
            theDrawSpace.setForegroundColor(Color.black);
            drawPoint(cur_x, dataPoint);
            
            
            if(dataPoint < min){
            	replaceString("global min: " + min, "global min: " + dataPoint, (int)(width*0.82), (int)(height*0.17), Color.black);
            	min = dataPoint;
            	
            	String val = "" + min;
            	setFontSmall();
            	theDrawSpace.drawString(val, getTrueX(cur_x - (int)(val.length()*1.5)),  printMinAt+=10);
            	setFontBig();
            }
            
            if(dataPoint > max){
            	replaceString("global max: " + max, "global max: " + dataPoint, (int)(width*0.82), (int)(height*0.23), Color.black);
            	max = dataPoint;

            	theDrawSpace.setFont(new Font("Serif", Font.BOLD, 4));
            	
            	String val = "" + max;
            	setFontSmall();
            	theDrawSpace.drawString(val, getTrueX(cur_x - (int)(val.length()*1.5)), printMaxAt-=10);
            	setFontBig();
            }
            
            cur_x += lineWidth;
            
            replaceString("current val: " + prev, "current val: " + dataPoint, (int)(width*0.82), (int)(height*0.26), Color.gray);
            prev = dataPoint;       
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
        setToDefault();
    }
}
