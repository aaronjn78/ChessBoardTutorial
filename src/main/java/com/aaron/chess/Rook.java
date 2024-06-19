package com.aaron.chess;

public class Rook extends Piece {

    public Rook(PieceColor color, Position position) {
        super(color,position);
    }

    @Override
    public boolean isValidMove(Position newPosition, Piece[][] board) {
        //rooks can move up or down any number of squares, can't jump

        if (newPosition.equals(this.position)) {
            return false; //can't move to same position
        }
        if (position.getRow() == newPosition.getRow()) {
            //rook is moving across the row
            int columnStart = Math.min(position.getColumn(), newPosition.getColumn()) + 1;
            int columnEnd = Math.max(position.getColumn(), newPosition.getColumn());
            for (int column = columnStart; column < columnEnd; column++){
                if (board[position.getRow()][column] != null){
                    return false; //piece in the way
                }
            }
        } else if (position.getColumn() == newPosition.getColumn()) {
            //rook is moving up/down a column
            int rowStart = Math.min(position.getRow(), newPosition.getRow()) + 1;
            int rowEnd = Math.max(position.getRow(), newPosition.getRow());
            for (int row = rowStart; row < rowEnd; row++) {
                if (board[row][position.getColumn()] != null) {
                    return false; //piece in the way
                }
            }
        } else {
            return false; //not valid move
        }

        // check destination square for capture
        Piece destinationPiece = board[newPosition.getRow()][newPosition.getColumn()];
        if (destinationPiece == null) {
            return true; //destination empty, move valid
        } else if (destinationPiece.getColor() != this.color) {
            return true; //destination has opponent's piece
        }

        return false; //destination has piece of same color or move invalid
    }

}
