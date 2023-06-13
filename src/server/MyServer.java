package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MyServer {
    private static List<Socket> connections = new ArrayList<>();

    private static void connectToServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(9991);
        Socket connectionSocket = serverSocket.accept();
        connections.add(connectionSocket);

        InputStream inputToServer = connectionSocket.getInputStream();
        OutputStream outputFromServer = connectionSocket.getOutputStream();

        Scanner scanner = new Scanner(inputToServer, "UTF-8");
        PrintWriter serverPrintOut = new PrintWriter(new OutputStreamWriter(outputFromServer, "UTF-8"), true);

        serverPrintOut.println("Welcome to the group chat!");
    }

    public static void run() throws IOException {
        connectToServer();
        boolean done = false;

        InputStream inputToServer = connections.get(0).getInputStream();
        OutputStream outputFromServer = connections.get(0).getOutputStream();

        Scanner scanner = new Scanner(inputToServer, "UTF-8");
        PrintWriter serverPrintOut = new PrintWriter(new OutputStreamWriter(outputFromServer, "UTF-8"), true);

        while(!done && scanner.hasNextLine()) {
            System.out.println(scanner.hasNextLine());
            String line = scanner.nextLine();
            serverPrintOut.println("Echo from Server: " + line);

            if(line.toLowerCase().trim().equals("bye")) {
                done = true;
            }
        }
    }
}
