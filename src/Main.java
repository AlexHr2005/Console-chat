import org.w3c.dom.ls.LSResourceResolver;
import server.MyServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        MyServer server = new MyServer();
        server.run();
    }
}