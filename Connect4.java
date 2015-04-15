/* File: Connect4.java
 * Author: Shuochen Zheng (U42012371)
 * Team: Shuocheng Zheng -- GUI
 *       Sida Gao -- Evaluation Function(s)
 *       Jun Xu -- MinMax
 * Date: 4/23/2013
 * Class: CS 112, Spring 2013
 * Purpose: This is a GUI for the player class. U can play with it and have fun
 * Associated files: Player12.java
 */


import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.*;

//Connect4 Class
public class Connect4{
  private static int winIndex;
  private static int playrow;
  private static int playcolumn;
  private static int move = 0;
  private static final int Inf = 100000000;
  private static final int MAX_THRESHOLD = Inf;
  private static final int MIN_THRESHOLD = -Inf;
  
  //GUI for the board
  private static JButton slot[][] = new JButton[8][8];
  private static int board[][] = new int[8][8];
  private static Player12 C = new Player12();
  private static void reset(){
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        board[i][j] = 0;
        slot[i][j].setBackground(null);
      }
    } 
  }
  
  //checkWIns method, this makes the four rows yellow
  private static void checkWins(){
    bottom();
    horizontal();
    diagonal();
  }
  
  //diagonally check if it is four
  private static void diagonal(){
    int icount = 0;
    int jcount = 0;
    int kcount = 0;
    int lcount = 0;
    int pos = board[playrow][playcolumn];
    for(int i = 1; i < 4 && playcolumn - i > -1 && playrow - i > -1; i++){
      if(board[playrow-i][playcolumn-i] !=pos){
        break;
      }
      icount++;
    }
    for(int i = 1; i < 4 && playcolumn + i < 8 && playrow - i > -1; i++){
      if(board[playrow-i][playcolumn+i] !=pos){
        break;
      }
      jcount++;
    }
    for(int i = 1; i < 4 && playcolumn + i < 8 && playrow + i < 8; i++){;
      if(board[playrow+i][playcolumn+i] !=pos){
        break;
      }
      kcount++;
    }
    for(int i = 1; i < 4 && playcolumn - i > -1 && playrow + i < 8; i++){
      if(board[playrow+i][playcolumn-i] !=pos){
        break;
      }
      lcount++;
    }
    if(icount + kcount >= 3){
      for(int i = 0; i <= icount; i++){
        slot[playrow-i][playcolumn-i].setBackground(Color.yellow);
        slot[playrow-i][playcolumn-i].setOpaque(true);
      }
      for(int i = 0; i <= kcount; i++){
        slot[playrow+i][playcolumn+i].setBackground(Color.yellow);
        slot[playrow+i][playcolumn+i].setOpaque(true);
      }
    }
    if(jcount + lcount >= 3){
      for(int i = 0; i <= jcount; i++){
        slot[playrow-i][playcolumn+i].setBackground(Color.yellow);
        slot[playrow-i][playcolumn+i].setOpaque(true);
      }
      for(int i = 0; i <= lcount; i++){
        slot[playrow+i][playcolumn-i].setBackground(Color.yellow);
        slot[playrow+i][playcolumn-i].setOpaque(true);
      }
    }
  }
  
  //check the horizontal if there is four
  private static void horizontal(){
    for(int i = 0; i < 4; i++){
      int sum = 0;
      for(int j = 0; j < 4; j++){
        sum = sum + board[playrow][i + j];
      }
      if(sum == 40 || sum == 4){
        for(int j = 0; j < 4; j++){
          slot[playrow][i + j].setBackground(Color.yellow);
          slot[playrow][i + j].setOpaque(true);
        }
      }
    }
  }
  
  //check the bottom to see if it was four
  private static void bottom(){
    if(playrow >4){
      return;
    }
    int sum = 0;
    for(int i = 0; i < 4; i++){
      sum = sum + board[playrow+i][playcolumn];
    }
    if(sum == 4 || sum == 40){
      for(int i = 0; i < 4; i++){
        slot[playrow+i][playcolumn].setBackground(Color.yellow);
        slot[playrow+i][playcolumn].setOpaque(true);
      }
    }
  }
  
  //main 
  public static void main(String[] args){
    JFrame frame=new JFrame("Connect Four");
    frame.setSize(1000,1000);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JLabel header = new JLabel("Connect Four");
    JLabel status = new JLabel("Playing...");
    header.setFont(new Font("Arial",Font.BOLD,30));
    JButton reset = new JButton("Reset"); 
    JButton quit = new JButton("Quit");
    quit.setFont(new Font("TimesRoman", Font.BOLD, 20)); 
    reset.setFont(new Font("TimesRoman", Font.BOLD, 20)); 
    status.setFont(new Font("TimesRoman", Font.BOLD, 20));
    //construction of the board
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        slot[i][j] = new JButton("");
        JBox.setSize(slot[i][j],80,80);
        slot[i][j].setBorder(BorderFactory.createEtchedBorder());
      }
    }
    //forming the JFrame
    JBox body= 
      JBox.vbox(JBox.vglue(),
                JBox.hbox(JBox.hglue(),header,JBox.hglue()),
                JBox.vglue(),
                JBox.hbox(JBox.hglue(),slot[0][0],slot[0][1],slot[0][2],slot[0][3],slot[0][4],slot[0][5],slot[0][6],slot[0][7],JBox.hglue()),
                JBox.hbox(JBox.hglue(),slot[1][0],slot[1][1],slot[1][2],slot[1][3],slot[1][4],slot[1][5],slot[1][6],slot[1][7],JBox.hglue()),
                JBox.hbox(JBox.hglue(),slot[2][0],slot[2][1],slot[2][2],slot[2][3],slot[2][4],slot[2][5],slot[2][6],slot[2][7],JBox.hglue()),
                JBox.hbox(JBox.hglue(),slot[3][0],slot[3][1],slot[3][2],slot[3][3],slot[3][4],slot[3][5],slot[3][6],slot[3][7],JBox.hglue()),
                JBox.hbox(JBox.hglue(),slot[4][0],slot[4][1],slot[4][2],slot[4][3],slot[4][4],slot[4][5],slot[4][6],slot[4][7],JBox.hglue()),
                JBox.hbox(JBox.hglue(),slot[5][0],slot[5][1],slot[5][2],slot[5][3],slot[5][4],slot[5][5],slot[5][6],slot[5][7],JBox.hglue()),
                JBox.hbox(JBox.hglue(),slot[6][0],slot[6][1],slot[6][2],slot[6][3],slot[6][4],slot[6][5],slot[6][6],slot[6][7],JBox.hglue()),
                JBox.hbox(JBox.hglue(),slot[7][0],slot[7][1],slot[7][2],slot[7][3],slot[7][4],slot[7][5],slot[7][6],slot[7][7],JBox.hglue()),
                JBox.vglue(),
                JBox.hbox(JBox.hglue(),reset,JBox.hglue(),quit,JBox.hglue(),status,JBox.hglue())
               );
    frame.add(body); 
    frame.setVisible(true);
    //wait for the button
    JEventQueue events =new JEventQueue();
    events.listenTo(reset,"reset");
    events.listenTo(quit,"quit");
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        events.listenTo(slot[i][j],i+" "+j);
      }
    }
    while(true){
      EventObject event = events.waitEvent();
      String name = events.getName(event);
      if(name.equals("reset")){
        reset();  
      }else if(name.equals("quit")){
        System.exit(1);
      }else{
        String [] tokens = name.split("[ ]+");//using split and get the second token which is the colomn
        int column = Integer.valueOf(tokens[1]);
        int playerRow = 0;//player moves
        for(; playerRow < 8 ; playerRow++){
          if(board[7 - playerRow][column]==0){
            board[7 - playerRow][column] = 1;
            playrow = 7 - playerRow;
            playcolumn = column;
            slot[7 - playerRow][column].setBackground(Color.red);
            slot[7 -playerRow][column].setOpaque(true);
            move++;
            break;
          }
        }//check to see if player wins
        if(playerRow != 8){
          winIndex = C.eval(board);
          System.out.println(winIndex);
          if(winIndex <= MIN_THRESHOLD){
            status.setText("Player wins");
            checkWins();
            try {                                
              Thread.sleep(2000);                         
            } catch(InterruptedException ex) {
              Thread.currentThread().interrupt();
            }
            reset();  
            status.setText("Playing...");
          }
          column = C.move(board);
          int computerRow = 0;//computer moves
          for(; computerRow < 8 ; computerRow++){
            if(board[7 - computerRow][column]==0){
              //System.out.println("computerRow: " + computerRow);
              //printBoard(board, 0);
              board[7 - computerRow][column] = 10;
              playrow = 7 - computerRow;
              playcolumn = column;
              slot[7 - computerRow][column].setBackground(Color.blue);
              System.out.println(7-computerRow);
              slot[7 -computerRow][column].setOpaque(true);
              move++;
              break;
            }
          }//check to see if computer wins
          winIndex = C.eval(board);
          System.out.println(winIndex);
          if(winIndex >= MAX_THRESHOLD){
            status.setText("Computer wins");
            checkWins();
            try {                                
              Thread.sleep(2000);                         
            } catch(InterruptedException ex) {
              Thread.currentThread().interrupt();
            }
            reset();  
            status.setText("Playing...");
          }//check draw
          if(move ==64){
            status.setText("Draw game");
            try {                                
              Thread.sleep(2000);                         
            } catch(InterruptedException ex) {
              Thread.currentThread().interrupt();
            }
            reset();
            status.setText("playing...");
            move = 0;
          }
        }
      }
    }
  }
}