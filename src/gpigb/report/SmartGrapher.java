<<<<<<< HEAD
package gpigb.report;

import java.awt.*;
import javax.swing.*;
import java.util.Random;

public class SmartGrapher {

  private JFrame frame;
  private GraphPanel graph;

  private static final int Default_width = 900;
  private static final int Default_height = 500;


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
  public SmartGrapher(String title, int width, int height) {
    makeFrame(title, width, height);
  }

  public SmartGrapher(String title) {
    this(title, Default_width, Default_height);
  }

  public SmartGrapher() {
    this("SmartGrapher", Default_width, Default_height);
  }
  
  public SmartGrapher(int width, int height) {
    this("SmartGrapher", width, height);
  }

  public static void main(String[] args) {
    SmartGrapher sg = new SmartGrapher(Default_width, Default_height);
    sg.testRun();
    sg.reset();
  }

  public void plot(int data) {
    graph.update(data);
  }


  public void plot(int[] data) {
    for (int i = 0; i < data.length; i++) {
      plot(data[i]);
    }
  }

  public void testRun() {
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
      else if (i < 2500)
        data[i] = 0 - g.nextInt(5000);
    }

    for (int i = 0; i < 2500; i++) {
      plot(data[i]);
      wait(1);
    }
  }

  public void wait(int milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      // CATCH A FLU
    }
  }

  protected void makeFrame(String title, int width, int height) {
    frame = new JFrame(title);
    frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    //frame.setExtendedState(Frame.MAXIMIZED_BOTH);  
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

  public void reset() {
    graph.reset();
  }
}
=======
package gpigb.report;

import java.awt.*;
import javax.swing.*;
import java.util.Random;

public class SmartGrapher {

  private JFrame frame;
  private GraphPanel graph;

  private static final int Default_width = 900;
  private static final int Default_height = 500;


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
  public SmartGrapher(String title, int width, int height) {
    makeFrame(title, width, height);
  }

  public SmartGrapher(String title) {
    this(title, Default_width, Default_height);
  }

  public SmartGrapher() {
    this("SmartGrapher", Default_width, Default_height);
  }
  
  public SmartGrapher(int width, int height) {
    this("SmartGrapher", width, height);
  }

  public static void main(String[] args) {
    SmartGrapher sg = new SmartGrapher(Default_width, Default_height);
    sg.testRun();
    sg.reset();
  }

  public void plot(int data) {
    graph.update(data);
  }


  public void plot(int[] data) {
    for (int i = 0; i < data.length; i++) {
      plot(data[i]);
    }
  }

  public void testRun() {
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
      else if (i < 2500)
        data[i] = 0 - g.nextInt(5000);
    }

    for (int i = 0; i < 2500; i++) {
      plot(data[i]);
      wait(1);
    }
  }

  public void wait(int milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      // CATCH A FLU
    }
  }

  protected void makeFrame(String title, int width, int height) {
    frame = new JFrame(title);
    frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    //frame.setExtendedState(Frame.MAXIMIZED_BOTH);  
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

  public void reset() {
    graph.reset();
  }
}
>>>>>>> 1dcecfdf4724e1a153be7c62c3ef4563c388b95b
