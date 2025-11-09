import org.junit.*;
import static org.junit.Assert.*;

import java.io.*;

/**
 * Test class for Project 1 file 
 */
public class Project1Test {

    private Project1 runner;

    /**
     * creates a game of life with 3x3 grid.
     */
    @Before
    public void setup() {
        runner = new Project1(3, 3);
    }

    /**
     * Checks basic setup of 3x3.
     */
    @Test
    public void testOne() {
        assertEquals(3, runner.numRows());
        assertEquals(3, runner.numCols());
    }

    /**
     * Tests the intial constructor 
     */
    @Test
    public void testSmallGrid() {
        String inputData =
            "3 3\n" +
            "O..\n" +
            "...\n" +
            "...\n";

        runner.loadFromString(inputData);
        assertTrue("Position 0,0 should be alive", runner.isAlive(0, 0));
        runner.nextGeneration();
        Project1.printGrid(runner);
        assertFalse("Position 0,0 should NOT be alive", runner.isAlive(0, 0));
    }

    /**
     * Test loading from a temporary file instead of relying on external data.
     */
    @Test
    public void filetest() throws Exception {
        File temp = File.createTempFile("glider", ".txt");
        PrintWriter pw = new PrintWriter(temp);
        pw.println("3 3");
        pw.println("O..");
        pw.println(".O.");
        pw.println("..O");
        pw.close();

        runner.loadFromFile(temp.getAbsolutePath());
        Project1.printGrid(runner);

        assertTrue("Position (0,0) should be alive", runner.isAlive(0, 0));
        runner.nextGeneration();
        Project1.printGrid(runner);
    }

    /**
     * Test still life method.
     */
    @Test
    public void countStillLifeTest() {
        runner.loadFromString(
            "4 4\n" +
            "OO..\n" +
            "OO..\n" +
            "....\n" +
            "....\n"
        );
        runner.nextGeneration();
        assertEquals(true, runner.isStillLife());

        runner.loadFromString(
            "4 4\n" +
            "....\n" +
            ".O..\n" +
            ".O..\n" +
            ".O..\n"
        );
    }


    /**
     * Test random initialization at probability 0 and 1.
     */
    @Test
    public void testRandomInt() {
        int aliveCount = 0;
        int liveCount = 0;

        runner.randomInitialize(0);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (runner.isAlive(i, j)) {
                    aliveCount++;
                }
            }
        }
        assertEquals(0, aliveCount);

        runner.randomInitialize(1);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (runner.isAlive(i, j)) {
                    liveCount++;
                }
            }
        }
        assertEquals(9, liveCount);
    }

    /**
     * Tests the basic default 3x3 constructor.
     */
    @Test
    public void const2() {
        Project1 run = new Project1();
        assertEquals(3, run.numRows());
        assertEquals(3, run.numCols());
    }

    /**
     * Tests isStillLife() when previousGrid is null.
     */
    @Test
    public void testStillLifeWithNullPreviousGrid() {
        Project1 run = new Project1(3, 3);
        assertFalse("Should return false when previousGrid is null", run.isStillLife());
    }

    /**
     * Tests the main() method execution by preparing the expected file.
     */
    @Test
    public void testMainMethodRuns() throws Exception {
        // Create data/glidder.txt so main() can run
        // 
        File folder = new File("data");
        if (!folder.exists()) {
            folder.mkdir();
        }
        File file = new File(folder, "glidder.txt");
        PrintWriter pw = new PrintWriter(file);
        pw.println("3 3");
        pw.println("O..");
        pw.println(".O.");
        pw.println("..O");
        pw.close();

        Project1.main(new String[0]); 
    }

    /**
     * Tests the isalive method for all conditions 
     */
    @Test
    public void testIsAliveAllBranches() {
        Project1 game = new Project1(2, 2);

        game.loadFromString("2 2\nO.\n..");

        // out-of-bounds  should all be false/ dead
        assertFalse(game.isAlive(-1, 0)); // r < 0
        assertFalse(game.isAlive(0, -1)); // c < 0
        assertFalse(game.isAlive(2, 0));  // r >= grid.length
        assertFalse(game.isAlive(0, 2));  // c >= grid[0].length

        // tests alive condition and dead condition
        assertTrue(game.isAlive(0, 0));   // alive cell (O)
        assertFalse(game.isAlive(1, 1));  // dead cell ().)
    }

    /**
     * Tests print grid method 
     */
    @Test
    public void testPrintGridCoversAliveAndDead() {
        Project1 game = new Project1(2, 2);
        game.loadFromString("2 2\nO.\n.O");
        Project1.printGrid(game); // executes both branches (O and .)
    }
}
