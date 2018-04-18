/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serversocket;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Huong Thu Tran
 */
public class DB {
    private Connection con;
    private Statement st;
    private ResultSet rs;
    public  DB() {
        try{
            System.out.println("connect");
            Class.forName("com.mysql.jdbc.Driver");
            con=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/gomokudb","root","");
            st=(Statement) con.createStatement();
           // getData();
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Erro:"+ex);
            Logger.getLogger(ServerSocket.class.getName()).log(Level.SEVERE, null, ex);}
    }
    public void getData(){
        try{
            String query="select * from matchboard";
            rs=st.executeQuery(query);
            System.out.println("hihihi");
            while (rs.next()){
                int id=rs.getInt("id");
                String p0=rs.getString("player0");
                String p1=rs.getString("player1");
                String board=rs.getString("board");
                System.out.println(p0+" vs "+p1+"=>"+board);
            }
        }catch(Exception e){
            System.out.println("Erro:"+e);
        }
    }
    public void insertDB(String p0,String p1,String board) throws SQLException{
        String query = " insert into matchboard ( player0, player1, board)"
        + " values (?, ?, ?)";
         PreparedStatement preparedStmt = con.prepareStatement(query);
         preparedStmt.setString (1,p0);
         preparedStmt.setString (2,p1);
         preparedStmt.setString (3,board);
         preparedStmt.execute();
        System.err.println("OK");
      con.close();
    }
}
