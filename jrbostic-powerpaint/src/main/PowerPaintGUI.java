/*
 *  PowerPaint - TCSS 305 - Autumn 2013
 */

package main;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import support.ColorChoice;
import support.DrawnObject;
import support.Reverter;
import tools.Ellipse;
import tools.Line;
import tools.Pencil;
import tools.Rectangle;

/**
 * The class that handles creation of GUI aspects of power paint.
 * 
 * @author Jesse Bostic
 * @version 11-13-2013
 */
public class PowerPaintGUI {
    
    /**
     * The GUI frame.
     */
    private static final JFrame MY_FRAME = new JFrame();
    
    /**
     * The GUI toolbar.
     */
    private static final JToolBar MY_TOOLBAR = new JToolBar();
    
    /**
     * The color choosing action for this GUI.
     */
    private static final ColorChoice COLOR_CHOOSER = new ColorChoice(MY_FRAME, MY_TOOLBAR);
    
    /**
     * The reverter for this GUI, handles undo/redo operations.
     */
    private static final Reverter MY_GRAPHICS = new Reverter(MY_FRAME);
    
    /**
     * The file chooser for opening and saving image files.
     */
    private static final JFileChooser FILE_CHOOSER = new JFileChooser();
    
    /**
     * Frame's main icon dimension.
     */
    private static final int FRAME_ICON_DIMENSION = 30;
    
    /**
     * The standard size of drawing panel.
     */
    private static final Dimension PANEL_SIZE = new Dimension(500, 300);
    
    /**
     * The number of stroke options from 1 to value assigned.
     */
    private static final int STROKE_OPTIONS = 9;
    
    /**
     * File menu string.
     */
    private static final String FILE_STRING = "File";
    
    /**
     * Option menu string.
     */
    private static final String OPTION_STRING = "Options";
    
    /**
     * Tool menu string.
     */
    private static final String TOOL_STRING = "Tools";
    
    /**
     * Help menu string.
     */
    private static final String HELP_STRING = "Help";
    
    /**
     * Pencil tool string.
     */
    private static final String PENCIL_STRING = "Pencil";
    
    /**
     * Line tool string.
     */
    private static final String LINE_STRING = "Line";
    
    /**
     * Rectangle tool string.
     */
    private static final String RECT_STRING = "Rectangle";
    
    /**
     * Array of actions to be attached to the various tools.
     */
    private static final Action[] MY_TOOLS = new Action[] {new Pencil(), new Line(), 
                                                           new Rectangle(), new Ellipse()};

    /**
     * The easel panel to be drawn on.
     */
    private final Easel myEasel;
    
    /**
     * The undo action.
     */
    private final Action myUndoAction;
    
    /**
     * The redo action.
     */
    private final Action myRedoAction;
    
    /**
     * The tool button group for keeping reference.
     */
    private final ButtonGroup myToolGroup;
    
    /**
     * Holds menu items that need to be enabled and disabled.
     */
    private final List<JMenuItem> myAbleToDisableTools; 
    
    /**
     * The current stroke.
     */
    private Stroke myStroke;
    
    /**
     * The current color.
     */
    private Color myColor;
    
    /**
     * Whether grid view is enabled.
     */
    private boolean myGridEnabled;
    
    /**
     * Whether shape fill function is enabled.
     */
    private boolean myFillEnabled;
    
    /**
     * Optional image loaded by user.
     */
    private ImageIcon myImage;
    
    /**
     * Constructor for this power paint instance.
     */
    public PowerPaintGUI() {
        myEasel = new Easel();
        myUndoAction = MY_GRAPHICS.getUndoAction();
        myRedoAction = MY_GRAPHICS.getRedoAction();
        myToolGroup = new ButtonGroup();
        myAbleToDisableTools = new ArrayList<JMenuItem>();
        myStroke = null;
        myColor = COLOR_CHOOSER.getChosenColor();
        myImage = null;
    }
    
