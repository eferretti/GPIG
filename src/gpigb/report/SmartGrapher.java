package gpigb.report;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Random;

import javax.swing.JFrame;

/**
 * The SmartGrapher provides a view of three populations of values (the current
 * integer value, the overall average and the moving average) as a line graph
 * over time. The image is re-scaling if the data value to be displayed exceeds
 * the current GraphPanel bounds. The image is initially zoomed in for allowing
 * the display of small integer values and would zoom out from there if needed
 * 
 * @author GPIG B
 */
public class SmartGrapher
{

	private JFrame frame;
	private GraphPanel graph;

	private static final int Default_width = 900;
	private static final int Default_height = 500;

	/**
	 * @param title
	 *            title for the new plotter window
	 * @param width
	 *            width The height of the plotter window (in pixels)
	 * @param height
	 *            height The height of the plotter window (in pixels)
	 */
	public SmartGrapher(String title, int width, int height)
	{
		makeFrame(title, width, height);
	}

	/**
	 * @param title
	 *            title for the new plotter window
	 */
	public SmartGrapher(String title)
	{
		this(title, Default_width, Default_height);
	}

	/**
	 * Constructor for SmartGrapher using default values
	 */
	public SmartGrapher()
	{
		this("SmartGrapher", Default_width, Default_height);
	}

	/**
	 * @param width
	 *            width width The height of the plotter window (in pixels)
	 * @param height
	 *            height The height of the plotter window (in pixels)
	 */
	public SmartGrapher(int width, int height)
	{
		this("SmartGrapher", width, height);
	}

	/**
	 * Main method showing a small demo for the graph
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		SmartGrapher sg = new SmartGrapher(Default_width, Default_height);
		sg.testRun();
		sg.reset();
	}

	/**
	 * Plots a new data point on the graph
	 * 
	 * @param data
	 */
	public void plot(int data)
	{
		graph.update(data);
	}

	/**
	 * A simple test run for randomly generated data
	 * 
	 * @param data
	 */
	public void plot(int[] data)
	{
		for (int i = 0; i < data.length; i++) {
			plot(data[i]);
		}
	}

	/**
	 * Plots a collection of data points on the graph
	 */
	public void testRun()
	{
		int[] data = new int[2500];
		Random g = new Random();

		for (int i = 0; i < 2500; i++) {
			if (i < 100)
				data[i] = 40 - g.nextInt(50);
			else if (i < 200)
				data[i] = 500 - g.nextInt(1000);
			else if (i < 400)
				data[i] = 800 - g.nextInt(1600);
			else if (i < 500)
				data[i] = 1000 - g.nextInt(2000);
			else if (i < 1500)
				data[i] = g.nextInt(3000);
			else if (i < 2500) data[i] = 0 - g.nextInt(5000);
		}

		for (int i = 0; i < 2500; i++) {
			plot(data[i]);
			wait(15);
		}
	}

	/**
	 * Method forcing the system to wait, simulating delay between the recieving
	 * of two data values
	 * 
	 * @param milliseconds
	 *            the time of the delay in ms
	 */
	public void wait(int milliseconds)
	{
		try {
			Thread.sleep(milliseconds);
		}
		catch (InterruptedException e) {
			// CATCH A FLU
		}
	}

	/**
	 * Prepare the frame for the graph display.
	 */
	protected void makeFrame(String title, int width, int height)
	{
		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setResizable(false);
		Container contentPane = frame.getContentPane();

		graph = new GraphPanel(width, height);
		contentPane.add(graph, BorderLayout.CENTER);

		frame.pack();

		/* Position the Frame window in the centre of the screen */
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);

		frame.setVisible(true);
	}

	public void reset()
	{
		graph.reset();
	}
}
