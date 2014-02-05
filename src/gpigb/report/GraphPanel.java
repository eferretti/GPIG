package gpigb.report;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

class GraphPanel extends JComponent
    {
        private static final double SCALE_FACTOR = .8;
        private int SCALE_TIMES = 0;
        // An internal image buffer that is used for painting. For
        // actual display, this image buffer is then copied to screen.
        private BufferedImage graphImage;
        private static final double yAxisHeight = .7;
        private int yMax;
        private int yMin;
        private static final int axisYgran = 20;
        private static final Color SHADOW = new Color(0, 0, 0, 40);
        private static final Color DARK_GREEN = new Color(0, 128, 0);
        private static final Color DATA_BAR = Color.green;
        private static final Color DATA_POINT = new Color(51, 25, 0);
        
        private static final Color STATS_COLOR = new Color(192, 192, 192);
        private static final Color AVG_COLOR = new Color(51, 51, 0);
        private static final Color BG_COLOR = Color.black;
        private static final Color AXIS_COLOR = DARK_GREEN;
        
        private static final int DistFromRightBoarder = 20;
        
        private int cur = 0;
        private int min = Integer.MAX_VALUE;
        private int max = Integer.MIN_VALUE;
        private int count = 0;
        private int sum = 0;
        private int avg = 0;
     
        public GraphPanel(int width, int height)
        {
            graphImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            clear();
            drawLegend();
        }
        
        public void setToDefault(){
            int height = graphImage.getHeight();
            yMin = (int)(height* .1);
            yMax = (int)(height* .95);
            cur = 0;
            min = Integer.MAX_VALUE;
            max = Integer.MIN_VALUE;
            SCALE_TIMES = 0;
            count = 0;
            sum = 0;
            avg = 0;
        }
        
        /* no no really math is really fun... I hate my life*/
        private void drawAxis(){
            Graphics g = graphImage.getGraphics();
            int height = graphImage.getHeight();
            
            g.setColor(AXIS_COLOR);
            int set_x = 6;
            int cur_y = (int)(height*.05);
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
        
        g.setColor(BG_COLOR); 
        g.fillRect(0, 0, 40, height);
        g.setColor(SHADOW); /* AWESOME SHADOW BLURRY BUGGY THINGY - nono its a feature */
        g.fillRect(0, 40, 60, height);

       }
       
       private void eraseLegend(){
            Graphics g = graphImage.getGraphics();
            int height = graphImage.getHeight();
            int width  = graphImage.getWidth();
            g.setColor(BG_COLOR);
            
            g.fillRect((int)(width*.85), (int)(height*.05), (int)(width*.015), (int)(height*.015));
            g.drawString("Data value", (int)(width*.87), (int)(height*(.05 + .015)));

            g.fillRect((int)(width*.85), (int)(height*.073), (int)(width*.015), (int)(height*.015));
            g.drawString("Avrg value", (int)(width*.87), (int)(height*(.073 + .015)));

        }
       
       private void drawLegend(){
            Graphics g = graphImage.getGraphics();
            int height = graphImage.getHeight();
            int width  = graphImage.getWidth();
            
            g.setColor(DATA_BAR);
            g.drawString("Data value", (int)(width*.87), (int)(height*(.05 + .015)));
            g.fillRect((int)(width*.85), (int)(height*.05), (int)(width*.015), (int)(height*.015));
            g.setColor(DATA_POINT);
            g.fillRect((int)(width*.86), (int)(height*.05), (int)(width*.005), (int)(height*.015));
            

            g.setColor(AVG_COLOR);
            g.fillRect((int)(width*.85), (int)(height*.073), (int)(width*.015), (int)(height*.015));
            g.drawString("Avrg value", (int)(width*.87), (int)(height*(.073 + .015)));
           
        }
        
        private void drawStats(){
            Graphics g = graphImage.getGraphics();
            int height = graphImage.getHeight();
            int width  = graphImage.getWidth();
            
            g.setColor(STATS_COLOR);
            g.drawString("cur: " + cur, (int)(width*.37), (int)(height*(.05 + .015)));
            g.drawString("min: " + min, (int)(width*.45), (int)(height*(.05 + .015)));
            g.drawString("avg: " + avg, (int)(width*.53), (int)(height*(.05 + .015)));
            g.drawString("max: " + max, (int)(width*.61), (int)(height*(.05 + .015)));
        }
        
        private void eraseStats(){
            Graphics g = graphImage.getGraphics();
            int height = graphImage.getHeight();
            int width  = graphImage.getWidth();
            
            g.setColor(BG_COLOR);
            g.drawString("cur: " + cur, (int)(width*.37), (int)(height*(.05 + .015)));
            g.drawString("min: " + min, (int)(width*.45), (int)(height*(.05 + .015)));
            g.drawString("avg: " + avg, (int)(width*.53), (int)(height*(.05 + .015)));
            g.drawString("max: " + max, (int)(width*.61), (int)(height*(.05 + .015)));
        }
        
        
        /**
         * Dispay a new point of data.
         */
        public void update(int newData)
        {
                eraseStats();
                
                cur = newData;
                sum += newData;
                count ++;
                avg = sum / count;
                if(newData < min)min = newData;
                if(newData > max)max = newData;
                
                Graphics g = graphImage.getGraphics();

                int height = graphImage.getHeight();
                int width = graphImage.getWidth();
                eraseLegend();
                
                // move graph one pixel to left
                g.copyArea(1, 0, width - 1, height, -1, 0);
                
                // calculate y for the data point, check whether it's out of screen. 
                // scale down if necessary.
                int y = (int)(height*yAxisHeight - newData*Math.pow(SCALE_FACTOR,SCALE_TIMES));
                int y_avg  = (int)(height*yAxisHeight - avg*Math.pow(SCALE_FACTOR,SCALE_TIMES));
                while (y < yMin || y > yMax) {
                    zoomOut();
                    SCALE_TIMES++;
                    y = (int)(height*yAxisHeight - newData*Math.pow(SCALE_FACTOR,  SCALE_TIMES));
                    y_avg  = (int)(height*yAxisHeight - avg*Math.pow(SCALE_FACTOR, SCALE_TIMES));
                }
                g.setColor(DATA_BAR);
                g.drawLine(width - DistFromRightBoarder, y, 
                           width - DistFromRightBoarder, (int)(height*yAxisHeight));

                g.setColor(AVG_COLOR);
                g.drawLine(width - DistFromRightBoarder, y_avg, 
                           width - DistFromRightBoarder, (int)(height*yAxisHeight));
                
                int sign = (int)Math.signum(newData);
                int scaled_length = Math.abs((int)(height*yAxisHeight - y));
                g.setColor(DATA_POINT);
                g.drawLine(width - DistFromRightBoarder, y, 
                           width - DistFromRightBoarder, (int)(y + sign*scaled_length*0.05));
                eraseAxis();
                drawAxis();
                
                drawStats();
                drawLegend();
                repaintNow();
        }

        
            
        
        /**
         * Scale the current graph down vertically to make more room at the top or lefty.
         * Should have taken Advanced Computer Vision and probably use the Affine 
         * transformations provided in Java ..
         */
        private void zoomOut() {
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
            setToDefault();
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
