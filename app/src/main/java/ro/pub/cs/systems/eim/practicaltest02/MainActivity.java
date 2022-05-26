package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText port;
    EditText client_port;
    EditText ip;
    EditText key;
    EditText value;
    Button get;
    Button put;
    Button start_server;
    TextView result;
    ServerThread serverThread;
    ClientThread clientThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        port = findViewById(R.id.server_port);
        start_server = findViewById(R.id.start_server);
        client_port = findViewById(R.id.client_port);
        ip = findViewById(R.id.client_addr);
        key = findViewById(R.id.key);
        value = findViewById(R.id.value);
        get = findViewById(R.id.get);
        put = findViewById(R.id.put);
        result = findViewById(R.id.result);

        start_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serverThread = new ServerThread(Integer.parseInt(port.getText().toString()));
                serverThread.start();
            }
        });

        put.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = "put," + key.getText().toString()+","+value.getText().toString()+"\n";
                clientThread = new ClientThread(Integer.parseInt(client_port.getText().toString()), ip.getText().toString(), query, result);
                clientThread.start();
            }
        });

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String query = "get," + key.getText().toString()+"\n";
                    clientThread = new ClientThread(Integer.parseInt(client_port.getText().toString()), ip.getText().toString(), query, result);
                    clientThread.start();
            }
        });


    }

}