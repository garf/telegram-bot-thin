package mother;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connect {
    private String host;
    private int port;
    private Socket socket;

    public Connect(String host, int port) {
        this.host = host;
        this.port = port;
        this.connect();
    }

    public String send(String content) {
        try {
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            out.println(content);
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            String text = in.readLine();
            System.out.println(text);
            return text;
        } catch  (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
            return this.connect().send(content);
        }
    }

    public Connect connect() {
        try {
            this.socket = new Socket(this.host, this.port);
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + this.host);
        } catch  (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        }
        System.out.println(String.format("Connected to server %s:%d", this.host, this.port));

        return this;
    }
}
