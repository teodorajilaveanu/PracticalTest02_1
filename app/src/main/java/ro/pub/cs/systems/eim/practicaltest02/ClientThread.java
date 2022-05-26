package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread{
    int port;
    String addr;
    String query;
    TextView res;
    public ClientThread(int port, String addr, String query, TextView res){
        this.port = port;
        this.addr = addr;
        this.query = query;
        this.res = res;
    }


    @Override
    public void run() {
        try {
            Socket socket = new Socket(addr, port);

            BufferedReader bufferedReader = Utils.getReader(socket);
            PrintWriter printWriter = Utils.getWriter(socket);

            printWriter.println(query);

            String response = bufferedReader.readLine();
            Log.d("Client ", "Recieved data: " + response);

            res.post(new Runnable() {
                @Override
                public void run() {
                    res.setText(response);
                }
            });
        } catch (IOException e) {
            Log.e("Client error", "Socket error on client for ip " + addr + " and port " + port);
            e.printStackTrace();
        }
    }
}