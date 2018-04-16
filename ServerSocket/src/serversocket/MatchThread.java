/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serversocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Callback;

/**
 *
 * @author Huong Thu Tran
 */
public class MatchThread extends Thread{
    private PlayerClient playerA;
    private PlayerClient playerB; 
    private DataInputStream dataInputStreama;
    private DataOutputStream dataOutputStreama;
    private ClientPlayerInBoard1 pA;
    private ClientPlayerInBoard1 pB;
    public String message,ms="";
    public void setMess(String m){
        this.message=m;
    }
    String msgLog="";
    GomokuBoard board=new GomokuBoard(8, 8);
    public MatchThread(PlayerClient playerA, PlayerClient playerB) {
        this.playerA = playerA;
        this.playerB = playerB;
        dataInputStreama = null;
        dataOutputStreama = null;
        message = "";
        pA=new ClientPlayerInBoard1(playerA);
        pB=new ClientPlayerInBoard1(playerB);
        
    }
    //Override method
    @Override
    public void run() {
        pA.start();
        pB.start();
        while (true) {
            if(!message.trim().equals("")){
                 broadcastMsg(message);
                 System.out.println(message);
                 message="";
            }
           
        }
        //broadcastMsg(message);
    }
    private void broadcastMsg(String msg) {
         pA.sendMsg(msg);
         pB.sendMsg(msg);
        
    }
    private String checkBoard(int col,int row,int player){
        String status="continue";
        
        //neus đi đc
        if(board.checkStep(col,row)){
            //ghi 1 bước đi
            board.setStep(col,row,player);
            //kiểm tra thắng
            
            if(board.checkWin(player)){
               status="win"; 
            } else {
               status="continue";
            }
        }
        //nếu đã có người đi
        
        else{
            status="non";
            return status;
        }
        return status;
    }
    public class ClientPlayerInBoard1 extends Thread implements Runnable{
    Callback c; 
    Socket socket;
    PlayerClient playerClient;
    String msgToSend = "";

    ClientPlayerInBoard1(PlayerClient client) {
        playerClient = client;
        this.socket = client.getSocket();
        client.setSocket(client.getSocket());
        client.setMatchThread(client.getMatchThread());

    }

    @Override
        public void run() {
            DataInputStream dataInputStream = null;
            DataOutputStream dataOutputStream = null;

            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                this.msgToSend="You're " + playerClient.getRolePlayer();
                dataOutputStream.writeUTF(msgToSend);
                dataOutputStream.flush();
                this.msgToSend="";
                while (true) {
                    if (dataInputStream.available() > 0) {
                        msgLog = playerClient.getRolePlayer()+" - "+dataInputStream.readUTF();
                        
                        System.out.print(msgLog);      
                        String str[]=msgLog.split(" - ");
                        int p=0;
                        if(str[0].equals("PLAYERA")) p=0; else p=1;
                        int col=Integer.parseInt(str[1]);
                        int row=Integer.parseInt(str[2]);
                        String check=checkBoard(col,row,p);
                        //System.out.println(message);
                        
                        ms=p+" - "+col+" - "+row+" - "+check;
                        msgLog="";
                        broadcastMsg(ms);
                       // broadcastMsg(playerClient.getRolePlayer() + ": " + newMsg);
                    }
                    
                    if (!msgToSend.equals("")) {
                        dataOutputStream.writeUTF(msgToSend);
                        dataOutputStream.flush();
                        msgToSend = "";
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
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

            }

        }
        public void sendMsg(String msg) {
            msgToSend = msg;
        }
        public String getMsLog(){
          
            return msgLog;
            
        }
   
}
}