// @Author: Kevin Costello
import java.util.*;
import java.io.*;

public class EditDistance {   

   private static class Cell {
      private int value, direction;
      public Cell(int d, int v) {
         direction = d;
         value = v;
      }
   }

   public static void main(String[] args) {
      String fileName = "JUNK";

      if (args.length == 1) {
         fileName = args[0];         
      }
      else {
         System.out.println("Need a file for input");
         System.exit(0);
      }
      makeSeqs(fileName);      
   }

   private static void makeSeqs(String fileName) {
      String seq1 = "EMPTY", seq2 = "EMPTY";
      try {
         File f = new File(fileName);
         Scanner sc = new Scanner(f);
         seq1 = sc.next();
         seq2 = sc.next();
         sc.close();
      }
      catch (Exception e) {
         System.out.println("Couldn't generate the strings, here's what went wrong..");
         System.out.println(e.toString());
         System.exit(0);
      }
     
      makeDistTable(seq1, seq2);
   }

   private static void makeDistTable(String seq1, String seq2) {
      int len1 = 0, len2 = 0;
      Cell[][] cells;
      
      len1 = seq1.length();
      len2 = seq2.length();

      cells = new Cell[len1 + 1][len2 + 1];

      //Setup edit distance table for filling in
      for (int i = 0; i < cells.length; i++) {
         cells[i][0] = new Cell(0, 2 * i);
            
      }      
      for (int i = 0; i < cells[0].length; i++) {
         cells[0][i] = new Cell(0, 2 * i);
      }      

      //r: row, c: column.
      int distance = 0, diagDist = 0, direction = 0;
      int val1, val2, val3;
      for (int r = 1; r < cells.length; r++) {
         for (int c = 1; c < cells[r].length; c++) {
            if (seq1.charAt(r - 1) == seq2.charAt(c - 1))
               diagDist = 0;//current characters match
            else
               diagDist = 1;//mismatch
            
            //Cost to get to the current ceel by coming from the left
            val1 = cells[r][c - 1].value + 2; //Insertion in the first word.

            //Cost to get to the current cell by coming from above
            val2 = cells[r - 1][c].value + 2; //Insertion in the second word.

            //Cost to get to the current cell by coming from
            //dianonally.
            val3 = cells[r - 1][c - 1].value + diagDist; //Matching 2 letters that may/may not match, Diagonal
            
            // Set distance to be the minimum of these 3 values.
            distance = Math.min(val1, Math.min(val2, val3));
            if (distance == val2) { //Up
               direction = 0;
            }
            if (distance == val1) { //Left
               direction = 1;
            }
            if (distance == val3) { //Diagonal
               direction = 2;
            }

            //Create a new cell at the current location with the
            //minimum distance it takes to get here, as well as the
            //direction that was taken to get here.
            cells[r][c] = new Cell(direction, distance);
         }
      }

      //This prints out the cost needed to get to each cell
     /* for (int r = 0; r < cells.length; r++) {
         for (int c = 0; c < cells[r].length; c++) {
            System.out.print(cells[r][c].value + " ");
            //Print out an extra space if number is only 1 digit so
            //all numbers line up. Makes for nice formatting.
            if (cells[r][c].value < 10) System.out.print(" ");
         }
         System.out.println();
      } */ 
  /*   
      // This prints out each cells direction as to where it came
      // from. 0 = up, 1 = left, 2 = diag.
      for (int r = 0; r < cells.length; r++) {
         for (int c = 0; c < cells[r].length; c++) {
            System.out.print(cells[r][c].direction + " ");
         }
         System.out.println();
      }
*/
 //BACKTRACING STEP
      
      String word1 = "";
      String word2 = "";
      int r = cells.length - 1, c = cells[0].length - 1;
      while (r > 0 || c > 0) {// && cells[r][c].value > 0) {
         direction = cells[r][c].direction;
         if (direction == 0) { //Cell got here by coming from above.
            // Indicates a space inserted into the second word.
            word1 = seq1.charAt(r - 1) + word1;
            word2 = "-" + word2;
            r--;
         }
         else if (direction == 1) { // Cell got here by coming from the left.
            // Indicates a space inserted into the first word.
            word1 = "-" + word1;
            word2 = seq2.charAt(c - 1) + word2; 
            c--;
         }
         else if (direction == 2) { //Cell got here by moving diagonally (right and down).
            word1 = seq1.charAt(r - 1) + word1;
            word2 = seq2.charAt(c - 1) + word2;
            r--;
            c--;
         }
         else {
            System.out.println("Invalid direction found in cell");
            System.exit(0);
         }
      }
      //Print out result
      
      System.out.println("Edit distance = " + (cells[seq1.length()][seq2.length()].value));
      
      System.out.print("Please enter a 1 if you would like to see the alignment: ");
      Scanner sc = new Scanner(System.in);
      
      if (sc.nextInt() == 1) { 
         System.out.println("X     Y     Cost");
         System.out.println("----------------");

         //Both created words are supposed to be the same length
         for (int i = 0; i < word1.length(); i++) {
            System.out.print(word1.charAt(i) + "     " + word2.charAt(i) + "     ");
            if (word1.charAt(i) == '-' || word2.charAt(i) == '-') {
               System.out.print("2");
            }
            else if (word1.charAt(i) != word2.charAt(i)) {
               System.out.print("1");
            }
            else System.out.print("0");
         
            System.out.println();
         }
      }
      System.out.println();
   
   } // End makeDistTable
   
} // End class
