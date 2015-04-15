/* File: Player35.java
 * Modified by: Qifan Huang, Xu Sun (huangqf@bu.edu, sunxu@bu.edu)
 * Relative file: Connect4.java
 */

public class Player35 implements PlayerClass{
    
    //constructor
    public Player35() {
    }
    
    //input a board and returns a column that the computer should make a move
    public int move(int [][] b) {
        int best = 0;
        int [][] c = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                c[i][j] = b[i][j];
            }
        }
        int player = 1;
        int val = 100000001;
        for (int i = 0; i < 8; i++) {
            if (c[0][i] == 0) {
                for (int j = 7; j >= 0; j--) {
                    if (c[j][i] == 0) {
                        c[j][i] = 2;      //make a move on each column that is not fully occupied
                        
                        int m = minMax(c, player, 0, 100000000, -100000000);
                        if (m < val) {
                            val = m;
                            best = i;
                        }
                        
                        c[j][i] = 0;      //move back
                        break;
                    }
                }
            }
        }
        
        return best;
    }
    
    //search the minMax tree and return a greatest value where the computer should give a move to realize the value
    private int minMax(int [][] b, int p, int depth, int alpha, int beta) {
        
        //if the board if full, stop working
        boolean full = true;
        for (int i = 0; i < 8; i++) {
            if (b[0][i] == 0) {
                full = false;
                break;
            }
        }
        
        //base case
        if (full || eval(b) == 100000000 || eval(b) == -100000000 || depth == 6) {
            return eval(b);
        } else if (p == 2) {                //search for the minimum value which means computer wins
            int val = 100000001;
            for (int i = 0; i < 8; i++) {
                if (b[0][i] == 0) {
                    for (int j = 7; j >= 0; j--) {
                        if (b[j][i] == 0) {
                            b[j][i] = 2;
                            
                            alpha = min(alpha, val);       //get min value
                            if (beta > alpha) {            //terminate
                                b[j][i] = 0;
                                break;
                            }
                            val = min(val, minMax(b, 1, depth + 1, alpha, beta));
                            b[j][i] = 0;                   //cancel the move
                            break;
                        }
                    }
                }
            }
            
            return val;
        } else {                                            //search for the minimum value which means computer wins
            int val = -100000001; 
            for (int i = 0; i < 8; i++) {
                if (b[0][i] == 0) {
                    for (int j = 7; j >= 0; j--) {
                        if (b[j][i] == 0) {
                            b[j][i] = 1;
                            
                            beta = max(beta, val);          //get max value
                            if (beta > alpha) {             //terminate
                                b[j][i] = 0;
                                break;
                            }
                            val = max(val, minMax(b, 2, depth + 1, alpha, beta));
                            b[j][i] = 0;                     //cancel the move
                            break;
                        }
                    }
                }
            }
            
            return val;
        }
    }
    
    //return a lager value
    private int max(int a, int b) {
        if (a >= b) {
            return a;
        } else {
            return b;
        }
    }
    
    //return a smaller value
    private int min(int a, int b) {
        if (a <= b) {
            return a;
        } else {
            return b;
        }
    }
    
    //evaluate a score for the present board
    public int eval(int [][] board) {
        int score = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 1) {              //if there is a player's piece
                    score = score + evalRaw(board, i, j, 1);    //add the raw score
                } else if (board[i][j] == 2) {       //if there is a computer's piece
                    score = score - evalRaw(board, i, j, 2);    //substract the raw score
                }
            }
        }
        if (score >= 100000000) {                     //100000000 represents for a player's win
            return 100000000;
        } else if (score <= -100000000) {             //-100000000 represents for a computer's win
            return -100000000;
        } else {
            return score;
        }
    }
    
    //evaluate the raw score for each piece and returns the score
    private int evalRaw(int [][] b, int l, int r, int p) {
        int l1 = 0;
        int l2 = 7;
        int r1 = 0;
        int r2 = 7;
        
        if (l - 3 > 0) {
            l1 = l - 3;
        } else {
            l2 = l + 3;
        }
        
        if (r - 3 > 0) {
            r1 = r - 3;
        } else {
            r2 = r + 3;
        }
        
        int rawScore = 0;
        int numX = 0;
        int numO = 0;
        
        //find vertical possibilities
        for (int i = l1; i < (l2 - 2); i++) {
            for (int j = i; j < i + 4; j++) {
                if (b[j][r] == p) {
                    numX += 1;
                } else if (b[j][r] == 3 - p) {
                    numO += 1;
                }
            }
            if (numO == 0) {                    //no opponent's pieces
                if (numX == 4) {                //win
                    return 100000000;
                } else if (numX == 3) {         //3 pieces gets 10000
                    rawScore += 10000;
                } else if (numX == 2) {         //2 pieces gets 500
                    rawScore += 500;
                } else if (numX == 1) {         //1 pieces gets 5
                    rawScore += 5;
                }
            }
            numX = 0;
            numO = 0;
        }
        
        //find horizontal possibilities
        for (int i = r1; i < (r2 - 2); i++) {
            for (int j = i; j < i + 4; j++) {
                if (b[l][j] == p) {
                    numX += 1;
                } else if (b[l][j] == 3 - p) {
                    numO += 1;
                }
            }
            if (numO == 0) {
                if (numX == 4) {
                    return 100000000;
                } else if (numX == 3) {
                    rawScore += 10000;
                } else if (numX == 2) {
                    rawScore += 500;
                } else if (numX == 1) {
                    rawScore += 5;
                }
            }
            numX = 0;
            numO = 0;
        }
        
        //find diagonal possibilities
        for (int i = -3; i < 1; i++) {
            if (l + i > -1 && r + i > -1 && l + i + 3 < 8 && r + i + 3 < 8) {
                for (int j = i; j < i + 4; j++) {
                    if (b[l + j][r + j] == p) {
                        numX += 1;
                    } else if (b[l + j][r + j] == 3 - p) {
                        numO += 1;
                    }
                }
                if (numO == 0) {
                    if (numX == 4) {
                        return 10000000;
                    } else if (numX == 3) {
                        rawScore += 10000;
                    } else if (numX == 2) {
                        rawScore += 500;
                    } else if (numX == 1) {
                        rawScore += 5;
                    }
                }
            }
            numX = 0;
            numO = 0;
        }
        
        for (int i = -3; i < 1; i++) {
            if (l + i > -1 && r - i < 8 && l + i + 3 < 8 && r - i - 3 > -1) {
                for (int j = i; j < i + 4; j++) {
                    if (b[l + j][r - j] == p) {
                        numX += 1;
                    } else if (b[l + j][r - j] == 3 - p) {
                        numO += 1;
                    }
                }
                if (numO == 0) {
                    if (numX == 4) {
                        return 10000000;
                    } else if (numX == 3) {
                        rawScore += 10000;
                    } else if (numX == 2) {
                        rawScore += 500;
                    } else if (numX == 1) {
                        rawScore += 5;
                    }
                }
            }
            numX = 0;
            numO = 0;
        }
        
        return rawScore;
    }
}