import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

// TODO add documentation
public class PixelatedImage {
   private int size;
   private int grid[][];
   private boolean visited[][];

   // Constructor that sets the array size 
   public PixelatedImage(int s)
   {
      int grid [][]= new int[s][s];

   }

   // TODO add documentation
   public PixelatedImage(String filename)
   {
      try{
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line;
      for(int i = 0; i<)

      }catch(IOException e){
      System.out.println("Sorry ," +e.getMessage());
      }
   }

   // TODO add documentation
   public void loadRandomly()
   {
   }

   // TODO add documentation
   public void loadFromFile(String inFile) throws FileNotFoundException
   {
      File myObj = new File(inFile);
      Scanner scan = new Scanner(myObj);

      String line = scan.nextLine();
      size = Integer.parseInt(line);
      grid = new int[size][size];
      for (int i = 0; scan.hasNext() && i < getHeight(); i++) {
         line = scan.nextLine();
         String[] tokens = line.split(",");
         for (int j = 0; j < Math.min(tokens.length,size); j++) {
            grid[i][j] = Integer.parseInt(tokens[j]);
         }
      }
      scan.close();
   }

   // TODO add documentation
   public int getWidth()
   {
   }

   // TODO add documentation
   public int getHeight()
   {
   }

   // TODO add documentation
   public int getPixel(int row, int col)
   {
   }

   // TODO add documentation
   public void setPixel(int row, int col, int color)
   {
   }

   /**
    * Routine that starts the recursion by finding
    * the color at row,col and passing it to the recursive
    * version as origColor. Creates the visited array to
    * tag cells as they are visited.
    * 
    * @param row row of the grid to visit
    * @param col col of the grid to visit
    * @param fillColor color to use for replacing
    */
   public void floodFill(int row, int col, int fillColor)
   {
      int origColor = grid[row][col];
      visited = new boolean[getHeight()][getWidth()];
      floodFill(row, col, origColor, fillColor);
   }

   // TODO add documentation
   protected void floodFill(int row, int col, int origColor, int fillColor)
   {
   }

}
