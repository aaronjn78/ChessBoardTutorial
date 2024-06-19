package com.aaron.chess;

public class Bishop extends Piece{

    public Bishop(PieceColor color, Position position){
        super(color, position);
    }
    
    @Override
    public boolean isValidMove(Position newPosition, Piece[][] board) {
        
        if (newPosition.equals(this.position)) {
            return false; //can't move to same position
        }

        int colDiff = Math.abs(position.getColumn() - newPosition.getColumn());
        int rowDiff = Math.abs(position.getRow() - newPosition.getRow());

        //bishop can only move along diagonal
        if (rowDiff != colDiff) {
            return false;
        }

        //check for obstructions along path
        int rowDirection = newPosition.getRow() > position.getRow() ? 1: -1;
        int colDirection = newPosition.getColumn() > position.getColumn() ? 1: -1;
        int steps = rowDiff - 1; //number of squares to check for obstructions

        for (int i = 1; i <= steps; i++){
            if (board[position.getRow() + i * rowDirection][position.getColumn() + i * colDirection] != null){
                return false; //piece in the way
            }
        }

        //check if destination square is empty. 
        //if not, check if the piece occupying the space is opposing color
        Piece targetPiece = board[newPosition.getRow()][newPosition.getColumn()];

        if (targetPiece == null) {
            return true;
        } else {
            return targetPiece.getColor() != this.getColor(); //return true of opposing piece, false if same color
        }


    }
}
