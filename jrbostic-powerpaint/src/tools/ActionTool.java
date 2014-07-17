/*
 *  PowerPaint - TCSS 305 - Autumn 2013
 */

package tools;

import java.awt.Image;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

/**
 * Superclass for action tools, provides implementation
 * for handing image assignment and basic action state.
 * 
 * @author Jesse Bostic
 * @version 11-13-2013
 */
@SuppressWarnings("serial")
public class ActionTool extends AbstractAction {
    
    /**
     * The width of action icons.
     */
    private static final int ICON_WIDTH = 17;
    
    /**
     * The height of action icons.
     */
    private static final int ICON_HEIGHT = 17;
    
    /**
     * The name of this action.
     */
    private final String myName;
    
    /**
     * General constructor for an action tool object.
     */
    public ActionTool() {
        super();
        myName = getClass().getSimpleName();
        setFields();
    }
    
    /**
     * Gets name of action.
     * 
     * @return the name of this action
     */
    public String getName() {
        return myName;
    }
    
    /**
     * Helper method for constructor, sets action fields.
     */
    private void setFields() {
        super.putValue(NAME, myName);
        super.putValue(MNEMONIC_KEY, (int) getClass().getSimpleName().charAt(0));
        super.putValue(SHORT_DESCRIPTION, getClass().getSimpleName() + " Tool");
        
        //smoothes and resizes image
        Image image = new ImageIcon("icons/" + myName.toLowerCase() + "_bw.gif").getImage();  
        image = image.getScaledInstance(ICON_WIDTH, ICON_HEIGHT, java.awt.Image.SCALE_SMOOTH);
        
        super.putValue(SMALL_ICON, new ImageIcon(image));
    }
    
    /**
     * Behavior of pressed action tool button.
     * 
     * @param the_event event produced by clicking of button with this action
     */
    @Override
    public void actionPerformed(final ActionEvent the_event) { 
        super.putValue(SELECTED_KEY, true);
    }
    
    /**
     * Returns string representation of this tool.
     * 
     * @return string representation of this tool
     */
    @Override
    public String toString() {
        return myName;
    }

}
