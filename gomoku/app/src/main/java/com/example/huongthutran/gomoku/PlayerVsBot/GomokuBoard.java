package com.example.huongthutran.gomoku.PlayerVsBot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.huongthutran.gomoku.Line;
import com.example.huongthutran.gomoku.Move;
import com.example.huongthutran.gomoku.R;
import com.example.huongthutran.gomoku.Record;
import com.example.huongthutran.gomoku.onePlayerActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Huong Thu Tran on 4/3/2018.
 */

public class GomokuBoard {
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint;
    private int[][] board;//cac buoc đã đi -1 là chưa đi, 0 la nguoi choi, 1 la may
    private int player;//nguoi choi nào
    private Context context;
    private int bitmapWidth, bitmapHeight, colQty,rowQty;
    private List<Line> lines;
    private Negamax negamax;
    private int qltWin = 5;
    private int winner = -1;
    private Move tempMove;

    //gọi nhiều lần
    private Bitmap playerA, playerB;

    public GomokuBoard(Context context, int bitmapWidth, int bitmapHeight, int rowQty, int colQty) {
        this.context = context;
        this.bitmapWidth = bitmapWidth;
        this.bitmapHeight = bitmapHeight;
        this.colQty = colQty;
        this.rowQty = rowQty;
    }

    public void init(){
        negamax = new Negamax();
        tempMove = null;
        lines = new ArrayList<>();
        bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        board = new int[rowQty][colQty];

        for(int i = 0; i<rowQty; i++){
            for(int j = 0; j < colQty;j++){
                board[i][j] = -1;//-1 là chưa đi
            }
        }

        player = 0;
        paint.setStrokeWidth(2);
        int celWidth = 200;//bitmapWidth/colQty;
        int celHeight =200;// bitmapHeight/rowQty;
        for(int i = 0; i <= colQty; i++){
            lines.add(new Line(celWidth*i, 0, celWidth*i, bitmapHeight));
        }
        for(int i = 0; i <= rowQty; i++){
            lines.add(new Line(0, i*celHeight, bitmapWidth, i*celHeight));
        }
    }

