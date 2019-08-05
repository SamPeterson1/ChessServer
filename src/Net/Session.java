package Net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Session implements Runnable {
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private Socket socket;
	private Thread thread;
	private Server server;
	private char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	
	public Session(Socket socket, ArrayList<String> playerIDs, Server s) {
		
		try {
			this.thread = new Thread(this, "Session Thread");
			this.socket = socket;
			this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
			this.server = s;
			s.addSession(this);
			this.send(String.valueOf(s.getSide()));
			this.thread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getSpot(int x, int y) {
		return this.alphabet[x] + String.valueOf(8-y);
	}
	
	@Override
	public void run() {
		try {
			String line;
			while ((line = this.bufferedReader.readLine()) != null) {
				Request request = Request.parseString(line);
				if(request.getMessage().equals("move")) {
					int x1 = request.getInt("x1");
					int y1 = request.getInt("y1");
					int x2 = request.getInt("x2");
					int y2 = request.getInt("y2");
					
					System.out.println(this.getSpot(x1, y1) + " --> " + this.getSpot(x2, y2));
					
					this.server.movePiece(x1, y1, x2, y2);
					this.server.forward(line, this);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				this.bufferedReader.close();
				this.bufferedWriter.close();
				this.server.disconnect(this);
				this.socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void send(String message) {
		try {
			this.bufferedWriter.write(message);
			this.bufferedWriter.newLine();
			this.bufferedWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}