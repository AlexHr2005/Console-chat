package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyServer {
    private ServerSocket server;
    private ArrayList<ClientHandler> connections;
    private ExecutorService pool;
    private boolean done;

    public MyServer() {
        connections = new ArrayList<>();
        done = false;
    }

    public void run() throws IOException {
        server = new ServerSocket(9992);
        pool = Executors.newCachedThreadPool();

        while(!done) {
            Socket connectionSocket = server.accept();
            ClientHandler client = new ClientHandler(connectionSocket);
            connections.add(client);
            pool.execute(client);
        }
    }

    public void broadcast(String message) {
        for(ClientHandler connection : connections) {
            connection.sendMessage("\n" + message + "\n");
        }
    }

    public void shutdown() throws IOException {
        for(ClientHandler client : connections) {
            client.shutdown();
        }

        if(!server.isClosed()) {
            server.close();
        }
        pool.shutdown();
    }

    class ClientHandler extends Thread {
        private Socket client;
        private Scanner in;
        private PrintWriter out;
        private String username;

        public ClientHandler(Socket connectionSocket) {
            this.client = connectionSocket;
        }

        @Override
        public void run() {
            try {
                in = new Scanner(client.getInputStream(), "UTF-8");
                out = new PrintWriter(client.getOutputStream(), true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            out.println("Welcome to the group chat!");
            out.println("Enter username: ");
            username = in.nextLine();
            broadcast(username + " joined the chat!");

            while(in.hasNextLine()) {
                String line = in.nextLine();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d/MM/yyyy HH:mm");
                LocalDateTime now = LocalDateTime.now();
                String formattedDateTime = dtf.format(now);

                String message = username + ", " + formattedDateTime + "\n" + line;
                broadcast(message);

                if(line.toLowerCase().equals("bye")) {
                    try {
                        shutdown();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        public void shutdown() throws IOException {
            broadcast(username + " left the chat!");
            in.close();
            out.close();
            if(!client.isClosed()) {
                client.close();
            }
        }
    }
}