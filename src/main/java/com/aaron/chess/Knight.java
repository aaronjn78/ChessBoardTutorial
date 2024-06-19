package com.aaron.chess;

public class Knight extends Piece {

    public Knight(PieceColor color, Position position) {
        super(color, position);
    }

    @Override
    public boolean isValidMove(Position newPosition, Piece[][] board){

        if (newPosition.equals(this.position)) {
            return false; //can't move to same position
        }

        int rowDiff = Math.abs(this.position.getRow() - newPosition.getRow());
        int colDiff = Math.abs(this.position.getColumn() - newPosition.getColumn());
        boolean isValidMove = (rowDiff == 2 && colDiff == 1 || rowDiff == 1 && colDiff == 2);

        if (!isValidMove) {
            return false;
        }

        Piece targetPiece = board[newPosition.getRow()][newPosition.getColumn()];

        if (targetPiece == null) {
            return true; //destination empty
        } else {
            return targetPiece.getColor() != this.getColor();
        }

    }
}
