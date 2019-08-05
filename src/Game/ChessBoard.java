package Game;

import Net.Request;

public class ChessBoard {
	
	int[] board;
	
	public ChessBoard() {
		this.board = new int[64];
	}
	
	public void movePiece(int x1, int y1, int x2, int y2) {
		int id = this.board[y1*8+x1];
		this.board[y1*8+x1] = 0;
		this.board[y2*8+x2] = id;
	}
	
	public String toString() {
		Request r = new Request("board");
		r.addParameter("peices", this.board);

		return r.toString();
	}
}
