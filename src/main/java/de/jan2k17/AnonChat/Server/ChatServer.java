package de.jan2k17.AnonChat.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer {
    public static int port = 3141;

    private List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    private ServerSocket serverSocket;

    public ChatServer(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            System.out.println("Started chat server on port " + port);
            Functions.writeToFile("Started chat server on port " + port);
            while (true) {
                System.out.println("Waiting for new client...");
                Functions.writeToFile("Waiting for new client...");
                Socket connectionToClient = this.serverSocket.accept();
                ClientHandler client = new ClientHandler(this, connectionToClient);
                this.clients.add(client);
                System.out.println("Accepted new client");
                Functions.writeToFile("Accepted new client");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (this.serverSocket != null) {
                try {
                    this.serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void removeClient(ClientHandler client) {
        this.clients.remove(client);
    }

    public void broadcastMessage(String message) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String DateTime = dtf.format(now);
        System.out.println("[" + DateTime + "] " + message);
        Functions.writeToFile("[" + DateTime + "] " + message);
        if (message != null) {
            for (ClientHandler client : this.clients) {
                client.sendMessage("[" + DateTime + "] " + message);
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            port = 3141;
        } else {
            int i = 3140 + Integer.parseInt(args[0]);
            port = i;
        }
    }
}
