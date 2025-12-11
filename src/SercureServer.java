package javaApp;

import javax.net.ssl.*;
import java.io.*;
import java.net.Socket;
import java.security.KeyStore;

public class SecureServer {
    private SSLServerSocket serverSocket;

    public SecureServer(SSLServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try {
            System.out.println("Secure Server is running and waiting for clients...");
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected securely!");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            closeServerSocket();
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            // Load server keystore
            System.out.println("Loading server keystore...");
            KeyStore keyStore = KeyStore.getInstance("JKS");
            FileInputStream keyStoreFile = new FileInputStream("server.keystore");
            keyStore.load(keyStoreFile, "password123".toCharArray());
            keyStoreFile.close();
            System.out.println("Keystore loaded successfully!");

            System.out.println("Initializing key manager...");
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keyStore, "password123".toCharArray());

            System.out.println("Creating SSL context...");
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, null);

            System.out.println("Creating secure server socket...");
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(1234);

            System.out.println("====================================");
            System.out.println("SSL/TLS ENABLED on port 1234");
            System.out.println("Server is ready for secure connections");
            System.out.println("====================================\n");
            
            SecureServer server = new SecureServer(serverSocket);
            server.startServer();

        } catch (FileNotFoundException e) {
            System.err.println("ERROR: server.keystore file not found!");
            System.err.println("Please run the certificate generation script first.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error starting secure server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
