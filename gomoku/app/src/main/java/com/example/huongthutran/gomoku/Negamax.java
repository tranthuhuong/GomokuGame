package com.example.huongthutran.gomoku;

import android.util.Log;

/**
 * Created by Huong Thu Tran on 4/14/2018.
 */

public class Negamax {
    int c=0;

    public Record mini(GomokuBoard gomokuBoard, int player,int currentDept, int maxDept, int alpha, int beta){
        Move bestMove = null;
        int bestScore=Integer.MIN_VALUE;

        if((gomokuBoard.isGameOver() || currentDept == maxDept )){
            return  new Record(null,gomokuBoard.evaluate());
        }


        for (Move move:gomokuBoard.getMove()){
            GomokuBoard newChess = new GomokuBoard(gomokuBoard.getContext(),gomokuBoard.getBitmapWidth(),gomokuBoard.getBitmapHeight(),gomokuBoard.getColQty(),gomokuBoard.getRowQty());
            newChess.setBoard(gomokuBoard.getNewBoard());
            newChess.setPlayer(gomokuBoard.getPlayer());

            newChess.makeMove(move);

            Record  record = mini(
                    newChess,player,currentDept,maxDept,-alpha,-beta);

            int currentScore = - record.getScore();
            alpha=Math.max(alpha,bestScore);
            if(currentScore > bestScore){
                bestScore = currentScore;
                bestMove = move;
                Log.d("c", String.valueOf(bestScore));
                Log.d("c", String.valueOf(beta));

                alpha = Math.max(alpha, currentScore);
                if (alpha >= beta)   return new Record(bestMove,bestScore);
            }
        }
        return new Record(bestMove,bestScore);
    }

}
