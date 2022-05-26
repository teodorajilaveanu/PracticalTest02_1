package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerThread extends Thread{

    public int port;
    public ServerSocket serverSocket;
    public HashMap<String, String> data;
    public HashMap<String, String> times;
    public ServerThread(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            Log.e("Server error", "ServerSocker error for port " + this.port);
            e.printStackTrace();
        }
        this.data = new HashMap<>();
        this.times = new HashMap<>();

    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Socket clientSocket = serverSocket.accept();
                String respose = "";
                if (clientSocket != null) {
                    BufferedReader bufferedReader = Utils.getReader(clientSocket);
                    String query = bufferedReader.readLine();
                    String[] q = query.split(",");
                    System.out.println(q[0] + q[1]);
                    if (q[0].equals("get")) {
                        System.out.println(this.data.containsKey(q[1]));
                        if (this.data.containsKey(q[1])) {
                            HttpClient httpClient = new DefaultHttpClient();
                            HttpGet httpGet = new HttpGet("https://worldtimeapi.org/api/ip");
                            HttpResponse httpResponse = httpClient.execute(httpGet);
                            HttpEntity httpEntity = httpResponse.getEntity();
                            if (httpEntity == null) {
                                Log.e("Server error", "Null response from api");
                            }
                            String res = EntityUtils.toString(httpEntity);
                            JSONObject content = new JSONObject(res);
                            Integer time = content.getInt("unixtime");
                            Log.d("Time:", q[1] + " " + this.times.get(q[1]) + this.data.get(q[1]));
                            if (time - Integer.parseInt(this.times.get(q[1])) > 60) {
                                this.data.remove(q[1]);
                                this.times.remove(q[1]);
                                respose = "none";
                            } else {
                                respose = this.data.get(q[1]) + "\n";
                            }
                        } else {
                            respose = "none\n";
                        }
                    }
                    System.out.println(q[0].equals("put"));
                    if (q[0].equals("put")) {
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpGet httpGet = new HttpGet("https://worldtimeapi.org/api/ip");
                        HttpResponse httpResponse = httpClient.execute(httpGet);
                        HttpEntity httpEntity = httpResponse.getEntity();
                        if (httpEntity == null) {
                            Log.e("Server error", "Null response from api");
                        }
                        String res = EntityUtils.toString(httpEntity);
                        JSONObject content = new JSONObject(res);
                        Integer time = content.getInt("unixtime");
                        this.times.put(q[1], time.toString());
                        this.data.put(q[1], q[2]);

                    }
                    PrintWriter printWriter = Utils.getWriter(clientSocket);
                    printWriter.println(respose);

                    clientSocket.close();
                } else {
                    Log.e("Server error", "Client socket is null");
                }
            } catch (IOException e) {
                Log.e("Server error", "Accept method failed");
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e("Server error", "Json exception");
                e.printStackTrace();
            }

        }
    }
}
