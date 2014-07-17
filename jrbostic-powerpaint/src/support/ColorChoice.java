/*
 *  PowerPaint - TCSS 305 - Autumn 2013
 */

package support;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import tools.ActionTool;

/**
 * Class which provides functionality and appearance for a color chooser button.
 * 
 * @author Jesse Bostic
 * @version 11-13-2013
 */
@SuppressWarnings("serial")
public class ColorChoice extends ActionTool {
    
    /**
     * Current color selected.
     */
    private Color myColor;
    
    /**
     * Frame in which color chooser appears.
     */
    private JFrame myFrame;
    
    /**
     * Toolbar in which color chooser appears.
     */
    private JToolBar myToolBar;
    
    /**
     * Color swatch for instantiated color choosing button.
     */
    private ColorSwatch mySwatch;
    
    /**
     * Constructor for a color choice tool.
     * 
     * @param the_frame frame in which colorchoice action will exist
     * @param the_toolbar toolbar in which colorchoice action will exist
     */
    public ColorChoice(final JFrame the_frame, final JToolBar the_toolbar) {
        super();
        super.putValue(NAME, "Color...");
        myColor = Color.black;
        mySwatch = new ColorSwatch();
        super.putValue(SMALL_ICON, mySwatch);  
        super.putValue(SHORT_DESCRIPTION, "Color Tool");
        myFrame = the_frame;
        myToolBar = the_toolbar;
    }
    
    /**
     * Gets the color currently chosen through JColorChooser.
     * 
     * @return the color chosen by user
     */
    public Color getChosenColor() {
        return myColor;
    }
    
    /**
     * If button containing action is pressed, if color is chosen, sets myColor to that 
     * value and repaints containing toolbar and frame objects.
     * 
     * @param the_event the event produced by clicking button
     */
    @Override
    public void actionPerformed(final ActionEvent the_event) {
        final Color temp = JColorChooser.showDialog(myFrame, "My Color Chooser", myColor);
        if (temp != null) {
            myColor = temp;
            
        }
        myToolBar.repaint();
        myFrame.repaint();
    }
    
    /**
     * Constructs and returns string representation of color choice object.
     * 
     * @return string representation of this colorchoice object
     */
    @Override
    public String toString() {
        return super.toString() + " Tool: " + myColor;
    }
    
    /**
     * Private inner class which provides an icon of the chosen color.
     * 
     * @author Jesse Bostic
     * @version 11-13-2013
     */
    private final class ColorSwatch implements Icon {
        
        /**
         * Width of icon.
         */
        private static final int ICON_WIDTH = 20;
        
        /**
         * Height of icon.
         */
        private static final int ICON_HEIGHT = 12;
        
        /**
         * Width of icon edge arc. (for rounded rect)
         */
        private static final int ICON_ARC_WIDTH = 3;
        
        /**
         * Height of icon edge arc. (for rounded rect)
         */
        private static final int ICON_ARC_HEIGHT = 3;
        
        /**
         * Constructor for color swatch icon.
         */
        private ColorSwatch() {
            
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public int getIconHeight() {
            return ICON_HEIGHT;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public int getIconWidth() {
            return ICON_WIDTH;
        }
        
        /**
         * Sets up the icon to be displayed.
         * 
         * {@inheritDoc}
         */
        @Override
        public void paintIcon(final Component the_comp, final Graphics the_graphic, 
                              final int the_x, final int the_y) {
            the_graphic.setColor(myColor);
            the_graphic.fillRoundRect(the_x, the_y, ICON_WIDTH, ICON_HEIGHT, 
                                      ICON_ARC_WIDTH, ICON_ARC_HEIGHT);
            the_graphic.setColor(Color.BLACK);
            the_graphic.drawRoundRect(the_x, the_y, ICON_WIDTH, ICON_HEIGHT, 
                                      ICON_ARC_WIDTH, ICON_ARC_HEIGHT);
            
        }
        
    }

}
