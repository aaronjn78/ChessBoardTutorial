package com.aaron.chess;

import java.util.List;
import java.util.ArrayList;

public class ChessGame{

    private ChessBoard board;
    private boolean whiteTurn = true; //sets standard start of white having first move
    
    public ChessGame() {
        this.board = new ChessBoard();
    }

    public boolean makeMove(Position start, Position end) {
        Piece movingPiece = board.getPiece(start.getRow(), start.getColumn());

        if (movingPiece == null || movingPiece.getColor() != (whiteTurn ? PieceColor.WHITE : PieceColor.BLACK)) {
            return false; // no piece at start position or not the player's turn
        }

        if (movingPiece.isValidMove(end, board.getBoard())) {
            // execute move
            board.movePiece(start, end);
            whiteTurn = !whiteTurn; // switch turns
            return true;
        }

        return false;
    }

    public boolean isInCheck(PieceColor kingColor) {
        Position kingPosition = findKingPosition(kingColor);

        for (int row = 0; row < board.getBoard().length; row++) {
            for (int col = 0; col < board.getBoard()[row].length; col++) {

                Piece piece = board.getPiece(row, col);

                if (piece != null && piece.getColor() != kingColor) {
                    if (piece.isValidMove(kingPosition, board.getBoard())) {
                        return true; // an opposing piece can capture king
                    }
                }
            }
        }
        return false;
    }

    private Position findKingPosition(PieceColor color) {
        for (int row = 0; row < board.getBoard().length; row++){
            for (int col = 0; col < board.getBoard()[row].length; col++){
                Piece piece = board.getPiece(row, col);

                if (piece instanceof King && piece.getColor() == color) {
                    return new Position(row, col);
                }
            }
        }
        throw new RuntimeException("King not found, which should never happen");
    }

