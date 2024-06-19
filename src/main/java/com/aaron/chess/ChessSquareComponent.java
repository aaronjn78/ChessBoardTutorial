package com.aaron.chess;

import javax.swing.*;
import java.awt.*;

//class for squares on chessboard
public class ChessSquareComponent extends JButton{

    private int row;
    private int col;

    public ChessSquareComponent(int row, int col) {
        this.row = row;
        this.col = col;
        initButton();
    }
    
    private void initButton() {
        // set preferred button size for uniformity
        setPreferredSize(new Dimension(64, 64));

        //set background color based on row and column for checkerboar effect
        if ((row + col) % 2 == 0) {
            setBackground(Color.LIGHT_GRAY);
        } else {
            setBackground(new Color(205, 133, 63));
        }

        //ensure text (symbols) are centered
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);

        //set font for visual enhancement. Initial size suggested by tutorial (40) caused pieces to not render correctly
        setFont(new Font("Serif", Font.BOLD,28));
    }

    public void setPieceSymbol(String symbol, Color color) {
        this.setText(symbol);
        this.setForeground(color); //adjust for better visibility against background
    }

    public void clearPieceSymbol() {
        this.setText("");
    }
}
