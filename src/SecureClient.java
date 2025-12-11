package javaApp;

import javax.net.ssl.*;
import java.io.*;
import java.net.Socket;
import java.security.KeyStore;
import java.util.Scanner;

public class SecureClient {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public SecureClient(Socket socket, String username) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage() {
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(username + ": " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
                while (socket.isConnected()) {
                    try {
                        msgFromGroupChat = bufferedReader.readLine();
                        System.out.println(msgFromGroupChat);
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter your username for the group chat: ");
            String username = scanner.nextLine();

            System.out.print("Enter server IP address (or press Enter for localhost): ");
            String serverIP = scanner.nextLine();
            if (serverIP.trim().isEmpty()) {
                serverIP = "localhost";
            }

            System.out.print("Enter server port (or press Enter for default 1234): ");
            String portInput = scanner.nextLine();
            int port = portInput.trim().isEmpty() ? 1234 : Integer.parseInt(portInput);

            System.out.println("\nLoading client truststore...");
            KeyStore trustStore = KeyStore.getInstance("JKS");
            FileInputStream trustStoreFile = new FileInputStream("client.truststore");
            trustStore.load(trustStoreFile, "password123".toCharArray());
            trustStoreFile.close();
            System.out.println("Truststore loaded successfully!");

            System.out.println("Initializing trust manager...");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(trustStore);

            System.out.println("Creating SSL context...");
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);


            System.out.println("Connecting to secure server...");
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket(serverIP, port);

            System.out.println("====================================");
            System.out.println("CONNECTED to secure server with SSL/TLS encryption!");
            System.out.println("All messages are now encrypted");
            System.out.println("====================================\n");

            SecureClient client = new SecureClient(socket, username);
            client.listenForMessage();
            client.sendMessage();

        } catch (FileNotFoundException e) {
            System.err.println("ERROR: client.truststore file not found!");
            System.err.println("Please run the certificate generation script first.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error connecting to secure server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
