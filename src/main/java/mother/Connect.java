package mother;

import dto.socket.MotherMessage;
import dto.socket.ClientMessage;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connect {
    private String host;
    private int port;
    private Socket socket = null;
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
            System.out.println("I/O error sending: " + e.getMessage());
            System.out.println("Retry connection " + (this.connectTries + 1));

            if (this.connectTries < 5) {
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
            this.connectTries++;
            this.socket = new Socket(this.host, this.port);
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + this.host);
            return this;
        } catch (IOException e) {
            System.out.println("I/O error connecting: " + e.getMessage());
            return this;
        }

        this.connectTries = 0;
        System.out.println(String.format("Connected to server %s:%d", this.host, this.port));

        return this;
    }
}
