package com.aaron.chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.awt.Color;

public class ChessGameGUI extends JFrame {

    //create an 8x8 array of ChessSquareComponent
    private final ChessSquareComponent[][] squares = new ChessSquareComponent[8][8];
    
    //initialize a new ChessGame called 'game'
    private final ChessGame game = new ChessGame();
    
    private final Map<Class<? extends Piece>, String> pieceUnicodeMap = new HashMap<>() {
        {
            //map the pieces to unicode grapical representation of pieces
            put(Pawn.class, "\u265F");
            put(Rook.class, "\u265C");
            put(Knight.class, "\u265E");
            put(Bishop.class, "\u265D");
            put(Queen.class, "\u265B");
            put(King.class, "\u265A");
        }
    };

    //get the board for the current game at the current moment
    private ChessBoard board = game.getBoard();

    //initialize the GUI for the game
    public ChessGameGUI() {
        //set basic options for GUI
        setTitle("Chess Games");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //setup gridlayout
        setLayout(new GridLayout(8, 8));
        initializeBoard();
        addGameResetOption();
        pack(); //adjults window to fit board
        setVisible(true);
    }

    private void initializeBoard() {
        //setup coordinates for squares on board
        for (int row = 0; row < squares.length; row++){
            for (int col = 0; col < squares[row].length; col++){

                //coordinates on board can't and won't change
                final int finalRow = row;
                final int finalCol = col;
                ChessSquareComponent square = new ChessSquareComponent(row, col);

                //add a mouse listener to each square to detect mouse clicks on that square
                square.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleSquareClick(finalRow, finalCol);
                    }
                });
                add(square);
                squares[row][col] = square;
            }            
        }

        refreshBoard();
    }

    private void refreshBoard(){
        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){
                Piece piece = board.getPiece(row, col);

                if (piece != null) {
                    //if using unicode symbols
                    String symbol = pieceUnicodeMap.get(piece.getClass());
                    Color color = (piece.getColor() == PieceColor.WHITE) ? Color.WHITE :Color.BLACK;
                    squares[row][col].setPieceSymbol(symbol, color);
                    //or, if updating with icons or any other graphical representation
                } 
                else {
                    //ensure this mehod clears the square
                    squares[row][col].clearPieceSymbol();
                }
            }
        }

    }

    private void handleSquareClick(int row, int col) {

        boolean moveResult = game.handleSquareSelection(row, col);

        clearHighlights();
        if (moveResult){
            //if a move was made, refresh and check game state without hightlighting new moves
            refreshBoard();
            checkGameState();
            checkGameOver();
        }
       
        else if (game.isPieceSelected()) {
            //if no move was made but a piece is selected, hightlight its legal moves
            //tutorial code does not highlight the piece selected. Added this feature. 
                if (board.getPiece(row, col).getColor().equals(game.getCurrentPlayerColor())){
                squares[row][col].setBackground(Color.BLUE);
                hightlightLegalMoves(new Position(row, col));
                }
                else {
                    JOptionPane.showMessageDialog(this, "Not this color's turn");
                    game.clearSelectedPosition();
                }            
        }
    }
    

    private void checkGameState(){
        PieceColor currentPlayer = game.getCurrentPlayerColor();
        boolean inCheck = game.isInCheck(currentPlayer);

        if (inCheck) {
            JOptionPane.showMessageDialog(this, currentPlayer + " is in check!");
        }
    }

    private void hightlightLegalMoves(Position position) {
        List<Position> legalMoves = game.getLegalMovesForPieceAt(position);
        for (Position move : legalMoves) {
            squares[move.getRow()][move.getColumn()].setBackground(Color.GREEN);
        }
    }

    private void clearHighlights(){
        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){
                squares[row][col].setBackground((row + col) % 2 == 0? Color.LIGHT_GRAY : new Color(205, 133,63));
            }
        }
    }

    //add menu option to reset game
    private void addGameResetOption() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenuItem resetItem = new JMenuItem("Reset");

        menuBar.setFocusTraversalKeysEnabled(true);
        resetItem.addActionListener(e -> resetGame());
        gameMenu.add(resetItem);
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
    }

    private void resetGame() {
        game.resetGame();
        refreshBoard();
    }
    
    private void checkGameOver() {
        if (game.isCheckMate(game.getCurrentPlayerColor())) {
            int response = JOptionPane.showConfirmDialog(this, "Checkmate! Would you like to play again?", "GAme Over", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_NO_OPTION) {
                resetGame();
            }
            else {
                System.exit(0);
            }
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChessGameGUI::new);
    }
}
