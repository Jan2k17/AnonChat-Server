package de.jan2k17.AnonChat.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private ChatServer chatServer;

    private Socket connectionToClient;

    private BufferedReader fromClientReader;

    private PrintWriter toClientWriter;

    public ClientHandler(ChatServer chatServer, Socket connectionToClient) {
        this.chatServer = chatServer;
        this.connectionToClient = connectionToClient;
        (new Thread(this)).start();
    }

    public void run() {
        try {
            this.fromClientReader = new BufferedReader(new InputStreamReader(this.connectionToClient.getInputStream()));
            this.toClientWriter = new PrintWriter(new OutputStreamWriter(this.connectionToClient.getOutputStream()));
            this.chatServer.broadcastMessage("New client connected.");
            Functions.writeToFile("New client connected.");
            String message = this.fromClientReader.readLine();
            while (message != null) {
                String[] msg = message.split(" -:- ");
                this.chatServer.broadcastMessage(String.valueOf(msg[0]) + ": " + msg[1]);
                message = this.fromClientReader.readLine();
            }
        } catch (IOException iOException) {

        } finally {
            this.chatServer.removeClient(this);
            this.chatServer.broadcastMessage("Client disconnected.");
            Functions.writeToFile("Client disconnected.");
            if (this.fromClientReader != null)
                try {
                    this.fromClientReader.close();
                } catch (IOException iOException) {}
            if (this.toClientWriter != null)
                this.toClientWriter.close();
        }
    }

    public void sendMessage(String message) {
        this.toClientWriter.println(message);
        this.toClientWriter.flush();
    }
}
