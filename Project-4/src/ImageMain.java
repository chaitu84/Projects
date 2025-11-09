/**
 * ImageMain.java
 * based on code by Lewis/Chase
 *
 * Displays an image to be filled recursively.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * ImageMain wrapper main class used for displaying
 * the PixelatedImage class for Project4 of 
 * ITSC2214 Fall 2025
 * @author Manuel A. Pérez-Quiñones
 */

public class ImageMain {

    /**
     * Main program, run when this class is run as an app.
     * @param args commandline arguments
     */
    public static void main(String[] args)
    {
        new ImageMain("data/image5.csv");
    }

    private int BOX_WIDTH = 20;
    private final int BORDER = 10;

    private JFrame win = null;
    private JComponent drawArea;
    private JLabel message;

    private boolean hasFile = false;
    private String filename = null;
    private PixelatedImage image;
    private int currentColor = 0;       // white

    private String[] colorNames = {"White", "Light Gray", "Gray", "Dark Gray", "Black",
        "Red", "Green", "Blue", "Cyan", "Yellow", "Magenta", "Orange",
        "Pink", "Charlotte Green", "Niner Gold", "Clay Red"};
    private Color[] colors = {
        // 0-3
        Color.WHITE, Color.LIGHT_GRAY, Color.GRAY, Color.DARK_GRAY, 
        Color.BLACK, // 4
        // 5-7
        Color.RED, Color.GREEN, Color.BLUE,
        // 8-10
        Color.CYAN, Color.YELLOW, Color.MAGENTA,
        // 11
        Color.ORANGE,
        // 12-15
        Color.PINK,
        new Color(0,80,53), // "Charlotte Green"
        new Color(164, 150, 101), // "Niner Gold"
        new Color(128, 47, 45) // "Clay Red"}
    };

    /**
     * ImageMain constructor for images loaded from a files.
     */
    public ImageMain(String inFile) 
    {
        // This loads the file for Project 4
        image = new PixelatedImage(10);
        filename = inFile;
        loadFromFile(inFile);
        hasFile = true;
        setupWindow();
    }

    /**
     * ImageMain constructor for randomly generated imges.
     */
    public ImageMain() 
    {
        // This loads the file for Project 4
        image = new PixelatedImage(10);
        image.loadRandomly();
        hasFile = false;
        filename = null;
        setupWindow();
    }

    /**
     * loadFile and set things up
     * @param inFile
     */
    private void loadFromFile(String file)
    {
        try {
            image.loadFromFile(file);
            filename = file;
        }
        catch (FileNotFoundException f) {}
    }

    /**
     * Setup the GUI window with display of the image and
     * buttons.
     * @param filename name of the file to be loaded, could be null
     */
    private void setupWindow()
    {
        // create window with border layout
        win = new JFrame("FloodFill");
        win.getContentPane().setLayout(new BorderLayout());

        // if we have a file, then set the window
        // title acordingly.
        if (filename == null) {
            win.setTitle("FloodFill - Random image");
        }
        else {
            win.setTitle("FloodFill - Image: "+filename);
        }

        // Panel on the north side of the window to hold
        // buttons.
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        // New button, creates a whole new window like this one
        JButton newWindow = new JButton("New");
        newWindow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                new ImageMain();
            }
        });

        // Load - select a file and open that file in this
        // window (load file).
        JButton loadFile = new JButton("Load");
        loadFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae)  {
                JFileChooser fileChooser = new JFileChooser();
                // Opens in user's home directory
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    // Now you can work with the selectedFile, e.g., read its content
                    loadFromFile(selectedFile.getAbsolutePath());
                    win.setTitle("FloodFill - Image: "+selectedFile.getName());
                    message.setText("Image loaded");
                    drawArea.repaint();
                }
            }});

        JButton reload = new JButton("Reload");
        reload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (hasFile) {
                    loadFromFile(filename);
                    message.setText("Image reloaded.");
                }
                else {
                    image.loadRandomly();
                    message.setText("");
                    win.setTitle("FloodFill - Random image");
                }
                win.repaint();
            }
        });

        // New Open Reload buttons
        panel.add(newWindow);
        panel.add(loadFile);
        panel.add(reload);
        win.getContentPane().add(panel, BorderLayout.NORTH);


        // The south part of the border layout shows
        // a message and a color picker. The message is for updates
        // of what the code is doing and the pop up list is for
        // selecting colors.
        panel = new JPanel();
        message = new JLabel("");
        panel.add(message);

        JComboBox<String> dropdown = new JComboBox<>(colorNames);
        panel.add(dropdown);
        win.getContentPane().add(panel, BorderLayout.SOUTH);

        dropdown.addActionListener(e -> { currentColor = dropdown.getSelectedIndex(); });

        // The center part of the border layout contains
        // the grid area of the image.
        drawArea = new JComponent() {
            public void paintComponent(Graphics g) {
                // get the width of the panel, substract the border
                // we leave around the edges and then divide by the
                // number of cells the image has... This is the
                // size of each cell on the screen, stored in BOX_WIDTH
                BOX_WIDTH = (Math.min(getWidth(), getHeight()) - 2 * BORDER) / image.getWidth();
                for (int i = 0; i < image.getHeight(); i++) {
                    for (int j = 0; j < image.getWidth(); j++) {
                        // draw square
                        drawCell(g, i, j);
                    }
                }
            }

            public void drawCell(Graphics g, int row, int col) {
                // Fill a cell in the color selected from the dropdown
                g.setColor(colors[image.getPixel(row, col)]);
                g.fillRect(BORDER + col * BOX_WIDTH, BORDER 
                    + row * BOX_WIDTH, BOX_WIDTH, BOX_WIDTH);
                // then set the color to black and draw a box
                // around the cell
                g.setColor(Color.black);
                g.drawRect(BORDER + col * BOX_WIDTH, BORDER 
                    + row * BOX_WIDTH, BOX_WIDTH, BOX_WIDTH);
            }
        };

        drawArea.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                int col = (arg0.getX() - BORDER) / BOX_WIDTH;
                int row = (arg0.getY() - BORDER) / BOX_WIDTH;
                message.setText("FloodFill "+row+" x "+col+" in "+currentColor);
                image.floodFill(row, col, currentColor);
                drawArea.repaint();
            }
        });
        win.getContentPane().add(drawArea, BorderLayout.CENTER);

        // Set the window size to fit everything snuggly
        win.setSize(400,500);
        win.setVisible(true);
    }
}