    public Bitmap drawBoard() {
        for(int i = 0; i < lines.size(); i++){
            canvas.drawLine(
                    lines.get(i).getX1(),
                    lines.get(i).getY1(),
                    lines.get(i).getX2(),
                    lines.get(i).getY2(),
                    paint
            );
        }
        playerA = BitmapFactory.decodeResource(context.getResources(),R.drawable.o_tick);
        playerB = BitmapFactory.decodeResource(context.getResources(),R.drawable.x_tick);

        return bitmap;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        int cellWidth = view.getWidth()/colQty;
        int cellHeight = view.getWidth()/rowQty;
        int colIndex = (int) (motionEvent.getX() / cellWidth);
        int rowIndex = (int) ((motionEvent.getY()- ((view.getHeight()-view.getWidth())/2)) / cellHeight );


        if(board[rowIndex][colIndex] != -1){
            return true;
        }

        onDrawBoard(rowIndex, colIndex, view);
        view.invalidate();
        board[rowIndex][colIndex] = player;
        player = (player+1)%2;
        if(isGameOver()){
            winner=evaluate();
            if (winner == 1) {
                Toast.makeText(context, "YOU WIN!!!!", Toast.LENGTH_LONG).show();
            } else if (winner == -1) {
                Toast.makeText(context, "YOU LOST!!!!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "DRAWWW!!", Toast.LENGTH_LONG).show();
            }
        }
        return true;
    }

    public boolean onTouchBot(View view, MotionEvent motionEvent) {

        int count = getCurrentDept();
        final int currentDetp = rowQty*colQty - count;

        Record record = negamax.negamax(
                GomokuBoard.this,
                currentDetp,
                colQty*rowQty,
                Integer.MIN_VALUE,
                Integer.MAX_VALUE
        );
        if(record.getMove() == null){
            return true;
        }
        onDrawBoard(record.getMove().getRowIndex(), record.getMove().getColIndex(), view);
        view.invalidate();
        board[record.getMove().getRowIndex()][record.getMove().getColIndex()] = player;

        player = (player+1)%2;
        if(isGameOver()){
            winner=evaluate();
            if (winner == -1) {
                Toast.makeText(context, "YOU LOST", Toast.LENGTH_LONG).show();
            } else if (winner == 1) {
                Toast.makeText(context, "YOU WIN", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "DRAWWW!!", Toast.LENGTH_LONG).show();
            }
        }
        return true;
    }

    public void onDrawBoard(int rowIndex, int colIndex, View view){
        int cellWidth = 200;//view.getWidth() / colQty;
        int cellHeight = 200;//view.getHeight() / rowQty;
        int padding = 0;

        if(player == 0){
            canvas.drawBitmap(
                    playerA,
                    new Rect(0,0,playerA.getWidth(), playerA.getHeight()),
                    new Rect(colIndex*cellWidth+padding,rowIndex*cellHeight+padding,(colIndex+1)*cellWidth -padding, (rowIndex+1)*cellHeight -padding),
                    paint);
        }else {
            canvas.drawBitmap(
                    playerB,
                    new Rect(0, 0, playerB.getWidth(), playerB.getHeight()),
                    new Rect(colIndex * cellWidth, rowIndex * cellHeight, (colIndex + 1) * cellWidth, (rowIndex + 1) * cellHeight),
                    paint);
        }
    }
    public List<Move> getMove() {
        //tạo mới 1 danh sách, duyệt qua từng vị trí, nếu -1 còn vị trí đi
        List<Move> moves = new ArrayList<>();
        for (int i = 0; i < rowQty; i++) {
            for (int j = 0; j < colQty; j++) {
                if (board[i][j] == -1) moves.add(new Move(i, j));//có thể đi dc
            }
        }
        return moves;
    }

    //ghi nhận nước đi.gán nước đi đó là player nào.
    public void makeMove(Move move) {
        tempMove = move;
        board[move.getRowIndex()][move.getColIndex()] = player;
        player = (player + 1) % 2;

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

    private boolean checkWin(int player) {
        if(board[0][0]==player && board[0][1]==player && board[0][2]==player)
            return true;

        if(board[1][0]==player && board[1][1]==player && board[1][2]==player)
            return true;

        if(board[2][0]==player && board[2][1]==player && board[2][2]==player)
            return true;

        if(board[0][0]==player && board[1][0]==player && board[2][0]==player)
            return true;

        if(board[0][1]==player && board[1][1]==player && board[2][1]==player)
            return true;

        if(board[0][2]==player && board[1][2]==player && board[2][2]==player)
            return true;

        if(board[0][0]==player && board[1][1]==player && board[2][2]==player)
            return true;

        if(board[0][2]==player && board[1][1]==player && board[2][0]==player)
            return true;

        return false;
    }

    public int  evaluate() {
        if (checkWin(player))
            return 1;
        if (checkWin((player + 1) % 2))
            return -1;
        return 0;
    }


    public int[][] getNewBoard(){
        int[][] newBoard = new int[rowQty][colQty];
        for (int i = 0; i < rowQty; i++) {
            for (int j = 0; j < colQty; j++) {
                newBoard[i][j] = board[i][j];
            }
        }
        return newBoard;
    }
    public int getCurrentDept(){
        int count = 0;
        for (int i = 0; i < rowQty; i++) {
            for (int j = 0; j < colQty; j++) {
                if (board[i][j] == -1) count++;
            }
        }
        return count;
    }

    public Boolean checkRow(int row){
        int dem = 0;
        for (int i = 1; i < rowQty; i++) {
            if (board[row][i] != board[row][i-1]) {
                dem = 0;
            }else if(board[row][i] != -1){
                dem++;
            }
            if (dem == qltWin) {
                winner = board[row][i];
                return true;
            }
        }

        return false;
    }
    public Boolean checkCol(int col){
        int dem = 0;
        for (int i = 1; i < rowQty; i++) {
            if (board[i][col] != board[i-1][col]) {
                dem = 0;
            }else if(board[i][col] != -1){
                dem++;
            }
            if (dem == qltWin) {
                winner = board[i][col];
                return true;
            }
        }

        return false;
    }
    public Boolean checkDiagonalRight(int row, int col){
        int a, b;
        int i = 0;
        int count = 0;

        if (row > col) {
            a = row - col;
            b = 0;
        } else {
            a = 0;
            b = col - row;
        }

        while (a + i + 1 < colQty && b + i + 1 < rowQty) {
            if (board[a + i][b + i] == board[a + i + 1][b + i + 1] && board[a + i][b + i] != -1) {
                count++;

                if (count == qltWin) {
                    winner = board[a + i][b + i];
                    return true;
                }
            } else {
                count = 0;
            }
            i++;
        }

        return false;
    }
    public Boolean checkDiagonalLeft(int row, int col){
        int a, b;
        int i = 0;
        int count = 0;

        if (row + col < colQty - 1) {
            b = row + col;
            a = 0;
        } else {
            b = colQty - 1;
            a = col + row - (colQty - 1);
        }

        while (b - i - 1 >= 0 && a + i + 1 < colQty) {
            if (board[a + i][b - i] == board[a + i + 1][b - i - 1] && board[a + i][b - i] != -1) {
                count++;

                if (count == qltWin) {
                    winner = board[a + i][b - i];
                    return true;
                }
            } else {
                count = 0;
            }

            i++;
        }
        return false;
    }
    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public int getColQty() {
        return colQty;
    }

    public void setColQty(int colQty) {
        this.colQty = colQty;
    }

    public int getRowQty() {
        return rowQty;
    }

    public void setRowQty(int rowQty) {
        this.rowQty = rowQty;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getBitmapWidth() {
        return bitmapWidth;
    }

    public void setBitmapWidth(int bitmapWidth) {
        this.bitmapWidth = bitmapWidth;
    }

    public int getBitmapHeight() {
        return bitmapHeight;
    }

    public void setBitmapHeight(int bitmapHeight) {
        this.bitmapHeight = bitmapHeight;
    }

    boolean checkOneSquare(int x,int y,int player){
        for(int i=x;i<x+qltWin;i++){
            if(board[i][y]==player && board[i][y+1]==player && board[i][y+2]==player && board[i][y+3]==player && board[i][y+4]==player)
                return true;
        }
        for(int j=y;j<y+qltWin;j++){
            if(board[x][j]==player && board[x+1][j]==player && board[x+2][j]==player && board[x+3][j]==player && board[x+4][j]==player)
                return true;
        }
        if(board[x][y]==player &&board[x+1][y+1]==player && board[x+2][y+2]==player && board[x+3][y+3]==player &&board[x+4][y+4]==player)
            return true;
        if(board[x+4][y]==player &&board[x+3][y+1]==player && board[x+2][y+2]==player && board[x+1][y+3]==player &&board[x+0][y+4]==player )
            return true;

        return false;
    }
    boolean checkWin1(int p){

        for(int i = 0 ;i <= colQty-qltWin ; i++){
            for(int j=0;j<=rowQty-qltWin;j++){
                if(checkOneSquare(i,j,p)) return true;
            }
        }
        return false;
    }
}
