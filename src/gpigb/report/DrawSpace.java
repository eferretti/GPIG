package gpigb.report;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Class DrawingBoard - a class creating a drawing space window to draw on..
 */

public class DrawSpace
{
    private Graphics2D graphic;
    private JFrame frame;
    private CanvasPane canvas;
    private Color backgroundColor;
    private Image canvasImage;
    
    /* default values of the drawing space */
    final static int DEFAULT_WIDHT  = 800;
    final static int DEFAULT_HEIGHT = 500;
    
    /**
     * Create a drawing space with default height, width and background color 
     * (800, 500, white).
     * @param title  title to appear in Drawing Space Frame     
     */
    public DrawSpace(String title) {
        this(title, DEFAULT_WIDHT, DEFAULT_HEIGHT, Color.white);
    }

    /**
     * Create a Drawing Space with white color
     * @param title  title to appear in the Drawing Space Frame
     * @param width  the desired width for the frame
     * @param height  the desired height for the frame
     */
    public DrawSpace(String title, int width, int height) {
        this(title, width, height, Color.white);
    }

    /**
     * Create a Drawing Space.
     * @param title  title to appear in the Drawing Space Frame
     * @param width  the desired width for the frame
     * @param height  the desired height for the frame
     * @param bgClour  the desired background color of the frame
     */
    public DrawSpace(String title, int width, int height, Color bgColor) {
        frame = new JFrame();
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        canvas.setPreferredSize(new Dimension(width, height));
        backgroundColor = bgColor;
        frame.pack();
        setVisible(true);
    }

    /**
     * Set the canvas visibility and brings canvas to the front of screen
     * when made visible. This method can also be used to bring an already
     * visible drawing spaces to the front of other windows (drawing spaces).
     * @param visibility  boolean value representing the desired visibility of
     * the drawing space (true or false) 
     */
    public void setVisible(boolean visibility) {
        if(graphic == null) {
            // initial start: instantiate the offscreen image and fill it with
            // the desired background color
            Dimension size = canvas.getSize();
            canvasImage = canvas.createImage(size.width, size.height);
            graphic = (Graphics2D)canvasImage.getGraphics();
            graphic.setColor(backgroundColor);
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(Color.black);
        }
        frame.setVisible(visibility);
    }

    /**
     * Provide information on visibility of the drawing space.
     * @return  true if current window is visible, false otherwise
     */
    public boolean isVisible() {
        return frame.isVisible();
    }

    /**
     * Draw the outline of a given shape onto the canvas.
     * @param  shape  the shape object to be drawn on the canvas
     */
    public void draw(Shape shape) {
        graphic.draw(shape);
        canvas.repaint();
    }
    
    public void setLineWidth(int width){
    	graphic.setStroke(new BasicStroke(width));
    }
    /**
     * Fill the internal dimensions of a given shape with the current 
     * foreground color of the canvas.
     * @param  shape  the shape object to be filled 
     */
    public void fill(Shape shape) {
        graphic.fill(shape);
        canvas.repaint();
    }

    /**
     * Fill the internal dimensions of the given circle with the current 
     * foreground color of the canvas.
     * @param  xPos  The x-coordinate of the circle center point
     * @param  yPos  The y-coordinate of the circle center point
     * @param  diameter  The diameter of the circle to be drawn
     */
    public void fillCircle(int xPos, int yPos, int diameter) {
        Ellipse2D.Double circle = new Ellipse2D.Double(xPos, yPos, diameter, diameter);
        fill(circle);
    }

    /**
     * Fill the internal dimensions of the given rectangle with the current 
     * foreground color of the drawing space. This is a convenience method. A similar 
     * effect can be achieved with the "fill" method.
     */
    public void fillRectangle(int xPos, int yPos, int width, int height) {
        fill(new Rectangle(xPos, yPos, width, height));
    }
    
    
    /**
     * Erase the whole canvas.
     */
    public void erase() {
        Color original = graphic.getColor();
        graphic.setColor(backgroundColor);
        Dimension size = canvas.getSize();
        graphic.fill(new Rectangle(0, 0, size.width, size.height));
        graphic.setColor(original);
        canvas.repaint();
    }

    /**
     * Erase the internal dimensions of the given circle. This is a 
     * convenience method. A similar effect can be achieved with
     * the "erase" method.
     */
    public void eraseCircle(int xPos, int yPos, int diameter) {
        Ellipse2D.Double circle = new Ellipse2D.Double(xPos, yPos, diameter, diameter);
        erase(circle);
    }

    /**
     * Erase the internal dimensions of the given rectangle. This is a 
     * convenience method. A similar effect can be achieved with
     * the "erase" method.
     */
    public void eraseRectangle(int xPos, int yPos, int width, int height) {
        erase(new Rectangle(xPos, yPos, width, height));
    }

