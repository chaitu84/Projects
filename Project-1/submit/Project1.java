import itsc2214.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

/**
 * Implements Conway's Game of Life using a 2D array.
 */
public class Project1 implements GameOfLife {

    private boolean[][] grid;
    private boolean[][] newgrid;
    private boolean[][] previousGrid;

    /**
     * Randomly initialize live cells based on probability.
     * @param aliveProbability chance for each cell to be alive
     */
    @Override
    public void randomInitialize(double aliveProbability) {
        Random rand = new Random();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                double value = rand.nextDouble();
                grid[i][j] = value < aliveProbability;
            }
        }
    }

    /**
     * Counts the amount of live neighbors around a cell.
     * @param r row index
     * @param c column index
     * @return number of live neighbors
     */
    @Override
    public int countLiveNeighbors(int r, int c) {
        int count = 0;
        int[] d = {-1, 0, 1};

        for (int dr : d) {
            for (int dc : d) {
                if (dr == 0 && dc == 0) {
                    continue;
                }
                int newRow = r + dr;
                int newCol = c + dc;

                if (newRow >= 0 && newRow < grid.length
                        && newCol >= 0 && newCol < grid[0].length) {
                    if (grid[newRow][newCol]) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Checks if the element in row r and column c is alive.
     * @param r row index
     * @param c column index
     * @return true if alive, false if dead
     */
    @Override
    public boolean isAlive(int r, int c) {
        if (r < 0 || c < 0
                || r >= grid.length
                || c >= grid[0].length) {
            return false;
        } else if (grid[r][c]) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the previous and current generation are unchanged.
     * @return true if still life, false otherwise
     */
    @Override
    public boolean isStillLife() {
        if (previousGrid == null) {
            return false; // no previous state yet
        }

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] != previousGrid[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Sets the grid configuration from string values.
     * @param data string representation of the grid
     */
    @Override
    public void loadFromString(String data) {
        Scanner scan = new Scanner(data);

        // First line: "3 3"
        int rows = scan.nextInt();
        int cols = scan.nextInt();
        scan.nextLine(); // move to the next line

        grid = new boolean[rows][cols];

        // Read each row of the grid
        for (int i = 0; i < rows; i++) {
            String line = scan.nextLine().trim();
            for (int j = 0; j < cols; j++) {
                grid[i][j] = line.charAt(j) == 'O';
            }
        }
    }

    /**
     * Loads file and reads it into String which then builds the grid.
     * @param filename input file path
     * @throws FileNotFoundException if file not found
     */
    @Override
    public void loadFromFile(String filename) throws FileNotFoundException {
        StringBuilder build = new StringBuilder();
        File file = new File(filename);
        Scanner scan = new Scanner(file);

        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            build.append(line).append("\n");
        }
        scan.close();
        loadFromString(build.toString());
    }

    /**
     * Computes the next generation of the grid based on Conway's rules.
     */
    @Override
    public void nextGeneration() {
        // save current state for reference
        previousGrid = new boolean[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            System.arraycopy(grid[i], 0, previousGrid[i], 0, grid[i].length);
        }

        // compute next generation
        newgrid = new boolean[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                int neighbors = countLiveNeighbors(i, j);

                if (grid[i][j]) {
                    newgrid[i][j] = neighbors == 2 || neighbors == 3;
                } else {
                    newgrid[i][j] = neighbors == 3;
                }
            }
        }

        // now move forward: newgrid becomes grid
        grid = newgrid;
    }

    /**
     * Gets the number of columns in the grid.
     * @return number of columns
     */
    @Override
    public int numCols() {
        return grid[0].length;
    }

    /**
     * Gets the number of rows in the grid.
     * @return number of rows
     */
    @Override
    public int numRows() {
        return grid.length;
    }

    /**
     * Prints the grid to the console.
     * @param game GameOfLife instance
     */
    public static void printGrid(GameOfLife game) {
        for (int i = 0; i < game.numRows(); i++) {
            for (int j = 0; j < game.numCols(); j++) {
                if (game.isAlive(i, j)) {
                    System.out.print("O");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Runs the Game of Life simulation with a test file.
     * @param args command line arguments
     * @throws FileNotFoundException if input file not found
     */
    public static void main(String[] args) throws FileNotFoundException {
        GameOfLife game = new Project1();
        game.loadFromFile("data/glidder.txt"); // loads the file
        printGrid(game);

        for (int g = 0; g < 5; g++) {
            game.nextGeneration();
            printGrid(game);

            if (game.isStillLife()) {
                System.out.println("Reached still life at generation " + (g + 1));
                break;
            }
        }
    }

    /**
     * Default constructor (3x3 grid).
     */
    public Project1() {
        int rows = 3;
        int cols = 3;
        grid = new boolean[rows][cols];
        newgrid = new boolean[rows][cols];
    }

    /**
     * Constructor with custom dimensions.
     * For data sets that have 7 by 7 or 6 by 6 etc.
     * @param rows number of rows
     * @param cols number of columns
     */
    public Project1(int rows, int cols) {
        grid = new boolean[rows][cols];
        newgrid = new boolean[rows][cols];
    }
}
