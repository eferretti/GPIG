package gpigb.report;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

import java.util.Random;
import java.lang.Math;

import gpigb.data.*;

public class SmartGrapher
{
    
    private  JFrame frame;
    private  GraphPanel graph;
    
    private  static final int Default_width     =  800;
    private  static final int Default_height    =  500;
    
    private static final Color LIGHT_GRAY = new Color(0, 0, 0, 40);
    private static final Color DATA_COLOR = Color.green;
    private static final Color AVG_COLOR = Color.blue;
    private static final Color BG_COLOR = Color.black;
    private static final int DistFromRightBoarder = 20;
    private static final int axisYgran = 20;
        

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
    
    
    public void plot(SensorRecord<?> record){
    	int dataPoint = Math.round((float)Math.floor(Double.valueOf(record.getData().toString())));
    	graph.update(dataPoint);
    }
    
    public void plot(RecordSet<?> recordSet) {
        for( int i = 0 ; i < recordSet.getRecordCount() ; i++) {
            plot(recordSet.getReadingAtPosition(i));
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
            else if (i<2000)data[i] = 1500 - g.nextInt(3000);
        }
        
        for(int i = 0 ; i < 2000 ; i++){
            graph.update(data[i]);
            wait(15);
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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        Container contentPane = frame.getContentPane();
        
        graph = new GraphPanel(width, height);
        contentPane.add(graph, BorderLayout.CENTER);
        
        frame.pack();
        frame.setLocation(20, 400);

        frame.setVisible(true);
        
        return frame;
    }
    

    class GraphPanel extends JComponent
    {
        private static final double SCALE_FACTOR = .8;
        private int SCALE_TIMES = 0;
        // An internal image buffer that is used for painting. For
        // actual display, this image buffer is then copied to screen.
        private BufferedImage graphImage;
        private static final double yAxisHeight = .6;
        private int yMax;
        private int yMin;
        
        private int min = Integer.MAX_VALUE;
        private int max = Integer.MIN_VALUE;
        private int count = 0;
        private int sum = 0;
        
        
        /**
         * Create a new, empty GraphPanel.
         */
        public GraphPanel(int width, int height)
        {
            graphImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            clear();
            yMin = (int)(height* .05);
            yMax = (int)(height* .95);
            drawAxis();
        }
        
        /* no no really math is really fun... */
        private void drawAxis(){
            Graphics g = graphImage.getGraphics();
            int height = graphImage.getHeight();
            
            g.setColor(Color.red);
            int set_x = 6;
            int cur_y = (int)(height*0.05);
            int change_y = (int)((height*.95 - height*.05)/axisYgran);
            
            for(int i = 0 ; i <= axisYgran; i ++){
                int toPrint = (int)((height*yAxisHeight - cur_y)/Math.pow(SCALE_FACTOR,SCALE_TIMES));
                g.drawString("" + toPrint, set_x, cur_y + 1);
                cur_y += change_y;
            }   
        }
        
        private void eraseAxis(){
        Graphics g = graphImage.getGraphics();
        int height = graphImage.getHeight();
        
        g.setColor(LIGHT_GRAY); /* LULZ KELW */
        g.fillRect(0, 0, 50, height);

    }
        
        
        /**
         * Dispay a new point of data.
         */
        public void update(int newData)
        {
                
                sum += newData;
                count ++;
                int avg = sum / count;
                if(newData < min){min = newData;System.out.println("min: " + min);}
                if(newData > max){max = newData;System.out.println("max: " + max);}
                
                Graphics g = graphImage.getGraphics();

                int height = graphImage.getHeight();
                int width = graphImage.getWidth();
                
                // move graph one pixel to left
                g.copyArea(1, 0, width - 1, height, -1, 0);
                
                // calculate y for the data point, check whether it's out of screen. 
                // scale down if necessary.
                int y = (int)(height*yAxisHeight - newData*Math.pow(SCALE_FACTOR,SCALE_TIMES));
   
                while (y < yMin || y > yMax) {
                    zoomOut();
                    y = (int)(height*yAxisHeight - newData*Math.pow(SCALE_FACTOR,++SCALE_TIMES));
                }
                g.setColor(DATA_COLOR);
                g.drawLine(width - DistFromRightBoarder, y, 
                           width - DistFromRightBoarder, (int)(height*yAxisHeight));
                
                eraseAxis();
                drawAxis();
                repaintNow();
        }

        /**
         * Scale the current graph down vertically to make more room at the top or lefty.
         * Should have taken Advanced Computer Vision and probably use the Affine 
         * transformations provided in Java ..
         */
        public void zoomOut() {
            Graphics g = graphImage.getGraphics();
            int height = graphImage.getHeight();
            int width = graphImage.getWidth();            
            
            BufferedImage tmpImage = new BufferedImage(width, (int)(height*SCALE_FACTOR),                                                        BufferedImage.TYPE_INT_RGB);
            Graphics2D gtmp = (Graphics2D) tmpImage.getGraphics();
            //scale the Y axis by the SCALE_FACTOR
            gtmp.scale(1., SCALE_FACTOR);
            //make exact copy of graphImage but scaled with respect of the Y axis
            gtmp.drawImage(graphImage, 0, 0, null);
            // math is fun... not
            int YmoveTo = (int) ( yAxisHeight * height  * (1. - SCALE_FACTOR) + 1);
            //delete the entire image
            g.setColor(BG_COLOR);
            g.fillRect(0, 0, width, height);
            
            /* Draws as much of the specified image as is currently available. 
             * The image is drawn with its top-left corner at (0, oldTop) in the 
             * g graphics context's coordinate space.
             */
            g.drawImage(tmpImage, 0, YmoveTo, null);
            repaintNow();
        }

        /**
         * Cause immediate update of the panel.
         */
        public void repaintNow()
        {
            paintImmediately(0, 0, graphImage.getWidth(), graphImage.getHeight());
        }

        /**
         * Clear the image on this panel.
         */
        public void clear()
        {
            Graphics g = graphImage.getGraphics();
            g.setColor(BG_COLOR);
            g.fillRect(0, 0, graphImage.getWidth(), graphImage.getHeight());
            repaintNow();
        }

        // The following methods are redefinitions of methods
        // inherited from superclasses.

        /**
         * Tell the layout manager how big we would like to be.
         * (This method gets called by layout managers for placing
         * the components.)
         * 
         * @return The preferred dimension for this component.
         */
        public Dimension getPreferredSize(){
            return new Dimension(graphImage.getWidth(), graphImage.getHeight());
        }

        /**
         * This component is opaque.
         */
        public boolean isOpaque(){
            return true;
        }

        /**
         * This component needs to be redisplayed. Copy the internal image 
         * to screen. (This method gets called by the Swing screen painter 
         * every time it want this component displayed.)
         * 
         * @param g The graphics context that can be used to draw on this component.
         */
        public void paintComponent(Graphics g) {
            if(graphImage != null) {
                g.drawImage(graphImage, 0, 0, null);
            }
        }
    }

}