    /**
     * Erase a given shape's interior on the screen.
     * @param  shape  the shape object to be erased 
     */
    public void erase(Shape shape) {
        Color original = graphic.getColor();
        graphic.setColor(backgroundColor);
        graphic.fill(shape);              // erase by filling background color
        graphic.setColor(original);
        canvas.repaint();
    }

    /**
     * Erases a given shape's outline on the screen.
     * @param  shape  the shape object to be erased 
     */
    public void eraseOutline(Shape shape)
    {
        Color original = graphic.getColor();
        graphic.setColor(backgroundColor);
        graphic.draw(shape);  // erase by drawing background color
        graphic.setColor(original);
        canvas.repaint();
    }

    /**
     * Draws an image onto the canvas.
     * @param  image   the Image object to be displayed 
     * @param  x       x co-ordinate for Image placement 
     * @param  y       y co-ordinate for Image placement 
     * @return  returns boolean value representing whether the image was 
     *          completely loaded 
     */
    public boolean drawImage(Image image, int x, int y)
    {
        boolean result = graphic.drawImage(image, x, y, null);
        canvas.repaint();
        return result;
    }

    /**
     * Draws a String on the drawing space.
     * @param  text   the String to be displayed 
     * @param  x      x co-ordinate for text placement 
     * @param  y      y co-ordinate for text placement
     */
    public void drawString(String text, int x, int y) {
        graphic.drawString(text, x, y);   
        canvas.repaint();
    }

    /**
     * Erases a String on the drawing space.
     * @param  text     the String to be displayed 
     * @param  x        x co-ordinate for text placement 
     * @param  y        y co-ordinate for text placement
     */
    public void eraseString(String text, int x, int y) {
        Color original = graphic.getColor();
        graphic.setColor(backgroundColor);
        graphic.drawString(text, x, y);   
        graphic.setColor(original);
        canvas.repaint();
    }

    /**
     * Draws a line on the drawing space.
     * @param  x1   x co-ordinate of starting point of the line 
     * @param  y1   y co-ordinate of starting point of the line 
     * @param  x2   x co-ordinate of starting point of the line 
     * @param  y2   y co-ordinate of starting point of the line 
     */
    public void drawLine(int x1, int y1, int x2, int y2) {
        graphic.drawLine(x1, y1, x2, y2);   
        canvas.repaint();
    }

    /**
     * Sets the foreground color of the drawing space.
     * @param  newColor   the new color for the foreground of the drawing space 
     */
    public void setForegroundColor(Color newColor) {
        graphic.setColor(newColor);
    }

    /**
     * Returns the current foreground color
     * @return   the foreground color of the drawing space 
     */
    public Color getForegroundColor() {
        return graphic.getColor();
    }

    /**
     * Sets the background color of the drawing space.
     * @param  newColor   the new background color of the drawing space 
     */
    public void setBackgroundColor(Color newColor) {
        backgroundColor = newColor;   
        graphic.setBackground(newColor);
    }

    /**
     * Returns the current background color
     * @return   the background color of the drawing space 
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * set the current Font used on the drawing space
     * @param  newFont   new font to be used for String output
     */
    public void setFont(Font newFont) {
        graphic.setFont(newFont);
    }

    /**
     * Returns the current font of the canvas.
     * @return     the font currently in use
     **/
    public Font getFont() {
        return graphic.getFont();
    }

    /**
     * Sets the size of the canvas.
     * @param  width    new width 
     * @param  height   new height 
     */
    public void setSize(int width, int height) {
        canvas.setPreferredSize(new Dimension(width, height));
        Image oldImage = canvasImage;
        canvasImage = canvas.createImage(width, height);
        graphic = (Graphics2D)canvasImage.getGraphics();
        graphic.setColor(backgroundColor);
        graphic.fillRect(0, 0, width, height);
        graphic.drawImage(oldImage, 0, 0, null);
        frame.pack();
    }

    /**
     * Returns the size of the canvas.
     * @return     The current dimension of the canvas
     */
    public Dimension getSize() {
        return canvas.getSize();
    }

    /**
     * Waits for a specified number of milliseconds before finishing.
     * This provides an easy way to specify a small delay which can be
     * used when producing animations.
     * @param  milliseconds  the number 
     */
    public void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } 
        catch (InterruptedException e) {
            // CATCH A FLU
        }
    }

    /************************************************************************
     * Inner class CanvasPane - the actual canvas component contained in the
     * drawing space frame. This is essentially a JPanel with added capability to
     * refresh the image drawn on it.
     */
    private class CanvasPane extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(canvasImage, 0, 0, null);
        }
    }
}
