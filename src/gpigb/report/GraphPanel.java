package gpigb.report;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;

    public class GraphPanel extends JComponent
    {
        private static final double SCALE_FACTOR = 0.3;
        private static final Color DATA_COLOR = Color.green;
        private static final Color AVG_COLOR = Color.blue;
        private static final Color BG_COLOR = Color.black;
        private static final int DistFromRightBoarder = 20;
        private static final int axisYgran = 10;
        
        private static final int[] startCoords = new int[]{50,50};
        private ArrayList<JLabel> yLabels;
        
        // An internal image buffer that is used for painting. For
        // actual display, this image buffer is then copied to screen.
        private BufferedImage graphImage;
        private int lastVal;
        private int yMax;
        
        
        /**
         * Create a new, empty GraphPanel.
         */
        public GraphPanel(int width, int height, int startMax)
        {
            graphImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            clear();
            lastVal = height;
            yMax = startMax;
            
            
        }
        
        public void newRun()
        {

            Graphics g = graphImage.getGraphics();
            
            g.setColor(Color.BLACK);

            repaint();
        }


        private void populateLabels() {
            int labelValue = (int)(yMax * 0.90) / axisYgran;
            for(JLabel label : yLabels){
                label = new JLabel("" + labelValue);
                labelValue*=2;
            }
        }
        
        private void drawAxis(){
            populateLabels();
            Graphics g = graphImage.getGraphics();
        }
        
        /**
         * Dispay a new point of data.
         */
        public void update(int newData)
        {
           
                Graphics g = graphImage.getGraphics();

                int height = graphImage.getHeight();
                int width = graphImage.getWidth();

                // move graph one pixel to left
                g.copyArea(1, 0, width-1, height, -1, 0);

                // calculate y for the data point, check whether it's out of screen. 
                // scale down if necessary.
                int y = height - ((height * newData) / yMax) - 1;
                while (y < 0) {
                    scaleDown();
                    y = height - ((height * newData) / yMax) - 1;
                }
                g.setColor(DATA_COLOR);
                g.drawLine(width - DistFromRightBoarder, y, width - DistFromRightBoarder, height);
                lastVal = y;
                
                
                repaintNow();
                //repaint();
            
        }

        /**
         * Scale the current graph down vertically to make more room at the top.
         */
        public void scaleDown()
        {
            Graphics g = graphImage.getGraphics();
            int height = graphImage.getHeight();
            int width = graphImage.getWidth();

            BufferedImage tmpImage = new BufferedImage(width, (int)(height*SCALE_FACTOR), 
                                                       BufferedImage.TYPE_INT_RGB);
            Graphics2D gtmp = (Graphics2D) tmpImage.getGraphics();
            //scale Y axis
            gtmp.scale(1.0, SCALE_FACTOR);
            gtmp.drawImage(graphImage, 0, 0, null);

            int oldTop = (int) (height * (1.0-SCALE_FACTOR));

            g.setColor(BG_COLOR);
            g.fillRect(0, 0, width, oldTop);
            g.drawImage(tmpImage, 0, oldTop, null);

            yMax = (int) (yMax / SCALE_FACTOR);
            lastVal = oldTop + (int) (lastVal * SCALE_FACTOR);
            //repaint();
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
            //repaint();
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
        public Dimension getPreferredSize()
        {
            return new Dimension(graphImage.getWidth(), graphImage.getHeight());
        }

        /**
         * This component is opaque.
         */
        public boolean isOpaque()
        {
            return true;
        }

        /**
         * This component needs to be redisplayed. Copy the internal image 
         * to screen. (This method gets called by the Swing screen painter 
         * every time it want this component displayed.)
         * 
         * @param g The graphics context that can be used to draw on this component.
         */
        public void paintComponent(Graphics g)
        {
            Dimension size = getSize();
            if(graphImage != null) {
                g.drawImage(graphImage, 0, 0, null);
            }
        }
    }
