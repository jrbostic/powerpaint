/*
 *  PowerPaint - TCSS 305 - Autumn 2013
 */

package main;

import java.awt.EventQueue;

/**
 * Uninstantiable class that runs Power Paint GUI.
 * 
 * @author Jesse Bostic
 * @version 11-13-2013
 */
public final class PowerPaintMain {
    
    /**
     * Constructor that ensures uninstantiablity.
     */
    private PowerPaintMain() {
        //never instantiate
    }
    
    /**
     * Main method for running power paint gui.
     * 
     * @param the_args command line arguments
     */
    public static void main(final String[] the_args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                final PowerPaintGUI paintGUI = new PowerPaintGUI();
                paintGUI.start();
            }
        });

    }

}
