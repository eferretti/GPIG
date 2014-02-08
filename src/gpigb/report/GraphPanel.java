
package gpigb.report;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.LinkedList;

class GraphPanel extends JComponent {
  // An internal image buffer that is used for painting. For
  // actual display, this image buffer is then copied to screen.
  private BufferedImage graphImage;

  private static final double zoomOut = .8;
  private static final double zoomIn = 20;
  private static final double yAxisHeight = .6;
  private static int width;
  private static int height;
  
  private static final int mAvgSize = 6;
  private static final int axisYgran = 20;
  private static final int DistFromRightBoarder = 20;
  
  private static final Color SHADOW = new Color(0, 0, 0, 40);
  private static final Color DARK_GREEN = new Color(0, 128, 0);
  private static final Color DATA_BAR = new Color(0, 204, 0);
  private static final Color DATA_POINT = new Color(204, 102, 0);
  private static final Color STATS_COLOR = new Color(192, 192, 192);
  private static final Color AVG_COLOR = new Color(51, 51, 0, 200);
  private static final Color M_AVG_COLOR = new Color(0, 51, 102, 200);
  private static final Color BG_COLOR = Color.black;
  private static final Color AXIS_COLOR = DARK_GREEN;  

  private int yMax;
  private int yMin;
  private int SCALE_TIMES = 0;
  private int cur = 0;
  private int min = Integer.MAX_VALUE;
  private int max = Integer.MIN_VALUE;
  private int count = 0;
  private int sum = 0;
  private int avg = 0;
  private int m_avg = 0;
  private LinkedList<Integer> moving_avg;

  public GraphPanel(int width, int height) {
    graphImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    GraphPanel.width = width;
    GraphPanel.height = height;
    reset();
    yMin = (int) (height * .12);
    yMax = (int) (height * .95);
    drawLegend();
  }

  public void setToDefault() {
    cur = 0;
    min = Integer.MAX_VALUE;
    max = Integer.MIN_VALUE;
    SCALE_TIMES = 0;
    count = 0;
    sum = 0;
    avg = 0;
    moving_avg = new LinkedList<Integer>();
  }

  
  /**
   * Clear the image on this panel.
   */
  public void clear() {
    Graphics g = graphImage.getGraphics();
    g.setColor(BG_COLOR);
    g.fillRect(0, 0, width, height);
    repaint();
  }
  
  public void reset() {
    setToDefault();
    clear();
    repaint();
  }
  
  private void drawAxis() {
    Graphics g = graphImage.getGraphics();
    g.setColor(AXIS_COLOR);
    int set_x = 6;
    int cur_y = (int) (yMin);
    int change_y = (int) ((yMax - yMin) / axisYgran);
//  really math is really fun boys... I hate my life 
    
    for (int i = 0; i <= axisYgran; i++) {
      int toPrint = (int) ((height * yAxisHeight - cur_y) / Math.pow(zoomOut, SCALE_TIMES));
      toPrint/=zoomIn;
      g.drawString("" + toPrint, set_x, cur_y + 3);
      cur_y += change_y;
    }
  } 

  private void eraseAxis() {
    Graphics g = graphImage.getGraphics();
    g.setColor(BG_COLOR); 
    g.fillRect(0, 0, 40, height);
    g.setColor(SHADOW); /* Shadowy effect */
    g.fillRect(0, 40, 60, height);
  }

  private void drawLegend() {
    Graphics g = graphImage.getGraphics();

    g.setColor(DATA_BAR);
    g.drawString("Data value", (int) (width * .87), (int) (height * (.05 + .015)));
    g.fillRect((int) (width * .85), (int) (height * .05), (int) (width * .015),
        (int) (height * .015));
    g.setColor(DATA_POINT);
    g.fillRect((int) (width * .86), (int) (height * .05), (int) (width * .005),
        (int) (height * .015));

    g.setColor(AVG_COLOR);
    g.fillRect((int) (width * .85), (int) (height * .073), (int) (width * .015),
        (int) (height * .015));
    g.drawString("Avrg value", (int) (width * .87), (int) (height * (.073 + .015)));

    g.setColor(M_AVG_COLOR);
    g.fillRect((int) (width * .85), (int) (height * .096), (int) (width * .015),
        (int) (height * .015));
    g.drawString("Moving avg", (int) (width * .87), (int) (height * (.096 + .015)));
  }

  private void drawStats() {
    Graphics g = graphImage.getGraphics();

    g.setColor(STATS_COLOR);
    g.drawString("cur: " + cur, (int) (width * .37), (int) (height * (.05 + .015)));
    g.drawString("min: " + min, (int) (width * .45), (int) (height * (.05 + .015)));
    g.drawString("avg: " + avg, (int) (width * .53), (int) (height * (.05 + .015)));
    g.drawString("max: " + max, (int) (width * .61), (int) (height * (.05 + .015)));
  }

