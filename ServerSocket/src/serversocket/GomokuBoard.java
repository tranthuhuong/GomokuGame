/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serversocket;

import java.io.Serializable;

/**
 *
 * @author Huong Thu Tran
 */
public class GomokuBoard implements Serializable{
    private int player;
    private int bitmapWidth, bitmapHeight, colQty,rowQty;
    private final int[][] board;
    public GomokuBoard(int colQty, int rowQty){
        this.colQty=colQty;
        this.rowQty=rowQty;
        board = new int[rowQty][colQty];
        for(int i = 0; i<rowQty; i++){
            for(int j = 0; j < colQty;j++){
                board[i][j] = -1; //set new board none all step
            }
        }
    }
    boolean checkStep(int c,int r){
        if(board[c][r]==-1) return true;
        return false;
    }
    void setStep(int c,int r,int p){
        
        board[c][r]=p;
    }
    
    boolean checkOneSquare(int x,int y,int player){
        for(int i=x;i<x+5;i++){
            if(board[i][y]==player && board[i][y+1]==player && board[i][y+2]==player && board[i][y+3]==player && board[i][y+4]==player)
                return true;
        }
        for(int j=y;j<y+5;j++){
            if(board[x][j]==player && board[x+1][j]==player && board[x+2][j]==player && board[x+3][j]==player && board[x+4][j]==player)
                return true;
        }
        if(board[x][y]==player &&board[x+1][y+1]==player && board[x+2][y+2]==player && board[x+3][y+3]==player &&board[x+4][y+4]==player)
            return true;
        if(board[x+4][y]==player &&board[x+3][y+1]==player && board[x+2][y+2]==player && board[x+1][y+3]==player &&board[x+0][y+4]==player )
            return true;

        return false;
    }
    boolean checkWin(int p){
       
        for(int i = 0 ;i <= colQty-5 ; i++){
            for(int j=0;j<=rowQty-5;j++){
                if(checkOneSquare(i,j,p)) return true;
            }
        }
        return false;
    }
    //ktra coi có hết game chưa
    public boolean isGameOver(){
        if (checkWin(0) || checkWin(1)) return true;

        int count = 0;
        for (int i = 0; i < rowQty; i++) {
            for (int j = 0; j < colQty; j++) {
                if (board[i][j] == -1) count++;
            }
        }
        if (count == 0){
            return true;//trò chơi kết thúc
        }
        //chưa thắng hoặc còn vị trí để đi=>game chưa kết thúc
        return false;
    }
    //dánh giá bàn cờ
    public int  evaluate(int player) {
        if (checkWin(player))
            return 1;
        if (checkWin((player + 1) % 2))
            return -1;
        return 0;
    }
    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }
    public String getBoardToString(){
        String str="";
        for (int i = 0; i < rowQty; i++) {
            for (int j = 0; j < colQty; j++) {
                switch (board[i][j]) {
                    case -1:
                        str+="-";
                        break;
                    case 0:
                        str+="0";
                        break;
                    default:
                        str+="1";
                        break;
                }
            }
        }
        return str;
    }
}
