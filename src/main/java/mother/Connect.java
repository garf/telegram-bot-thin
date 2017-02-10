package mother;

import dto.socket.MotherMessage;
import dto.socket.ClientMessage;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connect {
    private String host;
    private int port;
    private Socket socket;
    private int connectTries = 0;

    public Connect(String host, int port) {
        this.host = host;
        this.port = port;
        this.connect();
    }

    public MotherMessage send(ClientMessage clientMessage) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(this.socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(this.socket.getInputStream());

            out.writeObject(clientMessage);

            return (MotherMessage) in.readObject();
        } catch  (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
            System.out.println("Retry connection");

            if (this.connectTries < 5) {
                this.connectTries++;

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

                return this.connect().send(clientMessage);
            } else {
                System.out.println("Mother is not up.");
                System.exit(-1);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Connect connect() {
        try {
            this.socket = new Socket(this.host, this.port);
            this.connectTries = 0;
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + this.host);
        } catch  (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        }
        System.out.println(String.format("Connected to server %s:%d", this.host, this.port));

        return this;
    }
}
