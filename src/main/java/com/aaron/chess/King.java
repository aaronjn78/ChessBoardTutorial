package com.aaron.chess;

public class King extends Piece {
    
    public King(PieceColor color, Position position) {
        super(color, position);
    }

    @Override
    public boolean isValidMove(Position newPosition, Piece[][] board) {

        if (newPosition.equals(this.position)) {
            return false; //can't move to same position
        }    
    
        int rowDiff = Math.abs(position.getRow() - newPosition.getRow());
        int colDiff = Math.abs(position.getColumn() - newPosition.getColumn());

        if (rowDiff > 1 || colDiff > 1) {
            //King is trying to move more than 1 space
            return false;
        }
        /*
        this is the code the tutorial uses to determine if the move is more than one square. I think my code works?

          boolean isOneSquareMove = rowDiff <= 1 && colDiff <= 1 && !(rowDiff == 0 && colDiff == 0);

         */

        Piece targetPiece = board[newPosition.getRow()][newPosition.getColumn()];

        if(targetPiece == null){
            return true;
        } else {
            return targetPiece.getColor() != this.getColor();
        }





    }
}
