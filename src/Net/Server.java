package Net;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import Game.ChessBoard;

public class Server implements Runnable {
	private ServerSocket serverSocket;
	private Thread thread;
	private ArrayList<String> playerIDs;
	private ArrayList<Session> sessions;
	private ChessBoard board;
	private int side = -1;

	public Server() {
		playerIDs = new ArrayList<String>();
		this.sessions = new ArrayList<Session>();
		this.board = new ChessBoard();
		try {
			this.thread = new Thread(this, "Server Thread");
			this.serverSocket = new ServerSocket(7777);
			this.thread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getSide() {
		this.side = -side;
		return this.side;
	}
	
	public void disconnect(Session session) {
		this.sessions.remove(session);
	}
	
	public void addSession(Session s) {
		this.sessions.add(s);
	}
	
	public void movePiece(int x1, int y1, int x2, int y2) {
		this.board.movePiece(x1, y1, x2, y2);
	}
	
	public void forward(String message, Session sender) {
		for(Session s: this.sessions) {
			if(!s.equals(sender)) {
				s.send(message);
			}
		}
	}
	
	@Override
	public void run() {		
			try {
				while (true) {
					new Session(this.serverSocket.accept(), playerIDs, this);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}