/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serversocket;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Huong Thu Tran
 */
public class ServerSocket {
    //pott kết nối
   
    static final int SocketServerPORT = 8888;
    static final int TimeRandom = 10;

    String msgLog = "";

    private List<PlayerClient> userList;

    java.net.ServerSocket serverSocket;

    public static void main(String[] args) {
        
        ServerSocket myServerSocket = new ServerSocket();
        //DB db=new DB();
        //db.getData();
    }

    public ServerSocket() {
        System.out.print(Helper.getIpAddress());
        userList = new ArrayList<>();
        ServerThread chatServerThread = new ServerThread();
        chatServerThread.start();
    }
    
    public void addListPlayer(PlayerClient player) {
        this.userList.add(player);
    }

    private class ServerThread extends Thread {
        
        private ScheduledExecutorService excutor = Executors.newSingleThreadScheduledExecutor();
        private Runnable task;

        public ServerThread() {
            task = excuteRandom;
        }

        @Override
        public void run() {
            Socket socket = null;
            
            try {
                serverSocket = new java.net.ServerSocket(SocketServerPORT);
                System.out.println(" : "
                    + serverSocket.getLocalPort());

                while (true) {
                    socket = serverSocket.accept();
                    System.out.println(socket.getInetAddress());
                    PlayerClient client = new PlayerClient(socket);
                    userList.add(client);
                    excutor.schedule(task, TimeRandom, TimeUnit.SECONDS);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } 
        }
        
        Runnable excuteRandom = new Runnable() {
            @Override
            public void run() {
                System.out.println("Randoming....");
                if (userList.size() > 1) {
                    userList = Helper.randomPlayer(userList);
                     System.out.println("size = " + userList.size());
                    int number = userList.size() / 2;
                    
                    for (int i = 0; i < number; i++) {
                        System.out.println("i = " + i);
                        
                        PlayerClient playerA = userList.get(i);
                        playerA.setRolePlayer(RolePlayer.PLAYERA);
                        
                        PlayerClient playerB = userList.get(i + number);
                        playerB.setRolePlayer(RolePlayer.PLAYERB);
                        
                        System.out.println("connect "+ playerA.getSocket().getInetAddress()+" vs "+playerB.getSocket().getInetAddress() );

                        MatchThread matchThread = new MatchThread(playerA, playerB);
                        
                        matchThread.start();
                    }
                    
                    for (int i = 0; i < number; i++) {
                        userList.remove(i);
                        userList.remove(i+number);
                    }
                    
                } else {
                    System.out.println("not enought device each other");
                }
            }
        };


    }
   

}
