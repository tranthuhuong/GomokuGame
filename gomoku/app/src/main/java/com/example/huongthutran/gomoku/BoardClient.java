package com.example.huongthutran.gomoku;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Huong Thu Tran on 4/12/2018.
 */

public class BoardClient {
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint;

    private int[][] board;//step existed ,-1 if none, 0 if player1, 1 if player2 or compu-player
    private int player;//player current
    private Context context;
    private List<Line> lines;
    private int bitmapWidth, bitmapHeight, colQty,rowQty;

    private Bitmap playerA, playerB;

    public BoardClient(Context context, int bitmapWidth, int bitmapHeight, int colQty, int rowQty) {
        this.context = context;
        this.bitmapWidth = bitmapWidth;
        this.bitmapHeight = bitmapHeight;
        this.colQty = colQty;
        this.rowQty = rowQty;
    }

    public void init(){
        lines = new ArrayList<>();
        bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();

        player = 0; //set player 1 play first

        board = new int[rowQty][colQty];
        for(int i = 0; i<rowQty; i++){
            for(int j = 0; j < colQty;j++){
                board[i][j] = -1; //set new board none all step
            }
        }

        paint.setStrokeWidth(2);
        int celWidth = bitmapWidth/colQty;
        int celHeight = bitmapHeight/rowQty;
        for(int i = 0; i <= colQty; i++){
            lines.add(new Line(celWidth*i, 0, celWidth*i, bitmapHeight));
        }
        for(int i = 0; i <= rowQty; i++){
            lines.add(new Line(0, i*celHeight, bitmapWidth, i*celHeight));
        }
    }
    public Bitmap drawBoard(){
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
    public void onDrawBoard(int rowIndex, int colIndex, final View view, int p){
        int cellWidth = 800/8;
        int cellHeight = 800/8;
        int padding = 0;
        //draw icon and change playerr
        if(p==0){
            canvas.drawBitmap(
                    this.playerA,
                    new Rect(0,0,this.playerA.getWidth(), this.playerA.getHeight()),
                    new Rect(colIndex*cellWidth+padding,rowIndex*cellHeight+padding,(colIndex+1)*cellWidth -padding, (rowIndex+1)*cellHeight -padding),
                    paint);
            this.player=1;

        } else{
            canvas.drawBitmap(
                    this.playerB,
                    new Rect(0,0,this.playerB.getWidth(), this.playerB.getHeight()),
                    new Rect(colIndex*cellWidth+padding,rowIndex*cellHeight+padding,(colIndex+1)*cellWidth -padding, (rowIndex+1)*cellHeight -padding),
                    paint);

            this.player=0;

        }

    }
    public int getColIndex(int x,View view){
        int cellWidth = view.getWidth()/colQty;
        return (int) (x/ cellWidth);
    }
    public int getRowIndex(double y,View view){
        int cellHeight = view.getWidth()/rowQty;
        return (int) ((y- ((view.getHeight()-view.getWidth())/2)) / cellHeight );
    }
    public boolean onTouch(View view, MotionEvent motionEvent){
        int cellWidth = view.getWidth()/colQty;
        int cellHeight = view.getWidth()/rowQty;
        int colIndex = (int) (motionEvent.getX() / cellWidth);
        int rowIndex = (int) ((motionEvent.getY()- ((view.getHeight()-view.getWidth())/2)) / cellHeight );

        if(board[rowIndex][colIndex]==-1) {

            board[rowIndex][colIndex] = player;
            // if( checkOneSquare(rowIndex,colIndex,player)) Toast.makeText(context,player+" win",Toast.LENGTH_SHORT).show();
            onDrawBoard(rowIndex, colIndex, view, player);
            //Toast.makeText(context,"Player="+player,Toast.LENGTH_SHORT).show();

        }



        return true;
    }

}
