package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MyServer {
    private String lastMessage;

    public static void run() throws IOException {
        ServerSocket serverSocket = new ServerSocket(9991);

        while(true) {
            Socket connectionSocket = serverSocket.accept();

            Thread client = new ClientHandler(connectionSocket);
            client.start();
        }
    }
}
