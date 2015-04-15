/* File: Player12.java
 * Author: Jun Xu (U89879711)
 * Team: Shuocheng Zheng -- GUI
 *       Sida Gao -- Evaluation Function(s)
 *       Jun Xu -- MinMax
 * Date: 4/23/2013
 * Class: CS 112, Spring 2013
 * Purpose: This is the code of Player.java of the project
 *          It constructs a class called Player which has two public methods move() 
 *          and eval().
 * Associated files: Connect4.java
 */

public class Player12 implements PlayerClass {
  
  /* private fields ****************************************************************************************************/
  private static final int Inf = 100000000;
  private static final int MAX_THRESHOLD = Inf;
  private static final int MIN_THRESHOLD = -Inf;
  private static final int MAX_DEPTH = 7;                                            // max depth of minmax search
  
  /* move ****************************************************************************************************/
  // choose the best move given a board, output is the column
  public int move(int [][] board) {
    
    int col = -1;
    int val = Integer.MIN_VALUE;
    
    for (int j = 0; j < 8; ++j) {                                                    // return next move with minimal minMax search value
      for (int i = 7; i >= 0; --i) {
        if (board[i][j] == 0) {
          board[i][j] = 10;
          int temp = minMax(board, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
          if (val < temp) { val = temp; col = j; }          
          board[i][j] = 0;
          break;
        }
      }
    }
    
    return col;
    
  }
  
  /* minMax search ****************************************************************************************************/
  // minMax search tree with alpha-beta pruning
  public int minMax(int [][] board, int depth, int alpha, int beta) {
    
    if (isFull(board) ||                                                             // base case
        depth >= MAX_DEPTH || 
        eval(board) >= MAX_THRESHOLD || 
        eval(board) <= MIN_THRESHOLD)
      return eval(board);
    
    int piece;
    int val;
    
    if (depth % 2 == 0) {                                                            // if in max level, i.e., it's your turn
      piece = 10;
      val = Integer.MIN_VALUE;
      
      for(int i = 0 ; i < 8; ++i) {
        for(int j = 0; j < 8; ++j) {
          if(board[i][j] == 0
               && ( (i < 7 && board[i+1][j] != 0) || i == 7) ) {
            board[i][j] = piece;
            
            if (alpha < val) alpha = val;                                            // alpha = max(val, alpha)
            if (beta < alpha) { board[i][j] = 0; break; }
            int temp = minMax(board, depth + 1, alpha, beta);
            if (val < temp) val = temp;                                              // val = max( minMax(board, depth + 1, alpha, beta), val )
            
            board[i][j] = 0;
          }
        }
      }
      
      return val;
      
    }
    
    else {                                                                           // if in min level, i.e., it's AI's turn
      piece = 1;
      val = Integer.MAX_VALUE;
      
      for(int i = 0 ; i < 8; ++i) {
        for(int j = 0; j < 8; ++j) {
          if(board[i][j] == 0
               && ( (i < 7 && board[i+1][j] != 0) || i == 7) ) {
            board[i][j] = piece;
            
            if (beta > val) beta = val;                                              // beta = min(beta, alpha)
            if (beta < alpha) { board[i][j] = 0; break; }            
            int temp = minMax(board, depth + 1, alpha, beta);
            if (val > temp) val = temp;                                              // val = min( minMax(board, depth + 1, alpha, beta), val )
            
            board[i][j] = 0;
          }
        }
      }
      
      return val;
      
    }
  }
  
  /* gameOver ****************************************************************************************************/
  // return true if board is full
  private static boolean isFull(int [][] board) {
    for (int j = 0; j < 8; ++j) {
      if (board[0][j] == 0) return false;
    }
    return true;
  }
  
  /* evaluation function ****************************************************************************************************/
  
  //go along the board
  public static int eval(int[][] board) {
    int val = 0;
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        val = val + value(board, i, j);
      }
    }
    return val;
  }
  
  //calculate 8 directions of a slot
  private static int value(int[][] board, int row, int column){
    int val1 = up(board,row,column);
    int val2  = down(board,row,column);
    int val3 = left(board,row,column);
    int val4 = right(board,row,column);
    int val5 = upleft(board,row,column);
    int val6 = upright(board,row,column);
    int val7 = downleft(board,row,column);
    int val8 = downright(board,row,column);
    //test method
    /*if(val1 >=100000000 ||val2 >=100000000 ||val3 >=100000000 ||val4 >=100000000 ||val5 >=100000000 ||val6 >=100000000 ||
     val7 >=100000000 ||val8 >=100000000){
     System.out.println(row + " " + column);
     System.out.println("val values");
     System.out.println(val1);
     System.out.println(val2);
     System.out.println(val3);
     System.out.println(val4);
     System.out.println(val5);
     System.out.println(val6);
     System.out.println(val7);
     System.out.println(val8);
     }*/
    return val1 + val2 + val3 + val4 + val5 + val6 + val7 + val8; 
  }
  
  //an array of the weight
  private static int[] mult = {0,1,100,100000,100000000};
  
  //all the direction method and calculations
  private static int up(int[][]board, int row, int column){
    if(row < 3){
      return 0;
    }else{
      int comp = 0;
      int player = 0;
      for(int i = 0; i < 4; i ++){
        int a = board[row - i][column];
        if(a == 10)
          comp++;
        if(a == 1)
          player++;
      }
      if(comp == 0 && player != 0 ){
        return 0 - mult[player];
      }if(player == 0 && comp != 0){
        return mult[comp];
      }else{
        return 0;
      }
    }
  }
  
  private static int down(int[][]board, int row, int column){
    if(row > 4 ){
      return 0;
    }else{
      int comp = 0;
      int player = 0;
      for(int i = 0; i < 4; i ++){
        int a = board[row + i][column];
        if(a == 10)
          comp++;
        if(a == 1)
          player++;
      }
      if(comp == 0 && player != 0 ){
        return 0 - mult[player];
      }if(player == 0 && comp != 0){
        return mult[comp];
      }else{
        return 0;
      }
    }
  }
  
  private static int left(int[][]board, int row, int column){
    if(column < 3){
      return 0;
    }else{
      int comp = 0;
      int player = 0;
      for(int i = 0; i < 4; i ++){
        int a = board[row][column - i];
        if(a == 10)
          comp++;
        if(a == 1)
          player++;
      }
      if(comp == 0 && player != 0 ){
        return 0 - mult[player];
      }if(player == 0 && comp != 0){
        return mult[comp];
      }else{
        return 0;
      }
    }
  }
  
  private static int right(int[][]board, int row, int column){
    if(column > 4){
      return 0;
    }else{
      int comp = 0;
      int player = 0;
      for(int i = 0; i < 4; i ++){
        int a = board[row][column + i];
        if(a == 10)
          comp++;
        if(a == 1)
          player++;
      }
      if(comp == 0 && player != 0 ){
        return 0 - mult[player];
      }if(player == 0 && comp != 0){
        return mult[comp];
      }else{
        return 0;
      }
    }
  }
  
  private static int upleft(int[][]board, int row, int column){
    if(row < 3 || column < 3){
      return 0;
    }else{
      int comp = 0;
      int player = 0;
      for(int i = 0; i < 4; i ++){
        int a = board[row - i][column - i];
        if(a == 10)
          comp++;
        if(a == 1)
          player++;
      }
      if(comp == 0 && player != 0 ){
        return 0 - mult[player];
      }if(player == 0 && comp != 0){
        return mult[comp];
      }else{
        return 0;
      }
    }
  }
  
  private static int upright(int[][]board, int row, int column){
    if(row < 3 || column > 4){
      return 0;
    }else{
      int comp = 0;
      int player = 0;
      for(int i = 0; i < 4; i ++){
        int a = board[row - i][column + i];
        if(a == 10)
          comp++;
        if(a == 1)
          player++;
      }
      if(comp == 0 && player != 0 ){
        return 0 - mult[player];
      }if(player == 0 && comp != 0){
        return mult[comp];
      }else{
        return 0;
      }
    }
  }
  
  private static int downleft(int[][]board, int row, int column){
    if(row > 4 || column < 3){
      return 0;
    }else{
      int comp = 0;
      int player = 0;
      for(int i = 0; i < 4; i ++){
        int a = board[row + i][column - i];
        if(a == 10)
          comp++;
        if(a == 1)
          player++;
      }
      if(comp == 0 && player != 0 ){
        return 0 - mult[player];
      }if(player == 0 && comp != 0){
        return mult[comp];
      }else{
        return 0;
      }
    }
  }
  
  private static int downright(int[][]board, int row, int column){
    if(row > 4 || column > 4){
      return 0;
    }else{
      int comp = 0;
      int player = 0;
      for(int i = 0; i < 4; i ++){
        int a = board[row + i][column + i];
        if(a == 10)
          comp++;
        if(a == 1)
          player++;
      }
      if(comp == 0 && player != 0 ){
        return 0 - mult[player];
      }if(player == 0 && comp != 0){
        return mult[comp];
      }else{
        return 0;
      }
    }
  }
  
  
  /* print board ****************************************************************************************************/
  private static void printBoard(int [][] board, int depth) {
    for(int i = 0 ; i < 8; ++i) {
      for(int j = 0; j < depth; ++j)
        System.out.print("   ");
      for(int j = 0; j < 8; ++j)
        System.out.print(board[i][j] + " ");
      System.out.println();
    }
    System.out.println();
  }
  
  /* unit test ****************************************************************************************************/
  // unit test use a fake evaluation function to test all other methods in Player.java
  public static void main(String [] args) {
    
    Player12 red = new Player12();
    int [][] board = {
      { 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 10, 0, 0, 0, 0 },
      { 0, 0, 0, 10, 0, 0, 0, 0 },
      { 0, 0, 0, 1, 0, 0, 0, 0 },
      { 0, 0, 0, 1, 10, 0, 0, 0 },
      { 10, 1, 1, 1, 10, 0, 0, 0 }
    };
    
    printBoard(board, 0);
    System.out.println();
    System.out.println("Best move: " + red.move(board));
    System.out.println("Root of minmax search tree:" + red.minMax(board, 0, Integer.MIN_VALUE, Integer.MAX_VALUE));                                 // set debug = true when debugging
    System.out.println("Evaluation value of this board:" + eval(board));
    System.out.println();
    
    int [][] board2 = {
      { 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 1, 0, 0, 0, 0 }
    };
    
    printBoard(board2, 0);
    System.out.println();
    System.out.println("Evaluation value of this board:" + eval(board2));
    System.out.println("Best move: " + red.move(board2));
    System.out.println("Root of minmax search tree:" + red.minMax(board2, 0, Integer.MIN_VALUE, Integer.MAX_VALUE));                                 // set debug = true when debugging
    
    System.out.println("Evaluation value of this board:" + eval(board2));
    System.out.println();
    
    int [][] board3 = {
      { 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 10, 0, 0, 0, 0 },
      { 0, 0, 0, 10, 10, 0, 0, 0 },
      { 0, 0, 0, 1, 10, 0, 0, 0 },
      { 0, 0, 1, 1, 10, 0, 0, 0 },
      { 0, 1, 1, 1, 10, 0, 0, 0 }
    };
    
    printBoard(board3, 0);
    System.out.println();
    System.out.println("Best move: " + red.move(board3));
    System.out.println("Root of minmax search tree:" + red.minMax(board3, 0, Integer.MIN_VALUE, Integer.MAX_VALUE));                                 // set debug = true when debugging
    System.out.println("Evaluation value of this board:" + eval(board3));
    System.out.println();
    
    int [][] board4 = {
      { 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 10, 0, 0, 0, 0 },
      { 0, 0, 0, 10, 0, 0, 0, 0 },
      { 0, 0, 0, 10, 0, 0, 0, 0 },
      { 0, 0, 0, 1, 0, 0, 0, 0 },
      { 0, 0, 0, 1, 10, 0, 0, 0 },
      { 1, 1, 1, 1, 10, 0, 0, 0 }
    };
    
    printBoard(board4, 0);
    System.out.println();
    System.out.println("Best move: " + red.move(board4));
    System.out.println("Root of minmax search tree:" + red.minMax(board4, 0, Integer.MIN_VALUE, Integer.MAX_VALUE));                                 // set debug = true when debugging
    System.out.println("Evaluation value of this board:" + eval(board4));
    System.out.println();
    
  }
}