package com.example.huongthutran.gomoku;

/**
 * Created by Huong Thu Tran on 4/8/2018.
 */

public class Move {
    //=>one step
    private int rowIndex;
    private int colIndex;

    public Move() {
    }

    public Move(int rowIndex, int colIndex) {
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getColIndex() {
        return colIndex;
    }

    public void setColIndex(int colIndex) {
        this.colIndex = colIndex;
    }
}
