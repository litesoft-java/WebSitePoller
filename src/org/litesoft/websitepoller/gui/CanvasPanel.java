package org.litesoft.websitepoller.gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CanvasPanel extends JPanel {
    private final List<Integer> mDataY = AddOnlyWindowedChunkedList.create( 5000 );
    private final LineGraph mLineGraph = new LineGraph( mDataY, Color.GREEN );

    public CanvasPanel( JLabel pLabel ) {
        setLayout( new BorderLayout() );
        add( pLabel, BorderLayout.NORTH );
    }

    public void addDataPoint( int pDuration ) {
        mDataY.add( pDuration );
        invalidate();
    }

    @Override
    protected void paintComponent( Graphics g ) {
        super.paintComponent( g );
        Insets insets = getInsets();
        int currentWidth = getWidth() - insets.left - insets.right;
        int currentHeight = getHeight() - insets.top - insets.bottom;

        mLineGraph.render( (Graphics2D) g, new Point( insets.left + 2, insets.top + 2 ), new Dimension( currentWidth - 4, currentHeight - 4 ) );
    }
}
