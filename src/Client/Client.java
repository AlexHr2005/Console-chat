package Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread {
    private Socket client;
    private Scanner in;
    private PrintWriter out;
    private boolean done = false;

    @Override
    public void run() {
        try {
            client = new Socket("192.168.1.169", 9992);

            in = new Scanner(client.getInputStream(), "UTF-8");
            out = new PrintWriter(client.getOutputStream(), true);

            InputHandler inputHandler = new InputHandler();
            Thread t = new InputHandler();
            t.start();

            String inMessage;
            while((inMessage = in.nextLine()) != null) {
                System.out.println(inMessage);
            }
        } catch (IOException e) {
            shutdown();
        }
    }

    public void shutdown() {
        try {
            done = true;
            in.close();
            out.close();
            if (!client.isClosed()) {
                client.close();
            }
        } catch(IOException e){
            throw new RuntimeException();
        }
    }

    class InputHandler extends Thread {
        @Override
        public void run() {
            try {
                BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
                while (!done) {
                    String message = inReader.readLine();
                    out.println(message);
                }
            } catch (IOException e) {
                shutdown();
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }
}
