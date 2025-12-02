import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String username) {
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
<<<<<<< HEAD
<<<<<<< HEAD
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
=======
        }
        closeEverything(socket, bufferedReader, bufferedWriter);
>>>>>>> dc9b1ee (Implement basic client functionality with messaging)
=======
        }
        closeEverything(socket, bufferedReader, bufferedWriter);
=======
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
>>>>>>> e268635 (fixed code)
>>>>>>> 26ed3cf (fixed files)
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
<<<<<<< HEAD
<<<<<<< HEAD
=======

>>>>>>> dc9b1ee (Implement basic client functionality with messaging)
=======

=======
>>>>>>> e268635 (fixed code)
>>>>>>> 26ed3cf (fixed files)
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

<<<<<<< HEAD
<<<<<<< HEAD
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
=======
    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
>>>>>>> dc9b1ee (Implement basic client functionality with messaging)
=======
    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
=======
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
>>>>>>> e268635 (fixed code)
>>>>>>> 26ed3cf (fixed files)
        try {
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
<<<<<<< HEAD
<<<<<<< HEAD

    public static void main(String[] args) throws IOException {
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
        
        Socket socket = new Socket(serverIP, port);
        Client client = new Client(socket, username);
        client.listenForMessage();
        client.sendMessage();
    }
}
=======
}
>>>>>>> dc9b1ee (Implement basic client functionality with messaging)
=======
}
=======

    public static void main(String[] args) throws IOException {
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
        
        Socket socket = new Socket(serverIP, port);
        Client client = new Client(socket, username);
        client.listenForMessage();
        client.sendMessage();
    }
}
>>>>>>> e268635 (fixed code)
>>>>>>> 26ed3cf (fixed files)
