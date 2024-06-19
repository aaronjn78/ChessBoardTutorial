package com.aaron.chess;

public class ChessBoard {
    
    private Piece[][] board;

    //create new chessboard and setup pieces
    public ChessBoard() {
        this.board = new Piece[8][8];
        setupPieces();
    }

    public void setupPieces() {    
        //place Rooks
        board[0][0] = new Rook(PieceColor.BLACK, new Position(0, 0));
        board[0][7] = new Rook(PieceColor.BLACK, new Position(0, 7));
        board[7][0] = new Rook(PieceColor.WHITE, new Position(7, 0));
        board[7][7] = new Rook(PieceColor.WHITE, new Position(7, 7));

        // place Knights
        board[0][1] = new Knight(PieceColor.BLACK, new Position(0, 1));
        board[0][6] = new Knight(PieceColor.BLACK, new Position(0, 6));
        board[7][6] = new Knight(PieceColor.WHITE, new Position(7, 6));
        board[7][1] = new Knight(PieceColor.WHITE, new Position(7, 1));

        //place Bishops
        board[0][2] = new Bishop(PieceColor.BLACK, new Position(0, 2));
        board[0][5] = new Bishop(PieceColor.BLACK, new Position(0, 5));
        board[7][5] = new Bishop(PieceColor.WHITE, new Position(7, 5));
        board[7][2] = new Bishop(PieceColor.WHITE, new Position(7, 2));

        //place Queens
        board[0][3] = new Queen(PieceColor.BLACK, new Position(0, 3));
        board[7][3] = new Queen(PieceColor.WHITE, new Position(7, 3));

        //place Kings
        board[0][4] = new King(PieceColor.BLACK, new Position(0, 4));
        board[7][4] = new King(PieceColor.WHITE, new Position(7, 4));

        //place Pawns
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(PieceColor.BLACK, new Position(1, i));
            board[6][i] = new Pawn(PieceColor.WHITE, new Position(6, i));
        }

    }

    public void movePiece(Position start, Position end) {
        //check if there is a piece at the start position and if the move is valid for the piece selected
        if (board[start.getRow()][start.getColumn()] != null && board[start.getRow()][start.getColumn()].isValidMove(end, board)) {

            //Perform the move; place the piece at the end position
            board[end.getRow()][end.getColumn()] = board[start.getRow()][start.getColumn()];

            //update the piece's position
            board[end.getRow()][end.getColumn()].setPosition(end);

            //clear the start position
            board[start.getRow()][start.getColumn()] = null;

        }
    }

    public Piece[][] getBoard() {
        return board;
    }

    //method to return a Piece on the board in a specified location. Returns null if no Piece in location
    public Piece getPiece(int row, int column){
        return board[row][column];
    }

    //method to put a Piece on the board at specified location
    //only used in ChessGame in wouldBeInCheckAfterMove method
    //can this be moved or eliminated?
    public void setPiece(int row, int column, Piece piece) {
        board[row][column] = piece;
        if (piece != null) {
            piece.setPosition(new Position(row, column));
        }
    }
}