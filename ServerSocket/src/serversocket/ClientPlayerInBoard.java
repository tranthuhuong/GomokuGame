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
import javafx.util.Callback;

/**
 *
 * @author Huong Thu Tran
 */
public class ClientPlayerInBoard extends Thread {
    Socket socket;
    PlayerClient playerClient;
    String msgToSend = "";
    String msgLog="";
    Callback c;
    ClientPlayerInBoard(PlayerClient client) {
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

              //  String n = dataInputStream.readUTF();
                sendMsg("You're " + playerClient.getRolePlayer() + "\n");
                //dataOutputStream.writeUTF("You're player " + playerClient.getRolePlayer() + "\n");
                //dataOutputStream.flush();

            //    broadcastMsg(n + " join our chat.\n");

                while (true) {
                    if (dataInputStream.available() > 0) {
                        this.msgLog = dataInputStream.readUTF();
                        //msgLog = playerClient.getRolePlayer() + " - " + newMsg+"\n";
                        
                        System.out.print(msgLog);
                        
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
