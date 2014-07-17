/*
 *  PowerPaint - TCSS 305 - Autumn 2013
 */

package tools;

/**
 * Class representing pencil tool action.
 * 
 * @author Jesse Bostic
 * @version 11-13-2013
 */
@SuppressWarnings("serial")
public class Pencil extends ActionTool {
    
    /**
     * Constructor for pencil tool action.
     */
    public Pencil() {
        super();
        super.putValue(SELECTED_KEY, true);
    }

}
