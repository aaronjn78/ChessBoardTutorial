package com.aaron.chess;

public class Queen extends Piece{
    
    public Queen(PieceColor color, Position position) {
        super(color, position);
    }

    @Override
    public boolean isValidMove(Position newPosition, Piece[][] board) {

        if (newPosition.equals(this.position)) {
            return false; //can't move to same position
        }

        int rowDiff = Math.abs(position.getRow() - newPosition.getRow());
        int colDiff = Math.abs(position.getColumn() - newPosition.getColumn());

        //check for straight line movement
        boolean straightLine = this.position.getRow() == newPosition.getRow() || this.position.getColumn() == newPosition.getColumn();

        //eheck for diagonal movement
        boolean diagonal = rowDiff == colDiff;

        if (!straightLine && !diagonal) {
            return false; //movement is not straight or diagonal
        }

        //calulcate direction of movment
        //x == y = 0, x < y = -1, x > y = 1
        //have to put new position first so movement is in positive direction
        int rowDirection = Integer.compare(newPosition.getRow(), this.position.getRow());
        int colDirection = Integer.compare(newPosition.getColumn(), this.position.getColumn());

        //check for any pieces in the path
        //renaming these variables from currentRow/Col as shown in the tutorial to nextRow/Col as it makes better sense to show that the code is checking the next space for a piece

        int nextRow = this.position.getRow() + rowDirection;
        int nextCol = this.position.getColumn() + colDirection;

        while(nextCol != newPosition.getRow() || nextCol != newPosition.getColumn()){

            if (board[nextRow][nextCol] != null){
                return false; //path blocked
            }

            nextRow += rowDirection;
            nextCol += colDirection;
        }

        //move valid if destination is empty or contains an opposing piece
        Piece destinationPiece = board[newPosition.getRow()][newPosition.getColumn()];
        return destinationPiece == null || destinationPiece.getColor() != this.getColor();



    /* code I used prior to looking at the tutorial code. Used coding from Rook and Bishop as the Queen uses a combination of movement logic from both of these pieces.  Added the final else statement after looking at the tutorial after I realized my initial code did not take into account a movement that was either not straight or diagonal

        int rowDiff = Math.abs(position.getRow() - newPosition.getRow());

        if (rowDiff == 0){
            //queen along a row
            int columnStart = Math.min(position.getColumn(), newPosition.getColumn()) + 1;
            int columnEnd = Math.max(position.getColumn(), newPosition.getColumn());
            for (int column = columnStart; column < columnEnd; column++){
                if (board[position.getRow()][column] != null){
                    return false; //piece in the way
                }
            }
            
        }else if (colDiff == 0) {
            //queen moving along a column
             int rowStart = Math.min(position.getRow(), newPosition.getRow()) + 1;
             int rowEnd = Math.max(position.getRow(), newPosition.getRow());
             for (int row = rowStart; row < rowEnd; row++) {
                 if (board[row][position.getColumn()] != null) {
                     return false; //piece in the way
                 }
             }
        } else if (rowDiff == colDiff){
            //queen moving along diagonal
            int rowDirection = newPosition.getRow() > position.getRow() ? 1: -1;
            int colDirection = newPosition.getColumn() > position.getColumn() ? 1: -1;
            int steps = rowDiff - 1; //number of squares to check for obstructions
    
            for (int i = 1; i <= steps; i++){
                if (board[position.getRow() + i * rowDirection][position.getColumn() + i * colDirection] != null){
                    return false; //piece in the way
                }
            }

        } else {
            return false; //move is either not straight or diagonal
        }
        
        Piece targetPiece = board[newPosition.getRow()][newPosition.getColumn()];

        if(targetPiece == null) {
            return true; //space is empty
        } else {
            return targetPiece.getColor() != this.getColor();
        }
*/
    }
        
}