    public boolean isCheckMate(PieceColor kingColor) {
        if (!isInCheck(kingColor)){
            return false; //king not even in check
        }

        Position kingPosition = findKingPosition(kingColor);
        King king = (King) board.getPiece(kingPosition.getRow(), kingPosition.getColumn());

        //attempt to find move to get king out of check
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++){
            for (int colOffset = -1; colOffset <= 1; colOffset++){

                if (rowOffset == 0 && colOffset == 0) {
                    continue; //skip position of the king
                }

                Position newPosition = new Position(kingPosition.getRow() + rowOffset, kingPosition.getColumn() + colOffset);

                //check if moving king to new position is a valid move and does not result in check

                if (isPositionOnBoard(newPosition) && king.isValidMove(newPosition, board.getBoard()) && !wouldBeInCheckAfterMove(kingColor, kingPosition, newPosition)) {
                    return false; //found move that gets king out of check so not in checkmate
                }           
            }          
        }
        return true; //no legal moves to escape check
    }

    private boolean isPositionOnBoard(Position position){
        //check if destinations are valid squares on board
        return position.getRow() >= 0 && position.getRow() < board.getBoard().length && position.getColumn() >= 0 && position.getColumn() < board.getBoard()[0].length;
    }

    //check if a move would result in the player's own king to be in check
    private boolean wouldBeInCheckAfterMove(PieceColor kingColor, Position from, Position to) {

        //simulate move temporarily
        Piece temp = board.getPiece(to.getRow(), to.getColumn()); //temporarily stores piece at desination

        board.setPiece(to.getRow(), to.getColumn(), board.getPiece(from.getRow(), from.getColumn())); //puts piece in new position
        board.setPiece(from.getRow(),from.getColumn(), null); //clears space where piece used to be

        boolean inCheck = isInCheck(kingColor); //checks if move places king in check

        //undo the move
        board.setPiece(from.getRow(),from.getColumn(), board.getPiece(to.getRow(), to.getColumn())); //puts "moved" piece back to original location
        board.setPiece(to.getRow(),to.getColumn(), temp); //puts temporary piece back

        return inCheck;
    }

    //returns current board state
    public ChessBoard getBoard() {
        return this.board;
    }

    //resets game
    public void resetGame() {
        this.board = new ChessBoard();
        this.whiteTurn = true;
    }

    //returns piece color of curent player
    public PieceColor getCurrentPlayerColor(){
        return whiteTurn ? PieceColor.WHITE:PieceColor.BLACK;
    }

    private Position selectedPosition; //track currently selected piece's position

    public boolean isPieceSelected() {
        return selectedPosition != null;
    }

    public void clearSelectedPosition(){
        selectedPosition = null;
    }

    public boolean handleSquareSelection(int row, int col){
        if (selectedPosition == null){
            //attempt to select a piece
            Piece selectedPiece = board.getPiece(row, col);
            //removed this code to always set a selectedPosition when any piece selected, not just current players turn
            //if (selectedPiece != null && selectedPiece.getColor() == (whiteTurn ? PieceColor.WHITE: PieceColor.BLACK))
            if (selectedPiece != null){
                selectedPosition = new Position(row, col);
                return false; //piece selected but not moved
            }
        } else {
            boolean moveMade = makeMove(selectedPosition, new Position(row, col));
            selectedPosition = null; //reset selection regardless of move success
            return moveMade;
        }
        return false; //return false if no piece was selected or move not made     
    }

    public List<Position> getLegalMovesForPieceAt(Position position){
        Piece selectedPiece = board.getPiece(position.getRow(), position.getColumn());
        if (selectedPiece == null)
            return new ArrayList<>();
        
        List<Position> legalMoves = new ArrayList<>();
        switch (selectedPiece.getClass().getSimpleName()) {
            case "Pawn":
                addPawnMoves(position, selectedPiece.getColor(), legalMoves);
                break;
            case "Rook":
                addLineMoves(position, new int[][]{{1,0},{-1,0},{0,-1}}, legalMoves);
                break;
            case "Knight":
                addSingleMoves(position, new int[][]{{2,1},{2,-1},{-2,1},{-2,-1},{1,2},{-1,2}, {-1,2},{1,-2},{-1,-2}}, legalMoves);
                break;
            case "Bishop":
                addLineMoves(position, new int[][]{{1,1},{-1,-1},{1,-1},{-1,1}},legalMoves);
                break;
            case "Queen":
                addLineMoves(position, new int[][]{{1,1},{1,0},{1,-1},{0,1},{0,-1},{-1,1},{-1,0},{-1,-1}}, legalMoves);
                break;
            case "King":
                addSingleMoves(position, new int[][]{{1,1},{1,0},{1,-1},{0,1},{0,-1},{-1,1},{-1,0},{-1,-1}}, legalMoves);
                break;
        }
        return legalMoves;
    }

    private void addLineMoves(Position position, int[][] directions, List<Position> legalMoves) {
        for (int[] d : directions){
            Position newPos = new Position(position.getRow() + d[0], position.getColumn() + d[1]);
            while(isPositionOnBoard(newPos)){
                if(board.getPiece(newPos.getRow(),newPos.getColumn())==null){
                    legalMoves.add(new Position(newPos.getRow(), newPos.getColumn()));
                    newPos = new Position(newPos.getRow() + d[0], newPos.getColumn() + d[1]);
                }
                else {
                    if (board.getPiece(newPos.getRow(), newPos.getColumn()).getColor() != board.getPiece(position.getRow(), position.getColumn()).getColor()){
                        legalMoves.add(newPos);
                    }
                    break;
                }
            }
        }
    }

    private void addSingleMoves(Position position, int[][] moves, List<Position> legalMoves) {
        for (int[] move : moves) {
            Position newPos = new Position(position.getRow() + move[0], position.getColumn() + move[1]);

            if (isPositionOnBoard(newPos) && (board.getPiece(newPos.getRow(), newPos.getColumn()) == null) || isPositionOnBoard(newPos) && board.getPiece(newPos.getRow(), newPos.getColumn()).getColor() != board.getPiece(position.getRow(), position.getColumn()).getColor()){
                legalMoves.add(newPos);

            }
        }
    }

    private void addPawnMoves(Position position, PieceColor color, List<Position> legalMoves){
        int direction = color == PieceColor.WHITE? -1 : 1;

        //standard single move
        Position newPos = new Position(position.getRow() + direction, position.getColumn());

        if (isPositionOnBoard(newPos) && board.getPiece(newPos.getRow(), newPos.getColumn()) == null){
            legalMoves.add(newPos);
        }

        //double move from starting position
        if((color == PieceColor.WHITE && position.getRow() == 6) || (color == PieceColor.BLACK && position.getRow() == 1)){
            newPos = new Position(position.getRow() + 2 * direction, position.getColumn());
            Position intermediatePos = new Position(position.getRow() + direction, position.getColumn());

            if (isPositionOnBoard(newPos) && board.getPiece(newPos.getRow(), newPos.getColumn()) == null && board.getPiece(intermediatePos.getRow(), intermediatePos.getColumn()) == null){
                legalMoves.add(newPos);
            }
        }

        //capture
        int[] captureCols = {position.getColumn() -1, position.getColumn() +1};
        for(int col: captureCols) {
            newPos = new Position(position.getRow() + direction, col);

            if (isPositionOnBoard(newPos) && board.getPiece(newPos.getRow(), newPos.getColumn()) != null && board.getPiece(newPos.getRow(), newPos.getColumn()).getColor() != color) {
                legalMoves.add(newPos);
            }
        }

    }
}
