package server;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ClientHandler extends Thread {
    private Socket connectionSocket;

    public ClientHandler(Socket connectionSocket) {
        this.connectionSocket = connectionSocket;
    }

    @Override
    public void run() {
        InputStream inputToServer = null;
        try {
            inputToServer = connectionSocket.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        OutputStream outputFromServer = null;
        try {
            outputFromServer = connectionSocket.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scanner scanner = new Scanner(inputToServer, "UTF-8");
        PrintWriter serverPrintOut = null;
        try {
            serverPrintOut = new PrintWriter(new OutputStreamWriter(outputFromServer, "UTF-8"), true);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        serverPrintOut.println("Welcome to the group chat!");
        serverPrintOut.println("Enter username: ");
        String username = scanner.nextLine();

        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d/MM/yyyy HH:mm");
            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = dtf.format(now);

            //System.out.println(username + ", " + formattedDateTime + "\n" + line);

            if(line.toLowerCase().equals("bye")) {
                try {
                    connectionSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