    /**
     * Central method that sets up the power paint GUI.
     */
    public void start() {
        MY_FRAME.setTitle("TCSS 305 PowerPaint");
        
        Image image = new ImageIcon("icons/spiral.png").getImage();  
        image = image.getScaledInstance(FRAME_ICON_DIMENSION, FRAME_ICON_DIMENSION, 
                                        java.awt.Image.SCALE_SMOOTH);
        MY_FRAME.setIconImage(image);
        
        MY_FRAME.setLocationRelativeTo(null);
        MY_FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        createMenuBar();
        createToolbar();
        
        myEasel.setPreferredSize(PANEL_SIZE);
        myEasel.setBackground(Color.WHITE);
        MY_FRAME.add(myEasel, BorderLayout.CENTER);
        
        MY_FRAME.pack();
        MY_FRAME.setVisible(true);
    } 
    
    /**
     * Method that creates menu bar.
     */
    private void createMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        final String[] menuNames = new String[] {FILE_STRING, OPTION_STRING, 
                                                 TOOL_STRING, HELP_STRING};
        for (String s : menuNames) {
            final JMenu menu = new JMenu(s);
            menu.setMnemonic(s.charAt(0));
            menuBar.add(menu);
            createSubMenu(menu);
        }
        MY_FRAME.setJMenuBar(menuBar);  
    }
    
    /**
     * Method populates passed menu via control structure.
     * 
     * @param the_menu the menu to be populated
     */
    private void createSubMenu(final JMenu the_menu) {
        if (the_menu.getText().equals(FILE_STRING)) { 
            createFileMenu(the_menu);
        } else if (the_menu.getText().equals(OPTION_STRING)) {
            createOptionMenu(the_menu);
        } else if (the_menu.getText().equals(TOOL_STRING)) {
            createToolMenu(the_menu);
        } else if (the_menu.getText().equals(HELP_STRING)) {
            createHelpMenu(the_menu);
        }
    }
    
    /**
     * Populates file menu items to passed menu.
     * 
     * @param the_menu menu to populate
     */
    private void createFileMenu(final JMenu the_menu) {
        final JMenuItem open = new JMenuItem("Open..."); //open file menu item
        open.setMnemonic('O');
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent the_event) {
                openFile();
            }
        });
        final JMenuItem save = new JMenuItem("Save");  //save file menu item
        save.setMnemonic('S');
        save.addActionListener(new ActionListener() {
            private final int myDelay = 1000;
            @Override
            public void actionPerformed(final ActionEvent the_event) {
                saveFile(myDelay);
            }
        });
        final JMenuItem clear = new JMenuItem("Clear"); //clear project menu item
        clear.setMnemonic('C');
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent the_event) {
                final int choice = JOptionPane.showConfirmDialog(MY_FRAME, "Are you sure you "
                        + "want to proceed?\nAll changes will be lost!", "Clear All Progress", 
                        JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    myEasel.clear();  
                }
            }
        });
        final JMenuItem exit = new JMenuItem("Exit"); //exit program menu item
        exit.setMnemonic('X');
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent the_event) {
                MY_FRAME.dispose();  
            }
        });
        
        //create menu structure
        the_menu.add(open);
        the_menu.add(save);
        the_menu.addSeparator();
        the_menu.add(clear);
        the_menu.addSeparator();
        the_menu.add(exit);
        
        //set enablement and hold references for save and clear menu items
        save.setEnabled(false);
        clear.setEnabled(false);
        myAbleToDisableTools.add(save);
        myAbleToDisableTools.add(clear);
    }
    
    /**
     * Populates option menu items to passed menu.
     * 
     * @param the_menu menu to populate
     */
    private void createOptionMenu(final JMenu the_menu) {
        final JMenuItem undo = new JMenuItem(myUndoAction); //undo option
        final JMenuItem redo = new JMenuItem(myRedoAction); //redo option
        final JCheckBox grid = new JCheckBox("Grid");  //grid option
        grid.setMnemonic('G');
        grid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent the_event) {
                myGridEnabled = !myGridEnabled;
                myEasel.repaint();
            }
        });
        final JCheckBox fill = new JCheckBox("Shape Fill"); //fill option
        fill.setMnemonic('F');
        fill.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent the_event) {
                myFillEnabled = !myFillEnabled;
            }
        });    
        final JMenu stroke = new JMenu("Thickness");  //stroke thickness options
        stroke.setMnemonic('T');
        final ButtonGroup group = new ButtonGroup();
        for (int i = 1; i <= STROKE_OPTIONS; i++) {
            final JRadioButton button = new JRadioButton("" + i);
            button.setMnemonic(("" + i).charAt(0));
            final Stroke this_stroke = new BasicStroke(i);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent the_event) {
                    myStroke = this_stroke;
                }
            });
            group.add(button);
            stroke.add(button);
            if (i == 1) {    //set stroke size 1
                myStroke = this_stroke;
                button.setSelected(true);
            }
        }
        
        //create menu structure
        the_menu.add(undo);
        the_menu.add(redo);
        the_menu.addSeparator();
        the_menu.add(grid);
        the_menu.add(fill);
        the_menu.add(stroke);
    }
    
    /**
     * Populates tool menu items to passed menu.
     * 
     * @param the_menu menu to populate
     */
    private void createToolMenu(final JMenu the_menu) {
        final JMenuItem color = new JMenuItem(COLOR_CHOOSER);
        the_menu.add(color);
        the_menu.addSeparator();
        for (Action t : MY_TOOLS) {
            final JRadioButton item = new JRadioButton(t);
            myToolGroup.add(item);
            the_menu.add(item);
        }
    }
    
    /**
     * Populates help menu items to passed menu.
     * 
     * @param the_menu menu to populate
     */
    private void createHelpMenu(final JMenu the_menu) {
        final JMenuItem about = new JMenuItem("About...");
        about.setMnemonic('A');
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent the_event) {
                JOptionPane.showMessageDialog(MY_FRAME, "PowerPaint HD\nJesse "
                        + "Bostic\nTCSS 305", "General Information", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        the_menu.add(about);
    }
    
    /**
     * Creates toolbar for this GUI.
     */
    private void createToolbar() {
        final JButton color = new JButton(COLOR_CHOOSER);
        MY_TOOLBAR.add(color);
        MY_TOOLBAR.addSeparator();
        for (Action t : MY_TOOLS) {
            final JToggleButton button = new JToggleButton(t);
            MY_TOOLBAR.add(button);
        }
        MY_TOOLBAR.addSeparator();
        MY_TOOLBAR.add(new JButton(myUndoAction));
        MY_TOOLBAR.add(new JButton(myRedoAction));
        MY_FRAME.add(MY_TOOLBAR, BorderLayout.SOUTH);
    }
    
    /**
     * Handles the file opening process.
     */
    private void openFile() {
        final int fileChoice = FILE_CHOOSER.showOpenDialog(MY_FRAME);
        if (fileChoice == JFileChooser.APPROVE_OPTION) {
            ImageIcon image = null;
            try {
                image = new ImageIcon(ImageIO.read(FILE_CHOOSER.getSelectedFile()));
            } catch (final IOException e) {
                JOptionPane.showMessageDialog(MY_FRAME, "Oops! Problem opening file!");
            }
            int choice = JOptionPane.YES_OPTION;
            if (MY_GRAPHICS.isUndoable() || MY_GRAPHICS.isRedoable()) {
                choice = JOptionPane.showConfirmDialog(MY_FRAME, "Loading a new image "
                       + "will reset project!\nAre you sure you want to proceed?", 
                       "Start New Project", JOptionPane.YES_NO_OPTION);
            }
            if (choice == JOptionPane.YES_OPTION) {
                myEasel.clear();
                myImage = image;
                myEasel.repaint();
            }
        }
    }
    
    /**
     * Handles the file saving process.
     * 
     * @param the_delay desired ms of delay before screenshot
     */
    private void saveFile(final int the_delay) {
        BufferedImage image = null;
        final int choice = FILE_CHOOSER.showSaveDialog(MY_FRAME);
        if (choice == JFileChooser.APPROVE_OPTION) {
            try {
                final File file = new File(FILE_CHOOSER.getSelectedFile().getPath());
                if (!file.exists()) {
                    file.createNewFile();
                }
                final Robot robot = new Robot();
                robot.delay(the_delay);
                image = robot.createScreenCapture(new java.awt.Rectangle
                (myEasel.getLocationOnScreen().x, myEasel.getLocationOnScreen().y, 
                    myEasel.getWidth(), myEasel.getHeight()));
                ImageIO.write(image, "png", file);
            } catch (final IOException exception) {
                JOptionPane.showMessageDialog(MY_FRAME, "Oops! File error!");
            } catch (final AWTException exception) {
                JOptionPane.showMessageDialog(MY_FRAME, "Oops! Unknown error!");
            }
            
        
        }
    }
    
    
    /**
     * Class which implements basic drawing panel and listens to mouse events.
     * 
     * @author Jesse Bostic
     * @version 11-13-2013
     */
    @SuppressWarnings("serial")
    public class Easel extends JPanel implements MouseListener, MouseMotionListener {
        
        /**
         * Spacing of lines in grid view.
         */
        private static final int GRID_SPACING = 10;
        
        /**
         * Thickness of grid stroke.
         */
        private static final float GRID_STROKE = .25f;
        
        /**
         * First point where mouse is pressed.
         */
        private Point myPoint1;
        
        /**
         * Second point where mouse is released.
         */
        private Point myPoint2;
        
        /**
         * Object to track pencil tool movement.
         */
        private Path2D.Float myPencilShape;
        
        /**
         * Whether a shape has just finished being drawn on this easel.
         */
        private boolean myJustReleased;
        
        /**
         * Constructor for easel object.
         */
        public Easel() {
            addMouseListener(this);
            addMouseMotionListener(this);
            myPoint1 = null;
            myPoint2 = null;
            myPencilShape = new Path2D.Float();
        }
        
        /**
         * Paints the component appropriate to current state.
         * 
         * @param the_graphic the graphics object for this panel
         */
        @Override
        public void paintComponent(final Graphics the_graphic) {
            
            super.paintComponent(the_graphic);
            final Graphics2D g2d = (Graphics2D) the_graphic;
          
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                 RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (myImage != null) {
                g2d.drawImage(myImage.getImage(), new AffineTransform(), (ImageObserver) this);
            }
            
            //redraw all previous drawn objects
            for (DrawnObject d : MY_GRAPHICS.getList()) {
                g2d.setStroke(d.getStroke());
                g2d.setColor(d.getColor());
                g2d.draw(d.getShape());
                if (d.getFillStatus()) {
                    g2d.fill(d.getShape());
                }
            }
            
            //set current color and stroke values
            myColor = COLOR_CHOOSER.getChosenColor();
            g2d.setColor(myColor);
            g2d.setStroke(myStroke);
            
            //determine which tool is selected and draw
            if (myPoint1 != null && myPoint2 != null) {
                if (getSelectedTool().equals(PENCIL_STRING)) {                    
                    myPencilShape.lineTo(myPoint2.getX(), myPoint2.getY());
                    g2d.draw(myPencilShape);
                    if (myJustReleased) {
                        MY_GRAPHICS.add(new DrawnObject(myPencilShape, myColor, myStroke, 
                                                        myFillEnabled));
                        myJustReleased = false;
                        resetPoints();
                    } 
                } else if (getSelectedTool().equals(LINE_STRING)) {
                    g2d.draw(new Line2D.Double(myPoint1.getX(), myPoint1.getY(), 
                                               myPoint2.getX(), myPoint2.getY()));
                    if (myJustReleased) {
                        final Line2D line = new Line2D.Double(myPoint1.getX(), 
                                                myPoint1.getY(), myPoint2.getX(), 
                                                myPoint2.getY());
                        MY_GRAPHICS.add(new DrawnObject(line, myColor, myStroke, 
                                                        myFillEnabled));
                        myJustReleased = false;
                        resetPoints();
                    }
                } else {  //tool is either rectangle or ellipse
                    final double startX = Math.min(myPoint1.getX(), myPoint2.getX());
                    final double startY = Math.min(myPoint1.getY(), myPoint2.getY());
                    final double width = Math.max(myPoint1.getX(), myPoint2.getX()) - startX;
                    final double height = Math.max(myPoint1.getY(), myPoint2.getY()) - startY;
                    Shape shape;
                    if (getSelectedTool().equals(RECT_STRING)) {
                        shape = new Rectangle2D.Double(startX, startY, width, height);
                    } else { 
                        shape = new Ellipse2D.Double(startX, startY, width, height);
                    }
                    
                    g2d.draw(shape);
                    if (myJustReleased) {
                        MY_GRAPHICS.add(new DrawnObject(shape, myColor, myStroke, 
                                                        myFillEnabled));
                        myJustReleased = false;
                        resetPoints();
                    }
                }
            }
            
          //draw grid if necessary
            if (myGridEnabled) {
                final Dimension window = this.getMaximumSize();
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(GRID_STROKE));
                for (int i = 0; i * GRID_SPACING < window.width; i++) {
                    g2d.drawLine(i * GRID_SPACING, 0, i * GRID_SPACING, window.height);
                }
                for (int i = 0; i * GRID_SPACING < window.height; i++) {
                    g2d.drawLine(0, i * GRID_SPACING, window.width, i * GRID_SPACING);
                }
            }
            
            //check various buttons for enablement
            myUndoAction.setEnabled(MY_GRAPHICS.isUndoable());
            myRedoAction.setEnabled(MY_GRAPHICS.isRedoable());
            for (JMenuItem m : myAbleToDisableTools) {
                m.setEnabled(MY_GRAPHICS.isUndoable() 
                            || MY_GRAPHICS.isRedoable() 
                            || myImage != null);
                
            }
        }
        
        /**
         * Gets the string representation of currently selected tool.
         * 
         * @return string representation of selected drawing tool
         */
        private String getSelectedTool() {
            String temp = "";
            for (final Enumeration<AbstractButton> buttons = myToolGroup.getElements(); 
                    buttons.hasMoreElements();) {
                final AbstractButton button = buttons.nextElement();
                if (button.isSelected()) {
                    temp = button.getText();
                }
                
            }
            return temp;
        }
        
        /**
         * Clears reverter, pencil shape, and points in this easel.
         */
        private void clear() {
            myImage = null;
            MY_GRAPHICS.reset();
            myPencilShape.reset();
            resetPoints();
            repaint();
        }
        
        /**
         * Resets points of this easel.
         */
        public void resetPoints() {
            myPoint1 = null;
            myPoint2 = null;
            repaint();
        }
        
        /**
         * Tracks current point of mouse while dragged.
         * 
         * @param the_event mouse dragged event object
         */
        @Override
        public void mouseDragged(final MouseEvent the_event) {
            myPoint2 = the_event.getPoint();
            repaint();
            
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseMoved(final MouseEvent the_event) {
            
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseClicked(final MouseEvent the_event) {
            
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseEntered(final MouseEvent the_event) {
            
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseExited(final MouseEvent the_event) {
            
        }
        
        /**
         * Makes appropriate initialization upon press event, based on selected shape.
         * 
         * @param the_event mouse pressed event object
         */
        @Override
        public void mousePressed(final MouseEvent the_event) {
            myPoint1 = the_event.getPoint();
            if (getSelectedTool().equals(PENCIL_STRING)) {
                myPencilShape = new Path2D.Float();
                myPencilShape.moveTo(myPoint1.getX(), myPoint1.getY());  
            }
        }
        
        /**
         * Sets endpoint and finalizes drawn shape upon release.
         * 
         * @param the_event mouse released event object
         */
        @Override
        public void mouseReleased(final MouseEvent the_event) {
            myPoint2 = the_event.getPoint();
            myJustReleased = true;
            repaint();
        }
        
    }
}