  private void eraseStatsAndLegend() {
    Graphics g = graphImage.getGraphics();

    g.setColor(BG_COLOR);    
    g.fillRect(0, 0, width, yMin);   
  }

  public Integer sumList(LinkedList<Integer> list) {
    Integer sum = 0;
    for (Integer i : list)
      sum += i;
    return sum;
  }

  /**
   * Dispay a new point of data.
   */
  public void update(int newData) {
    eraseStatsAndLegend();
    eraseAxis();
    int y_axis_0 = (int) (height * yAxisHeight);
    
    cur = newData;
    sum += newData;
    count ++;
    avg = sum / count;
    if (newData < min)     min = newData;
    if (newData > max)     max = newData;

    moving_avg.addLast(newData);
    if (moving_avg.size() >= mAvgSize) {
      m_avg = sumList(moving_avg) / mAvgSize;
      moving_avg.removeFirst();
    }
   
    Graphics g = graphImage.getGraphics();

    int height = graphImage.getHeight();
    int width = graphImage.getWidth();
    
    // move graph one pixel to left
    g.copyArea(1, 0, width - 1, height, -1, 0);

    // calculate y for the data point, check whether it's out of screen.
    int y = getYforPoint(newData);
    int y_avg = getYforPoint(avg);
    int y_m_avg = getYforPoint(m_avg);
    // scale down if necessary.
    while (y < yMin || y > yMax) {
      zoomOut();
      SCALE_TIMES++;
      y = getYforPoint(newData);
      y_avg = getYforPoint(avg);
      y_m_avg = getYforPoint(m_avg);
    }
    
    drawBarAtYtillX(y, y_axis_0, DATA_BAR);
    if (count >= mAvgSize)  drawBarAtYtillX(y_m_avg, y_axis_0, M_AVG_COLOR);
    drawBarAtYtillX( y_avg, y_axis_0, AVG_COLOR);

    int sign = (int) Math.signum(newData);
    int scaled_length = Math.abs((int) (height * yAxisHeight - y));
    drawBarAtYtillX( y, (int) (y + sign* scaled_length * 0.02), DATA_POINT);
 
    drawAxis();
    drawStats();
    drawLegend();
    
    repaintNow();
  }

  private int getYforPoint(int point) {
    return (int) (height * yAxisHeight - point * zoomIn * Math.pow(zoomOut, SCALE_TIMES));
  }
  
  private void drawBarAtYtillX(int y, int x, Color color) {
    Graphics g = graphImage.getGraphics();
    g.setColor(color);
    g.drawLine(width - DistFromRightBoarder, y, width - DistFromRightBoarder, x);
  }

  /**
   * Scale the current graph down vertically to make more room at the top or bottom. Should have
   * taken Advanced Computer Vision and probably use the Affine transformations provided in Java ..
   */
  private void zoomOut() {
    Graphics g = graphImage.getGraphics();

    BufferedImage tmpImage =
        new BufferedImage(width, (int) (height * zoomOut), BufferedImage.TYPE_INT_RGB);
    Graphics2D gtmp = (Graphics2D) tmpImage.getGraphics();
    // scale the Y axis by the zoomOut
    gtmp.scale(1., zoomOut);
    // make exact copy of graphImage but scaled with respect of the Y axis
    gtmp.drawImage(graphImage, 0, 0, null);
    // math is fun... not
    int YmoveTo = (int) (yAxisHeight * height * (1. - zoomOut) + 1);
    // delete the entire image
    g.setColor(BG_COLOR);
    g.fillRect(0, 0, width, height);

    /*
     * Draws as much of the specified image as is currently available. The image is drawn with its
     * top-left corner at (0, YmoveTo) in the g graphics context's coordinate space.
     */
    g.drawImage(tmpImage, 0, YmoveTo, null);
  }

  /**
   * Cause immediate update of the panel.
   */
  public void repaintNow() {
    paintImmediately(0, 0, graphImage.getWidth(), graphImage.getHeight());
  }


  /**
   * Tell the layout manager how big we would like to be. (This method gets called by layout
   * managers for placing the components.)
   * 
   * @return The preferred dimension for this component.
   */
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(graphImage.getWidth(), graphImage.getHeight());
  }

  /**
   * This component is opaque.
   */
  @Override
  public boolean isOpaque() {
    return true;
  }

  /**
   * This component needs to be redisplayed. Copy the internal image to screen. (This method gets
   * called by the Swing screen painter every time it want this component displayed.)
   * 
   * @param g The graphics context that can be used to draw on this component.
   */
  @Override
  public void paintComponent(Graphics g) {
    if (graphImage != null) {
      g.drawImage(graphImage, 0, 0, null);
    }
  }
}
