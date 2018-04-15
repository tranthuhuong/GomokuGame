package com.example.huongthutran.gomoku;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

/**
 * Created by Huong Thu Tran on 4/12/2018.
 */

public class Client extends AsyncTask<String, Void, String>   {
    @Override
    protected void onPostExecute(String result) {
        Log.d("Test1",result);
        super.onPostExecute(result);
    }
    @Override
    protected String doInBackground(String... strings) {
        String response="";
        Socket socket = null;
        String str=strings[3];
        try {
            socket = new Socket(strings[0], Integer.parseInt(strings[1]));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
                    1024);
            byte[] buffer = new byte[1024];

            int bytesRead;
            PrintStream ps=new PrintStream(socket.getOutputStream());
            ps.println("connect");
            InputStream inputStream = socket.getInputStream();
			/*
             * notice: inputStream.read() will block if no data return
			 */
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                response += byteArrayOutputStream.toString("UTF-8");
            }

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
            Log.d("Test2",response);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "IOException: " + e.toString();
            Log.d("Test2",response);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    Log.d("Test2",response);
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        Log.d("Test2",response);
        return response;
    }



}