package org.litesoft.websitepoller.gui;

import org.litesoft.commonfoundation.base.*;
import org.litesoft.commonfoundation.typeutils.*;
import org.litesoft.websitepoller.support.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class SimpleWST implements WidgetSourceTargetAccessor,
                                  SourceTarget {
    public static final int MIN_LINES = 9;

    private final JPanel mTitledBorderedPanel = new JPanel() {
        private boolean mTriggered;

        @Override
        public void paint( Graphics g ) {
            super.paint( g );
            if ( !mTriggered ) {
                mTriggered = true;
                SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        sizeStuff();
                    }
                } );
            }
        }
    };
    private final JLabel mLabel = new JLabel( "..." );
    private final CanvasPanel mCanvasPanel = new CanvasPanel( mLabel );

    public SimpleWST( InfoSource pSource ) {
        TitledBorder zBorder = BorderFactory.createTitledBorder( BorderFactory.createLineBorder( Color.black ),
                                                                 pSource.getID() );
        zBorder.setTitleJustification( TitledBorder.LEFT );
        mTitledBorderedPanel.setBorder( zBorder );
        mTitledBorderedPanel.setLayout( new BorderLayout() );

        mTitledBorderedPanel.add( mCanvasPanel, BorderLayout.CENTER );

        updateWith( pSource );
    }

    private void sizeStuff() {
//        Dimension zSize = mTitledBorderedPanel.getSize();
//        mCanvasPanel.setSize( new Dimension( zSize.width - 5, zSize.height - 5 ) );
        //    System.out.println( "SimpleWST.sizeStuff: " + mID );
        //    System.out.println( "    TBP: " + mTitledBorderedPanel.getSize() );
        //    System.out.println( "     CP: " + mCanvasPanel.getSize() );
        //    System.out.println( "    Lbl: " + mLabel.getSize() );
    }

    @Override
    public Component getWidget() {
        return mTitledBorderedPanel;
    }

    @Override
    public SourceTarget getTarget() {
        return this;
    }

    @Override
    public void updateWith( InfoSource pSource ) {
        String[] zTargetLines = Strings.toLines( Strings.padRight( "", '\n', MIN_LINES ) );
        zTargetLines[0] = firstLine( pSource );
        String[] zSourceLines = Strings.toLines( ConstrainTo.notNull( pSource.getMessage() ) );
        for ( int i = 0; i < zSourceLines.length; ) {
            String zSourceLine = zSourceLines[i++];
            zTargetLines[i] = zSourceLine;
        }
        mLabel.setText( htmlize( zTargetLines ) );
    }

    private String htmlize( String[] pLines ) {
        StringBuilder sb = new StringBuilder();
        sb.append( "<html>" );
        for ( String zLine : pLines ) {
            sb.append( zLine ).append( "<br>" );
        }
        return sb.append( "</html>" ).toString();
    }

    private String firstLine( InfoSource pSource ) {
        StringBuilder sb = new StringBuilder( "Duration: " );

        String s = "";
        Integer zDuration = pSource.getPollDurationMillisecs();
        if ( zDuration != null ) {
            mCanvasPanel.addDataPoint(zDuration);
            s = Integers.zeroPadIt( 4, zDuration );
            s = s.substring( 0, s.length() - 3 ) + "." + s.substring( s.length() - 3 );
        }
        return sb.append( Strings.padIt( 11, s ) ).toString();
    }
}
