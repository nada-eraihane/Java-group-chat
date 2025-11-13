

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public abstract class ClientHandler implements Runnable{
	
	public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String clientUsername;
	
	public abstract ClientHandler(Socket socket); {
		try {
			this.socket = socket;
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.clientUsername = bufferedReader.readLine();
			clientHandlers.add(this);
			broadcastMessage("SERVER: " + clientUsername + "has entered the chat");
			
		} catch(IOExceptions e) {
			closeEverything(socket, bufferedReader, bufferedWriter);
			
		}
		
	}
	protected abstract void closeEverything(Socket socket2, BufferedReader bufferedReader2,
			BufferedWriter bufferedWriter2);

}
