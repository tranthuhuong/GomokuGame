package com.example.huongthutran.gomoku;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TwoPlayerActivity extends AppCompatActivity {
    private ImageView imgView;
    private BoardClient boardClient;
    private Socket socket;
    private Scanner scanner;
    private int player=0;
    private int p=0;//hiển thị thằng đang đc đánh
    private String msgLog="";
    private int flag=-1; //nếu -1 không đc đi=> dang chờ đối phương đánh, 1 đc đi
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_player);
        imgView=findViewById(R.id.imgViewGomoku2);

        boardClient = new BoardClient(getApplicationContext(), 800,800,8,8);
        boardClient.init();

        imgView.setImageBitmap(boardClient.drawBoard());
        final ClientThread clientThread = new ClientThread( "192.168.0.180", 8888);
        clientThread.start();
        imgView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if(flag==-1) {
                        Toast.makeText(TwoPlayerActivity.this,"can't let go next step!",Toast.LENGTH_SHORT).show();
                         return  true;
                    }
                    int colIndex=boardClient.getColIndex((int)motionEvent.getX(),view);
                    int rowIndex=boardClient.getRowIndex((int)motionEvent.getY(),view);
                    clientThread.sendMsg(colIndex+" - "+rowIndex);
                    imgView.invalidate();
                    return true;
                }
               return true;

            }
        });
    }
    void resetView(){

    }


    private class ClientThread extends Thread {

        String dstAddress;
        int dstPort;
        int col,row;
        String msgToSend="";
        boolean goOut = false;

        ClientThread( String address, int port) {
            dstAddress = address;
            dstPort = port;
        }

        @Override
        public void run() {
            Socket socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;

            try {
                socket = new Socket(dstAddress, dstPort);
                dataOutputStream = new DataOutputStream(
                        socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
                //nhận thông báo lượt chơi
                msgLog = dataInputStream.readUTF();
                if(msgLog.trim().equals("You're PLAYERA")){
                    player=0;
                    flag=1;
                } else {
                    player=1;
                    flag=-1;
                }
                Log.d("Test",msgLog);
                Log.d("Test",player+"-"+flag);
                TwoPlayerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imgView.invalidate();
                        Toast.makeText(TwoPlayerActivity.this,msgLog,Toast.LENGTH_LONG).show();
                    }
                });

                while (!goOut) {

                    if (dataInputStream.available() > 0) {
                        Log.d("Test flag ",flag+"");
                        //nhận thông báo bước đi của server
                        msgLog = dataInputStream.readUTF();
                        String str[]=msgLog.split(" - ");
                        Log.d("Test", msgLog);
                        p=Integer.parseInt(str[0]);
                        int col=Integer.parseInt(str[1]);
                        int row=Integer.parseInt(str[2]);
                        String status=str[3];
                        switch (status.trim()){
                            case "non":
                                TwoPlayerActivity.this.runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        if(p==player){
                                            Toast.makeText(TwoPlayerActivity.this,"The Step Not Taken!!!",Toast.LENGTH_SHORT).show();
                                        }
                                        flag=flag*(1);
                                    }

                                });
                                break;
                            case "win":
                                if(p==player){
                                   updateStep(col,row,p,"YOU WIN!!!!");
                                }
                                else
                                    updateStep(col,row,p,"YOU LOST!!!!");
                                disconnect();
                                break;
                            case "draw":
                                updateStep(col,row,p,"DRAW OUT!!!!");
                                disconnect();
                                break;
                            case "continue":
                                /*if(p==player) flag=-1;
                                else flag=1;*/
                                flag=flag*(-1);
                                updateStep(col,row,p,"");
                                break;
                        }


                    }

                    if(!msgToSend.equals("")){
                        dataOutputStream.writeUTF(msgToSend);
                        dataOutputStream.flush();
                        msgToSend = "";

                    }
                    dataOutputStream.flush();
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
                final String eString = e.toString();
                TwoPlayerActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(TwoPlayerActivity.this, eString, Toast.LENGTH_LONG).show();
                    }

                });
            } catch (IOException e) {
                e.printStackTrace();
                final String eString = e.toString();
                TwoPlayerActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(TwoPlayerActivity.this, eString, Toast.LENGTH_LONG).show();
                    }

                });
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                TwoPlayerActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        imgView.invalidate();
                    }

                });
            }

        }
        private void updateStep(final int c, final int r, final int p_now, final String notifi){

            TwoPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    boardClient.onDrawBoard(r, c, imgView, p_now);
                    if(notifi.equals("")){

                    }else{
                        Toast.makeText(TwoPlayerActivity.this, notifi, Toast.LENGTH_LONG).show();
                    }
                    imgView.invalidate();

                }

            });
        }
        private void sendMsg(String msg){
            msgToSend = msg;
        }

        private void disconnect(){
            goOut = true;
        }
    }
}

