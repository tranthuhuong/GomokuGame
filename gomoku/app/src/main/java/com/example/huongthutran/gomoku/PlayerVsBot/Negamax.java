package com.example.huongthutran.gomoku.PlayerVsBot;

import android.util.Log;

import com.example.huongthutran.gomoku.Move;
import com.example.huongthutran.gomoku.Record;

/**
 * Created by Huong Thu Tran on 4/14/2018.
 */

public class Negamax {
    public Record negamax(GomokuBoard gomokuBoard, int currentDept, int maxDept, int alpha, int beta){
        Move bestMove = null;
        int bestScore;

        if(gomokuBoard.isGameOver() || currentDept >= maxDept){
            return  new Record(null,gomokuBoard.evaluate());
        }

        bestScore = Integer.MIN_VALUE;
        for (Move move:gomokuBoard.getMove()){
            GomokuBoard newGomokuBoard = new GomokuBoard(
                    gomokuBoard.getContext(),
                    gomokuBoard.getBitmapWidth(),
                    gomokuBoard.getBitmapHeight(),
                    gomokuBoard.getColQty(),
                    gomokuBoard.getRowQty()
            );

            newGomokuBoard.setBoard(gomokuBoard.getNewBoard());
            newGomokuBoard.setPlayer(gomokuBoard.getPlayer());

            int a, b;
            if (alpha == Integer.MIN_VALUE) {
                b = Integer.MAX_VALUE;
            } else if (alpha == Integer.MAX_VALUE){
                b = Integer.MIN_VALUE;
            } else {
                b = -alpha;
            }

            if (beta == Integer.MIN_VALUE) {
                a = Integer.MAX_VALUE;
            } else if (beta == Integer.MAX_VALUE) {
                a = Integer.MIN_VALUE;
            } else {
                a = -beta;
            }
            newGomokuBoard.makeMove(move);
            Record record = negamax(
                    newGomokuBoard,
                    currentDept + 1,
                    maxDept,
                    a,
                    b
            );

            int currentScore = - record.getScore();//do dang ktra o thang khac,

            if(currentScore > bestScore){
                bestScore = currentScore;
                bestMove = move;
            }
            alpha = Math.max(alpha, currentScore);

            if (alpha >= beta) {
                return new Record(bestMove,bestScore);
            }
        }
        return new Record(bestMove,bestScore);
    }
}
