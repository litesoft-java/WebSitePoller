package org.litesoft.websitepoller.gui;

import javax.swing.*;
import java.awt.*;

public class CanvasPanel extends JPanel {
    private final LineGraph mLineGraph = new LineGraph(Color.GREEN);

    public CanvasPanel( JLabel pLabel ) {
        setLayout( new BorderLayout() );
        add( pLabel, BorderLayout.NORTH );
    }

    public void addDataPoint( int pDuration ) {
        mLineGraph.addDataPoint( pDuration );
        invalidate();
    }

    @Override
    protected void paintComponent( Graphics g ) {
        super.paintComponent( g );
        Insets insets = getInsets();
        int currentWidth = getWidth() - insets.left - insets.right;
        int currentHeight = getHeight() - insets.top - insets.bottom;

        mLineGraph.render( (Graphics2D) g, new Point( insets.left + 2, insets.top ), new Dimension( currentWidth - 4, currentHeight - 4 ) );
    }
}
