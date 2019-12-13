import javax.sound.midi.Sequence;
import java.util.*;
import java.io.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

/*
Output Format

First output the optimal score of aligning the two sequences. Then output an optimal alignment of the two sequences.

As in PS4.4, a scoring matrix is used to determine the score of an alignment. For this challenge, you are to use the following scoring matrix :

    //will need a helper function, So we have 3 possible test cases here 1. gamma(-,x) 2. gamma(x,-), 3. (x,y)
    //Picture a grid --> either go left, down or diagonal
    if (-,x) || (x,-) == -2, if (x==y) == +3, if(x != y) == -3
 */
//What happens when string are different lengths?
public class Solution {
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);

        String m = scan.nextLine();
        String n = scan.nextLine();

        int match = 3;
        int dashPenalty = (-2);
        int misMatch = (-3);
        //I'm pretty sure java automatically sets all the values to zero in the 2D array
        int[][] dp = Sequence(m, n, match, misMatch, dashPenalty);
        printDNA(m,n,dp, misMatch, dashPenalty);
        //call helper function here

    }

    //are we supposed to be returning the whole matrix????  //MAKE SURE N AND M'S ARE IN THECORRECT POSITIONS
    public static int[][] Sequence(String n, String m, int match, int misMatch, int dashPenalty){
        int row = n.length();
        int column = m.length();

        int dp[][] = new int[n.length()+1][m.length()+1]; //total of O(mn) subproblems

        for(int i = 0; i < n.length()+1; i++){   //this used to be m
            dp[i][0] = i*dashPenalty;
        }
        for(int j = 0; j < m.length()+1; j++){  //this used to be n
            dp[0][j] = j*dashPenalty;
        }
        //2 for loops to initial rows and columns


        for(int i = 1; i <= n.length(); i++){    //this is  "i"
            for(int j = 1; j <= m.length(); j++){     // this is  "j"
                if(n.charAt(i-1) == m.charAt(j-1)){
                    //System.out.println(dp[0][0]);
                    dp[i][j] = dp[i-1][j-1]+match;  // if the characters match +3 to total score
                    //System.out.println(dp[1][1]);
                    //continue;
                }
                else{ //not sure if the -2 and -3 are in the correct spots
                    dp[i][j] = Math.max(Math.max((dp[i-1][j] + dashPenalty), (dp[i][j-1] + dashPenalty))  , (dp[i-1][j-1] + misMatch));
                }
            } // first for loop

        } // second for loop
/*
        for(int i = 0; i <= n.length(); i++){
            for(int j = 0; j <= m.length(); j++){
                System.out.print(dp[i][j] + "\t    ");
            }
            System.out.println("\n");
        }
//        System.out.println(dp[3][4]);
 //       System.out.println(dp[2][3]);
 //       System.out.println(dp[2][3] + misMatch + " Estimate");
*/
        System.out.print(dp[n.length()][m.length()] + "\n");
        return dp;
        //THIS PART IS NOW CORRECT, WE NEED TO OUTPUT THE CORRECT STRINGS NOW, start from the back

    }
    public static void printDNA(String n, String m, int[][] dp, int misMatch, int dashPenalty) {
        //first we need to check length to make sure we are accessing the correct elements
        int temp;
        int length = (n.length() + m.length());
        int s1pos = length; //position tracker for first array
        int s2pos = length; //position tracker for second array
        temp  = length;
        int i = n.length(); //THIS IS I
        int j = m.length(); // THIS IS J
        char[] arr1 = new char[length+1];
        char[] arr2 = new char[length+1];

        //System.out.println(n.charAt(3));
        //System.out.println(m.charAt(4));
        while (!(j == 0 || i == 0)) {
            //System.out.print(i + "'i' \t");
            //System.out.print(j + "'j' \t");
            //System.out.print(s1pos + "   " + s2pos + "\n");

            if (n.charAt(i - 1) == m.charAt(j - 1)) {
                //System.out.println("Hit ONE");
                //System.out.println(n.charAt(i-1));
                //System.out.println(m.charAt(j-1));
                arr1[s1pos--] = n.charAt(i - 1);
                arr2[s2pos--] = m.charAt(j - 1);
                i--;
                j--;

            } else if (dp[i - 1][j - 1] + misMatch == dp[i][j]) { //this means we are not using the dash value
                //System.out.println(dashPenalty + "  1ST ELSE IF");
                arr1[s1pos--] = n.charAt(i - 1);
                arr2[s2pos--] = m.charAt(j - 1);
                i--;
                j--;

            }
            else if (dp[i][j - 1] + dashPenalty == dp[i][j]) {
                //System.out.println(dashPenalty);
                //System.out.println(dp[i-1][j]);
                //System.out.println(dp[i - 1][j] + dashPenalty);
                //System.out.println(dp[i - 1][j] - dashPenalty);

                arr1[s1pos--] = '-';
                arr2[s2pos--] = m.charAt(j - 1);
                j--;

            }
            else if (dp[i - 1][j] + dashPenalty == dp[i][j]) {
                //System.out.println(dashPenalty + "  2ND ELSE IF");
                //System.out.println(dp[i-1][j] + "   VALUE AT THE DP");
                //System.out.println(dp[i][j] + " THE TARGET WE ARE TRYING TO HIT");
                //System.out.println(dp[i - 1][j] + dashPenalty + " PLUS");
                //System.out.println(dp[i - 1][j] - dashPenalty + " MINUS");

                arr1[s1pos--] = n.charAt(i - 1);
                arr2[s2pos--] = '-';
                i--;

            }
        } //now if one of them hits a bound
        //we need to extract our edge cases, similar to the 'merge' in mergesort

        //i == string n length && j == string m length
        //System.out.println("OUTSIDE INITIAL WHILE LOOP");
        //System.out.print(i + "'i' \t");
        //System.out.print(j + "'j' \t");
        //System.out.print(s1pos + "   " + s2pos + "\n");

        while (i > 0) {
            //if (i > 0) {
                arr1[s1pos--] = n.charAt(i - 1);
                arr2[s2pos--] = '-';
                i--;
            //}
        }




        while (j > 0) {
            //if (j > 0) {
                arr1[s1pos--] = '-';
                arr2[s2pos--] = m.charAt(j - 1);
                j--;
            //}
        }




            //System.out.println("BEFORE FINAL PRINT");
            //System.out.print(i + "'i' \t");
            //System.out.print(j + "'j' \t");
            //System.out.print(s1pos + "   " + s2pos + "\n");
        int start = 0; //Because printing is so hard, we have to make sure we start at the correct points
        for(int p = 0; p <length; p++){  //Must account for extra spaces, so a /u000 is a blank space "really java???"
            //System.out.println("Inside error correcting code");
            if(arr1[p] == Character.MIN_VALUE || arr2[p] == Character.MIN_VALUE){
                //System.out.println("increment");
                start++;

            }
        }

        for (int k = start; k <= temp; k++) {
            System.out.print(arr1[k]);
        }
        System.out.println("");
        for (int h = start; h <= temp; h++) {
            System.out.print(arr2[h]);
        }
    }



    // When finding max of three possible values use   Math.max(Math.max(x,y),z);  **
    //need to do iteratively
} //end of the class
