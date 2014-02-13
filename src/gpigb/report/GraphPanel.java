package gpigb.report;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.LinkedList;


/**
 * GraphPanel is a component to display the graph.
 */
class GraphPanel extends JComponent {
	
	// An internal image buffer that is used for painting. Used for
	// actual display, this image buffer is then copied onto the screen.
	private BufferedImage graphImage;

	// scaling parameters for initial zoom-in and then for zooming-out if
	// necessary
	private static final double zoomOut = .8;
	private static final double zoomIn = 20;
	private static final double yAxisHeight = .6;
	private static int width;
	private static int height;

	private static final int mAvgSize = 6; 	// moving average is displayed taking
											// into consideration the last 6
											// values observed
	
	private static final int axisYgran = 20; 	// the granularity of the data axis
												// displayed on the screen
	
	private static final int DistFromRightBoarder = 20; 	// display the current
															// data point 20 pixels
															// to the left of the
															// right screen boundary

	// color definitions used for aesthetic reasons
	private static final Color SHADOW = new Color(0, 0, 0, 40);
	private static final Color DARK_GREEN = new Color(0, 128, 0);
	private static final Color DATA_BAR = new Color(0, 204, 0);
	private static final Color DATA_POINT = new Color(204, 102, 0);
	private static final Color STATS_COLOR = new Color(192, 192, 192);
	private static final Color AVG_COLOR = new Color(51, 51, 0, 200);
	private static final Color M_AVG_COLOR = new Color(0, 51, 102, 200);
	private static final Color BG_COLOR = Color.black;
	private static final Color AXIS_COLOR = DARK_GREEN;

	// statistics to be displayed on the screen
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

	/**
	 * Constructor for GraphPanel
	 * 
	 * @param width   width of the image to be displayed on the screen        
	 * @param height  height of the image to be displayed on the screen           
	 */
	public GraphPanel(int width, int height) {
		graphImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		GraphPanel.width = width;
		GraphPanel.height = height;
		reset();
		yMin = (int) (height * .12);
		yMax = (int) (height * .95);
		drawLegend();
	}

	/**
	 * Sets the current state of the graphing application to its default
	 * settings
	 */
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
	 * Clears the current image display
	 */
	public void clear() {
		Graphics g = graphImage.getGraphics();
		g.setColor(BG_COLOR);
		g.fillRect(0, 0, width, height);
		repaint();
	}

	/**
	 * Resets the graph application
	 */
	public void reset() {
		setToDefault();
		clear();
		repaint();
	}

	/**
	 * Draws the data axis with respect to the current zoomed-out state of the
	 * system and the set axis granularity. With each zooming-out the axis needs
	 * to be redrawn with respect to the new scaling
	 */
	private void drawAxis() {
		Graphics g = graphImage.getGraphics();
		g.setColor(AXIS_COLOR);
		int set_x = 6;
		int cur_y = (int) (yMin);
		int change_y = (int) ((yMax - yMin) / axisYgran);

		for (int i = 0; i <= axisYgran; i++) {
			int toPrint = (int) ((height * yAxisHeight - cur_y) / Math.pow(
					zoomOut, SCALE_TIMES));
			toPrint /= zoomIn;
			g.drawString("" + toPrint, set_x, cur_y + 5);
			cur_y += change_y;
		}
	}

	/**
	 * Erases the data axis
	 */
	private void eraseAxis() {
		Graphics g = graphImage.getGraphics();
		g.setColor(BG_COLOR);
		g.fillRect(0, 0, 40, height);
		g.setColor(SHADOW); /* Shadowy effect */
		g.fillRect(0, 40, 60, height);
	}

	/**
	 * Draws the legend for the displayed graph on the screen. Currently there
	 * are three characteristics of the displayed data supported - the data
	 * value, global average and moving average. Each is drawn in different
	 * color.
	 */
	private void drawLegend() {
		Graphics g = graphImage.getGraphics();

		g.setColor(DATA_BAR);
		g.drawString("Data value", (int) (width * .87),
				(int) (height * (.05 + .015)));
		g.fillRect((int) (width * .85), (int) (height * .05),
				(int) (width * .015), (int) (height * .015));
		g.setColor(DATA_POINT);
		g.fillRect((int) (width * .86), (int) (height * .05),
				(int) (width * .005), (int) (height * .015));

		g.setColor(AVG_COLOR);
		g.fillRect((int) (width * .85), (int) (height * .073),
				(int) (width * .015), (int) (height * .015));
		g.drawString("Avrg value", (int) (width * .87),
				(int) (height * (.073 + .015)));

		g.setColor(M_AVG_COLOR);
		g.fillRect((int) (width * .85), (int) (height * .096),
				(int) (width * .015), (int) (height * .015));
		g.drawString("Moving avg", (int) (width * .87),
				(int) (height * (.096 + .015)));
	}

	/**
	 * Draws the current statistics for the observed data. Currently supported
	 * are: minimum and maximum value observed so far, global average and
	 * current data value
	 */
	private void drawStats() {
		Graphics g = graphImage.getGraphics();

		g.setColor(STATS_COLOR);
		g.drawString("cur: " + cur, (int) (width * .37),
				(int) (height * (.05 + .015)));
		g.drawString("min: " + min, (int) (width * .45),
				(int) (height * (.05 + .015)));
		g.drawString("avg: " + avg, (int) (width * .53),
				(int) (height * (.05 + .015)));
		g.drawString("max: " + max, (int) (width * .61),
				(int) (height * (.05 + .015)));
	}

