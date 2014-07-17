/*
 *  PowerPaint - TCSS 305 - Autumn 2013
 */

package support;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;

/**
 * Class that holds shape, color, stroke and fill data about a drawn object.
 * 
 * @author Jesse Bostic
 * @version 11-13-2013
 */
public class DrawnObject {
    
    /**
     * The shape object.
     */
    private final Shape myShape;
    
    /**
     * The color of shape.
     */
    private final Color myColor;
    
    /**
     * The stroke thickness of shape.
     */
    private final Stroke myStroke;
    
    /**
     * Whether or not shape is filled.
     */
    private final boolean myFillEnabled;
    
    /**
     * Constructor for drawn object.
     * 
     * @param the_shape the shape drawn
     * @param the_color the shape's color
     * @param the_stroke the shape's thickness
     * @param the_fill_flag true if shape filled, false otherwise
     */
    public DrawnObject(final Shape the_shape, final Color the_color, 
                       final Stroke the_stroke, final boolean the_fill_flag) {
        myShape = the_shape;
        myColor = the_color;
        myStroke = the_stroke;
        myFillEnabled = the_fill_flag;
    }
    
    /**
     * Gets this drawnobject's shape.
     * 
     * @return the shape of this object
     */
    public Shape getShape() {
        return myShape;
    }
    
    /**
     * Gets this drawnobject's color.
     * 
     * @return the color of this object
     */
    public Color getColor() {
        return myColor;
    }
    
    /**
     * Gets this drawnobject's stroke thickness.
     * 
     * @return the stroke of this object
     */
    public Stroke getStroke() {
        return myStroke;
    }
    
    /**
     * Gets this drawnobject's fill status.
     * 
     * @return true if shape is filled, false otherwise
     */
    public boolean getFillStatus() {
        return myFillEnabled;
    }
    
    /**
     * Constructs and returns string representation of drawn object.
     * 
     * @return string representation of this drawn object
     */
    @Override
    public String toString() { 
        final StringBuilder strRep = new StringBuilder();
        final String comma = ", ";
        strRep.append(myShape.getClass().getSuperclass().getSimpleName());
        strRep.append("[");
        strRep.append(myColor.toString().substring(myColor.toString().lastIndexOf('.') + 1));
        strRep.append(comma);
        strRep.append("Stroke:");
        strRep.append(((BasicStroke) myStroke).getLineWidth());
        strRep.append(comma);
        strRep.append("Filled:");
        strRep.append(myFillEnabled);
        strRep.append("]");
        return strRep.toString();
    }

}