	/**
	 * Erases the statistics and the legend from the image
	 */
	private void eraseStatsAndLegend() {
		Graphics g = graphImage.getGraphics();

		g.setColor(BG_COLOR);
		g.fillRect(0, 0, width, yMin);
	}

	/**
	 * Calculates the sum of the last mAvgSize values observed in order to
	 * calculate the moving average
	 * 
	 * @param list
	 *            list which stores the last mAvgSize number of values
	 * @return the sum
	 */
	public Integer sumList(LinkedList<Integer> list) {
		Integer sum = 0;
		for (Integer i : list)
			sum += i;
		return sum;
	}

	/**
	 * Plots the graph slice for the current data received.
	 */
	public void update(int newData) {

		// Erase everything but the the actual graph here before any rescaling
		// is made
		eraseStatsAndLegend();
		eraseAxis();
		int y_axis_0 = (int) (height * yAxisHeight);

		// Save the current data point, recalculate the current avg, min and max
		// values
		cur = newData;
		sum += newData;
		count++;
		avg = sum / count;
		if (newData < min)
			min = newData;
		if (newData > max)
			max = newData;

		moving_avg.addLast(newData);
		if (moving_avg.size() >= mAvgSize) {
			m_avg = sumList(moving_avg) / mAvgSize;
			moving_avg.removeFirst();
		}

		Graphics g = graphImage.getGraphics();

		int height = graphImage.getHeight();
		int width = graphImage.getWidth();

		// Move graph one pixel to left
		g.copyArea(1, 0, width - 1, height, -1, 0);

		// Calculate y value for the data point
		int y = getYforPoint(newData);
		int y_avg = getYforPoint(avg);
		int y_m_avg = getYforPoint(m_avg);

		// If the y value for the data point to be plotted is out of the screen
		// bounds zoom out by scaling the image appropriately.
		while (y < yMin || y > yMax) {
			zoomOut();
			SCALE_TIMES++;
			y = getYforPoint(newData);
			y_avg = getYforPoint(avg);
			y_m_avg = getYforPoint(m_avg);
		}

		// Plot the current data value as a bar on the screen
		drawBar(y, y_axis_0, DATA_BAR);

		// If enough points have been observed, plot the moving average value as
		// a bar on the screen
		if (count >= mAvgSize)
			drawBar(y_m_avg, y_axis_0, M_AVG_COLOR);
		drawBar(y_avg, y_axis_0, AVG_COLOR);

		// Because the average bar could be drawn on top of the data bar (if
		// average value is the same as the current value),
		// draw the data value as a small bar in the end on top of the current
		// large bar (simulating a point on the screen).
		// That way the data value is always displayed in a visible manner
		int sign = (int) Math.signum(newData);
		int scaled_length = Math.abs((int) (height * yAxisHeight - y));
		drawBar(y, (int) (y + sign * scaled_length * 0.02), DATA_POINT);

		// Redraw the axis, statistics and legend after the appropriate re-scaling has
		// been done
		drawAxis();
		drawStats();
		drawLegend();
		
		// Paint the internal image with the changes made on the screen
		repaintNow();
	}

	/**
	 * Calculates the y value for plotting the currently observed data value
	 * 
	 * @param point data value      
	 * @return the y location for the value to be plotted
	 */
	private int getYforPoint(int point) {
		return (int) (height * yAxisHeight - point * zoomIn
				* Math.pow(zoomOut, SCALE_TIMES));
	}

	/**
	 * Draws a simple bar on the screen, essentially plotting a new data value,
	 * the X position is fixed and the to and from parameters specify the Y
	 * positions of the start and end of the bar
	 * 
	 * @color Color of the bar
	 * @from y position for the starting location of the bar
	 * @to x position for the ending position of the bar
	 */
	private void drawBar(int from, int to, Color color) {
		Graphics g = graphImage.getGraphics();
		g.setColor(color);
		g.drawLine(width - DistFromRightBoarder, from, width
				- DistFromRightBoarder, to);
	}

	/**
	 * Scale the current graph down vertically to make more room at the top or
	 * bottom.
	 */
	private void zoomOut() {
		Graphics g = graphImage.getGraphics();

		BufferedImage tmpImage = new BufferedImage(width,
				(int) (height * zoomOut), BufferedImage.TYPE_INT_RGB);
		Graphics2D gtmp = (Graphics2D) tmpImage.getGraphics();
		// scale the Y axis using the zoomOut factor
		gtmp.scale(1., zoomOut);
		// make exact copy of graphImage but scaled with respect of the Y axis
		gtmp.drawImage(graphImage, 0, 0, null);
		// Calculate the place to paste the rescaled image in the initial image
		// so that the x axis doesn't move
		int YmoveTo = (int) (yAxisHeight * height * (1. - zoomOut) + 1);
		// delete the entire image
		g.setColor(BG_COLOR);
		g.fillRect(0, 0, width, height);

		/*
		 * Draws as much of the specified image as is currently available. The
		 * image is drawn with its top-left corner at (0, YmoveTo) in the g
		 * graphics context's coordinate space.
		 */
		g.drawImage(tmpImage, 0, YmoveTo, null);
	}

	/**
	 * Force paints the internal image to the screen immediately (changes made
	 * become visible)
	 */
	public void repaintNow() {
		paintImmediately(0, 0, graphImage.getWidth(), graphImage.getHeight());
	}

	/**
	 * Tell the layout manager how big we would like to be. (This method gets
	 * called by layout managers for placing the components.)
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
	 * This component needs to be redisplayed. Copy the internal image to
	 * screen. (This method gets called by the Swing screen painter every time
	 * it want this component displayed.)
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
